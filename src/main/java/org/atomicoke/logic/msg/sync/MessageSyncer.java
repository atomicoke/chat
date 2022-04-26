package org.atomicoke.logic.msg.sync;

import cn.hutool.extra.spring.SpringUtil;
import org.atomicoke.inf.common.util.Json;
import org.atomicoke.inf.middleware.redis.Redis;
import org.atomicoke.logic.msg.domain.resp.ChatMessageResp;
import org.atomicoke.logic.msg.sync.model.MessageSyncReq;
import org.atomicoke.logic.msg.sync.model.MessageSyncResp;
import org.atomicoke.logic.msg.ws.packet.chat.ReplayChatPacket;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用于消息同步.
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/12 15:48
 */
@Component
public class MessageSyncer implements InitializingBean {

    // 未读消息最小的id
    private final static String MIN_ID_KEY_PREFIX = "msg:seq:";
    private final static String COLLECTION = "chat";

    private static MongoTemplate mongoTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        mongoTemplate = SpringUtil.getBean(MongoTemplate.class);
    }

    public static Long incrSeq(final String userId) {
        return Redis.incr(getSeqKey(userId));
    }

    public static void saveToMongo(final ChatMessageResp chatMessageResp) {
        mongoTemplate.insert(Json.toJson(chatMessageResp), COLLECTION);
    }

    public static ReplayChatPacket.Data incrSeqAndSaveToMongo(final Long userId, final ChatMessageResp resp) {
        final var seq = incrSeq(userId.toString());

        final var chatMessageResp = resp.copy(userId, seq);

        saveToMongo(chatMessageResp);

        return new ReplayChatPacket.Data(chatMessageResp.getMessageId(), chatMessageResp.getBoxOwnerId(), chatMessageResp.getBoxOwnerSeq());
    }

    public static void saveToMongo(final List<ChatMessageResp> chatMessageResps) {
        mongoTemplate.insert(chatMessageResps, COLLECTION);
    }

    public static MessageSyncResp sync(final MessageSyncReq req) {
        req.check();

        final var chatMessageResps = mongoTemplate.find(req.toQuery(), ChatMessageResp.class, COLLECTION);

        return MessageSyncResp.of(chatMessageResps, getSeq(req.getUserId()));
    }

    private static String getSeq(final String userId) {
        return Redis.get(getSeqKey(userId));
    }

    private static String getSeqKey(final String recvUserId) {
        return MIN_ID_KEY_PREFIX + recvUserId;
    }
}