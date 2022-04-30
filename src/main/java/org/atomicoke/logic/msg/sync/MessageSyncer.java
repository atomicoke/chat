package org.atomicoke.logic.msg.sync;

import cn.hutool.extra.spring.SpringUtil;
import org.atomicoke.inf.middleware.redis.Redis;
import org.atomicoke.logic.msg.domain.model.Message;
import org.atomicoke.logic.msg.domain.resp.ChatMessageResp;
import org.atomicoke.logic.msg.sync.model.MessageSyncReq;
import org.atomicoke.logic.msg.sync.model.MessageSyncResp;
import org.atomicoke.logic.msg.ws.packet.chat.ReplayPacket;
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

    private final static String COLLECTION = "msg";
    private final static String SEQ_PREFIX = "msg:seq:";
    private static MongoTemplate mongoTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        mongoTemplate = SpringUtil.getBean(MongoTemplate.class);
    }

    public static Long incrSeq(final Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is null");
        }
        return Redis.incr(getSeqKey(userId.toString()));
    }

    public static void saveToMongo(final Message message) {
        mongoTemplate.insert(message.encode(), COLLECTION);
    }

    public static ReplayPacket.Data incrSeqAndSaveToMongo(final Long userId, final ChatMessageResp resp) {
        final var seq = incrSeq(userId);

        final var message = resp.toMessage(userId, seq);

        saveToMongo(message);

        return new ReplayPacket.Data(resp.getChatId(), message.getBoxOwnerId(), message.getBoxOwnerSeq());
    }

    public static void saveToMongo(final List<String> messages) {
        mongoTemplate.insert(messages, COLLECTION);
    }

    public static MessageSyncResp syncMessage(final MessageSyncReq req) {
        req.check();

        final var messages = mongoTemplate.find(req.toQuery(), Message.class, COLLECTION);

        return MessageSyncResp.of(messages, getSeq(req.getUserId()));
    }

    private static String getSeq(final String userId) {
        return Redis.get(getSeqKey(userId));
    }

    private static String getSeqKey(final String recvUserId) {
        return SEQ_PREFIX + recvUserId;
    }
}