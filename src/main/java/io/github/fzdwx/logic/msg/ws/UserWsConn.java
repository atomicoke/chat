package io.github.fzdwx.logic.msg.ws;

import io.github.fzdwx.inf.common.exc.Err;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import io.github.fzdwx.inf.msg.WebSocket;
import io.github.fzdwx.lambada.fun.State;
import io.github.fzdwx.logic.msg.ws.packet.ChatMessagePacket;
import io.netty.channel.ChannelFuture;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiConsumer;

import static io.github.fzdwx.inf.common.exc.Err.verify;

/**
 * manager user to websocket connection.
 *
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/5 18:05
 */
@Slf4j
public class UserWsConn {

    private final static Map<String, WebSocket> WEB_SOCKET_MAP = new ConcurrentSkipListMap<>();
    private final static AttributeKey<UserInfo> KEY_USER_INFO = AttributeKey.valueOf("userInfo");

    public static void add(String userId, WebSocket webSocket) {
        final var old = WEB_SOCKET_MAP.put(userId, webSocket);
        if (old != null) {
            old.close();
        }
        log.info("add userId:{}", userId);
    }

    @Nullable
    public static WebSocket get(@NotNull ChatMessagePacket packet) {
        return WEB_SOCKET_MAP.get(packet.getToId());
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

    public static void attachUserInfo(final WebSocket ws, final UserInfo userInfo) {
        attach(ws, KEY_USER_INFO, userInfo);
    }

    public static UserInfo userInfo(WebSocket webSocket) {
        if (webSocket == null) throw verify("webSocket is null");
        return webSocket.channel().attr(KEY_USER_INFO).get();
    }

    /**
     * attach attribute to channel
     */
    public static <VALUE> void attach(final WebSocket ws, final AttributeKey<VALUE> key, final VALUE value) {
        ws.channel().attr(key).set(value);
    }
}