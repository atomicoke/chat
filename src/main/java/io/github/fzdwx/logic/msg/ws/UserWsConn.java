package io.github.fzdwx.logic.msg.ws;

import io.github.fzdwx.inf.msg.WebSocket;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiConsumer;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/5 18:05
 */
@Slf4j
public class UserWsConn {

    private final static Map<String, WebSocket> WEB_SOCKET_MAP = new ConcurrentSkipListMap<>();

    public static void add(String userId, WebSocket webSocket) {
        WEB_SOCKET_MAP.put(userId, webSocket);
        log.info("add userId:{}", userId);
    }

    public static void remove(final String userId) {
        WEB_SOCKET_MAP.remove(userId);
        log.info("remove userId:{}", userId);
    }

    public static void sendTo(String userId, String msg) {
        final var conn = WEB_SOCKET_MAP.get(userId);
        if (conn == null) {
            return;
        }

        conn.send(msg);
    }

    public static void foreach(BiConsumer<String, WebSocket> consumer) {
        WEB_SOCKET_MAP.forEach(consumer);
    }
}