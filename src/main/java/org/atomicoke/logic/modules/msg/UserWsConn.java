package org.atomicoke.logic.modules.msg;

import socket.WebSocket;
import io.github.fzdwx.lambada.Lang;
import io.github.fzdwx.lambada.fun.State;
import io.netty.channel.ChannelFuture;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiConsumer;

import static io.github.fzdwx.lambada.fun.State.failure;
import static io.github.fzdwx.lambada.fun.State.success;
import static org.atomicoke.inf.common.err.Err.verify;

/**
 * manager user to websocket connection.
 *
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/5 18:05
 */
@Slf4j
@Component
public class UserWsConn {

    private final static Map<Long, WebSocket> WEB_SOCKET_MAP = new ConcurrentSkipListMap<>();
    private final static AttributeKey<UserInfo> KEY_USER_INFO = AttributeKey.valueOf("userInfo");

    public static void add(final Long userId, final WebSocket webSocket) {
        if (Lang.isNull(userId) || Lang.isNull(webSocket)) {
            throw verify("userId or webSocket is null");
        }

        final var old = WEB_SOCKET_MAP.put(userId, webSocket);
        if (old != null) {
            old.close();
        }
        log.info("add userId:{}", userId);
    }

    @Nullable
    public static WebSocket get(@NotNull final Long userId) {
        return WEB_SOCKET_MAP.get(userId);
    }

    public static void remove(@NotNull final Long userId) {
        WEB_SOCKET_MAP.remove(userId);
        log.info("remove userId:{}", userId);
    }

    public static State<ChannelFuture> sendTo(@NotNull final Long userId, String msg) {
        final var conn = WEB_SOCKET_MAP.get(userId);
        if (conn == null) {
            return failure(verify("user conn  not found :" + userId));
        }

        return success(conn.send(msg));
    }

    public static void foreach(BiConsumer<Long, WebSocket> consumer) {
        WEB_SOCKET_MAP.forEach(consumer);
    }

    public static void attachUserInfo(final WebSocket ws, final UserInfo userInfo) {
        if (Lang.isNull(userInfo) || Lang.isNull(userInfo.getIdLong())) {
            throw verify("userInfo or userInfo.idLong is null");
        }

        attach(ws, KEY_USER_INFO, userInfo);
    }

    public static UserInfo userInfo(WebSocket webSocket) {
        if (Lang.isNull(webSocket)) {
            throw verify("webSocket is null");
        }

        return webSocket.channel().attr(KEY_USER_INFO).get();
    }

    /**
     * attach attribute to channel
     */
    public static <VALUE> void attach(final WebSocket ws, final AttributeKey<VALUE> key, final VALUE value) {
        if (Lang.isNull(ws)) {
            throw verify("webSocket is null");
        }

        ws.channel().attr(key).set(value);
    }
}