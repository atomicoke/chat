package io.github.fzdwx.logic.msg.ws;

import io.github.fzdwx.inf.common.exc.Err;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import io.github.fzdwx.inf.msg.WebSocket;
import io.github.fzdwx.lambada.fun.State;
import io.netty.channel.ChannelFuture;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiConsumer;

import static io.github.fzdwx.inf.common.exc.Err.verify;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/5 18:05
 */
@Slf4j
public class UserWsConn {

    private final static Map<String, WebSocket> WEB_SOCKET_MAP = new ConcurrentSkipListMap<>();
    private final static AttributeKey<UserInfo> KEY = AttributeKey.valueOf("userInfo");

    public static void add(String userId, WebSocket webSocket) {
        WEB_SOCKET_MAP.put(userId, webSocket);
        log.info("add userId:{}", userId);
    }

    public static void remove(final String userId) {
        WEB_SOCKET_MAP.remove(userId);
        log.info("remove userId:{}", userId);
    }

    public static State<ChannelFuture> sendTo(String userId, String msg) {
        final var conn = WEB_SOCKET_MAP.get(userId);
        if (conn == null) {
            return State.failure(Err.verify("user conn  not found :" + userId));
        }

        return State.success(conn.send(msg));
    }

    public static void foreach(BiConsumer<String, WebSocket> consumer) {
        WEB_SOCKET_MAP.forEach(consumer);
    }

    public static void attach(final WebSocket ws, final UserInfo userInfo) {
        ws.channel().attr(KEY).set(userInfo);
    }

    public static UserInfo get(WebSocket webSocket) {
        if (webSocket == null) throw verify("webSocket is null");
        return webSocket.channel().attr(KEY).get();
    }
}