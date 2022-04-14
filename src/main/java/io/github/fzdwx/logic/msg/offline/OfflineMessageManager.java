package io.github.fzdwx.logic.msg.offline;

import io.github.fzdwx.inf.middleware.redis.Redis;
import io.github.fzdwx.logic.msg.ws.packet.resp.ChatMessageResp;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/12 15:48
 */
@Component
public class OfflineMessageManager implements InitializingBean {

    private final static String KEY_PREFIX = "offline:msg:";

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    /**
     * key: offline:msg:userId
     * filed: fromId | value:  min message id
     */
    public static void push(final ChatMessageResp chatMessageResp) {
        String key = key(chatMessageResp);

        // 当 msgId 为null 或 msgId > minMsgId 时，更新 msgId
        final String msgId = Redis.hGet(key, chatMessageResp.getFromId());
        if (msgId == null || Long.parseLong(msgId) > chatMessageResp.getMinMessageId()) {
            Redis.hSet(key, chatMessageResp.getFromId(), chatMessageResp.getMinMessageId().toString());
        }
    }

    /**
     * build redis key.
     *
     * @return {@link String }
     */
    private static String key(ChatMessageResp chatMessageResp) {
        return KEY_PREFIX + chatMessageResp.getToId();
    }
}