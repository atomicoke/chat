package io.github.fzdwx.logic.msg.offline;

import io.github.fzdwx.inf.common.util.Json;
import io.github.fzdwx.inf.middleware.redis.Redis;
import io.github.fzdwx.lambada.Coll;
import io.github.fzdwx.logic.msg.ws.packet.resp.ChatMessageResp;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/12 15:48
 */
@Component
public class OfflineMessageManager implements InitializingBean {

    private final static String KEY_PREFIX = "conn:";

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public static void push(final ChatMessageResp chatMessageResp) {
        String key = key(chatMessageResp);

        Coll.map(
                "fromId", "",
                "", "",
                "", ""
                );
        Redis.lpush(key, Json.toJson(chatMessageResp.getChatMessages()));
    }

    /**
     * build redis key.
     *
     * @return {@link String }
     */
    private static String key(ChatMessageResp chatMessageResp) {
        return KEY_PREFIX + chatMessageResp.getToId() + ":" + chatMessageResp.getFromId();
    }
}