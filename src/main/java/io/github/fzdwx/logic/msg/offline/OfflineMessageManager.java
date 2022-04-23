package io.github.fzdwx.logic.msg.offline;

import cn.hutool.extra.spring.SpringUtil;
import io.github.fzdwx.inf.middleware.redis.Redis;
import io.github.fzdwx.logic.msg.domain.resp.ChatMessageResp;
import io.github.fzdwx.logic.msg.ws.packet.ReplayChatPacket;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/12 15:48
 */
@Component
public class OfflineMessageManager implements InitializingBean {

    // 未读消息最小的id
    private final static String MIN_ID_KEY_PREFIX = "msg:seq:";
    private final static String COLLECTION = "chat";

    private static MongoTemplate mongoTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        mongoTemplate = SpringUtil.getBean(MongoTemplate.class);
    }

    public static ReplayChatPacket.Data incrSeqAndSaveToMongo(final Long userId, final ChatMessageResp resp) {
        final var seq = incrSeq(userId.toString());

        final var chatMessageResp = resp.copy(userId, seq);

        saveToMongo(chatMessageResp);

        return new ReplayChatPacket.Data(chatMessageResp.getMessageId(),chatMessageResp.getBoxOwnerId(), seq);
    }

    public static Long incrSeq(final String userId) {
        return Redis.incr(getSeqKey(userId));
    }

    public static void saveToMongo(final ChatMessageResp chatMessageResp) {
        mongoTemplate.insert(chatMessageResp, COLLECTION);
    }

    public static void saveToMongo(final List<ChatMessageResp> chatMessageResps) {
        mongoTemplate.insert(chatMessageResps, COLLECTION);
    }

    private static String getSeqKey(final String recvUserId) {
        return MIN_ID_KEY_PREFIX + recvUserId;
    }
}