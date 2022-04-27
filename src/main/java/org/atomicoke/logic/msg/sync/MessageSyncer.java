package org.atomicoke.logic.msg.sync;

import cn.hutool.extra.spring.SpringUtil;
import org.atomicoke.inf.common.util.Json;
import org.atomicoke.inf.middleware.redis.Redis;
import org.atomicoke.logic.msg.domain.resp.ChatMessageResp;
import org.atomicoke.logic.msg.domain.resp.SysInfoResp;
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

    // 未读消息最小的id
    private final static String CHAT_COLLECTION = "chat";
    private final static String SYS_INFO_COLLECTION = "sys_info";
    private final static String CHAT_SEQ_PREFIX = "msg:seq:" + CHAT_COLLECTION + ":";

    private final static String SYS_INFO_SEQ_PREFIX = "msg:seq:" + SYS_INFO_COLLECTION + ":";

    private static MongoTemplate mongoTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        mongoTemplate = SpringUtil.getBean(MongoTemplate.class);
    }

    public static Long incrSeq(final String userId) {
        return Redis.incr(getSeqKey(userId));
    }

    public static void saveChatToMongo(final ChatMessageResp chatMessageResp) {
        saveToMongo(Json.toJson(chatMessageResp), CHAT_COLLECTION);
    }

    public static void saveSysInfoToMongo(final SysInfoResp sysInfoResp) {
        saveToMongo(Json.toJson(sysInfoResp), SYS_INFO_COLLECTION);
    }

    public static void saveToMongo(String json, String collection) {
        mongoTemplate.insert(json, collection);
    }


    public static ReplayPacket.Data incrSeqAndSaveToMongo(final Long userId, final ChatMessageResp resp) {
        final var seq = incrSeq(userId.toString());

        final var chatMessageResp = resp.copy(userId, seq);

        saveChatToMongo(chatMessageResp);

        return new ReplayPacket.Data(chatMessageResp.getMessageId(), chatMessageResp.getBoxOwnerId(), chatMessageResp.getBoxOwnerSeq());
    }

    public static ReplayPacket.Data incrSeqAndSaveToMongo(final Long userId, final SysInfoResp resp) {
        final var seq = incrSeq(userId.toString());

        final var sysInfoResp = resp.copy(userId, seq);

        saveSysInfoToMongo(sysInfoResp);

        return new ReplayPacket.Data(sysInfoResp.getMessageId(), sysInfoResp.getBoxOwnerId(), sysInfoResp.getBoxOwnerSeq());
    }

    public static void saveChatToMongo(final List<ChatMessageResp> chatMessageResps) {
        mongoTemplate.insert(chatMessageResps, CHAT_COLLECTION);
    }

    public static void saveSysInfoToMongo(final List<SysInfoResp> sysInfoResps) {
        mongoTemplate.insert(sysInfoResps, CHAT_COLLECTION);
    }

    public static MessageSyncResp sync(final MessageSyncReq req) {
        req.check();

        final var chatMessageResps = mongoTemplate.find(req.toQuery(), ChatMessageResp.class, CHAT_COLLECTION);

        return MessageSyncResp.of(chatMessageResps, getSeq(req.getUserId()));
    }

    private static String getSeq(final String userId) {
        return Redis.get(getSeqKey(userId));
    }

    private static String getSeqKey(final String recvUserId) {
        return CHAT_SEQ_PREFIX + recvUserId;
    }
}