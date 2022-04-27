package org.atomicoke.logic.msg.ws;

import cn.hutool.extra.spring.SpringUtil;
import io.github.fzdwx.inf.msg.WebSocket;
import io.github.fzdwx.lambada.Lang;
import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.err.Err;
import org.atomicoke.inf.common.util.Json;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.inf.middleware.redis.Redis;
import org.atomicoke.logic.msg.ws.handler.ChatMessagePacketHandler;
import org.atomicoke.logic.msg.ws.handler.SysContactPacketHandler;
import org.atomicoke.logic.msg.ws.packet.chat.ChatMessagePacket;
import org.atomicoke.logic.msg.ws.packet.chat.ReplayPacket;
import org.atomicoke.logic.msg.ws.packet.status.ErrorPacket;
import org.atomicoke.logic.msg.ws.packet.status.SuccessPacket;
import org.atomicoke.logic.msg.ws.packet.sys.SysContactPacket;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * websocket packet.
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/7 11:20
 */
@Slf4j
public abstract class WsPacket {

    @Getter
    @Setter
    protected String randomId;
    @Getter
    @Setter
    protected String type;
    protected WebSocket ws;

    public String encode() {
        return Json.toJson(this);
    }

    /**
     * decode json to ws packet.
     *
     * @apiNote if not have type,then return null.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public static <T extends WsPacket> T decode(String s) {
        try {
            final var jsonObject = Json.parseObj(s);
            final var type = jsonObject.getString("type");
            if (type == null) {
                return null;
            }

            final Class<? extends WsPacket> clazz = TypeClassMapping.map.get(type);
            return (T) jsonObject.toJavaObject(clazz);
        } catch (Exception e) {
            log.error("decode json to ws packet error", e);
            return null;
        }
    }

    /**
     * new a success packet.
     */
    public static <OUT> SuccessPacket<OUT> newSuccessPacket(OUT data, WsPacket packet) {
        if (packet == null) {
            throw Err.verify("packet is null");
        }
        return new SuccessPacket<OUT>(data, packet.randomId);
    }

    public static ReplayPacket newReplayChatPacket(ReplayPacket.Data data, WsPacket packet) {
        if (packet == null) {
            throw Err.verify("packet is null");
        }
        return new ReplayPacket(data, packet.randomId, Type.replayChat);
    }

    public static ReplayPacket newReplaySysInfoPacket(ReplayPacket.Data data, WsPacket packet) {
        if (packet == null) {
            throw Err.verify("packet is null");
        }
        return new ReplayPacket(data, packet.randomId, Type.replaySys);
    }

    /**
     * new a success packet.
     */
    public <OUT> SuccessPacket<OUT> newSuccessPacket(OUT data) {
        return WsPacket.newSuccessPacket(data, this);
    }

    public WsPacket mountWebsocket(WebSocket ws) {
        this.ws = ws;
        return this;
    }

    public static <Packet extends WsPacket> void routing(Packet p) {
        final Handler<WsPacket> handler = TypePacketHandlerMapping.get(p.type());
        if (handler == null) {
            // todo send error for not found handler.
            return;
        }
        try {
            handler.handle(p);
        } catch (Exception e) {
            p.sendError(e);
            throw e;
        }
    }

    /**
     * 类型
     *
     * @return {@link String }
     * @apiNote e.g. {@code <pre>
     *    private String type = Type.chat;
     *
     *    @Override
     *    public String type() {
     *         return type;
     *    }
     * </pre>}
     */
    public abstract String type();

    public WebSocket webSocket() {
        return ws;
    }

    public final String randomId() {
        return randomId;
    }

    public ChannelFuture sendError(final Exception err) {
        return this.ws.send(new ErrorPacket(err, this.randomId).encode());
    }

    public ChannelFuture sendError(final String errorMessage) {
        return this.ws.send(new ErrorPacket(errorMessage, this.randomId).encode());
    }

    public ChannelFuture sendSuccess() {
        return this.ws.send(this.newSuccessPacket(null).encode());
    }

    public <OUT> ChannelFuture sendSuccess(OUT data) {
        return this.ws.send(this.newSuccessPacket(data).encode());
    }

    public interface Type {

        String err = "err";

        String success = "success";

        String replayChat = "replayChat";

        String chat = "chat";
        /**
         * 系统通讯录请求
         */
        String sysContact = "sysContact";

        String replaySys = "replaySys";
    }

    public interface Handler<Packet extends WsPacket> {
        String RANDOM_ID_MAP_CHAT_HISTORY_ID = "msg:map:";

        void handle(Packet packet);

        Duration getExpire();

        default UserInfo userInfo(@NotNull Packet packet) {
            return UserWsConn.userInfo(packet.webSocket());
        }

        /**
         * 获取在redis 中缓存的randomId对应的chatHistoryId
         *
         * @apiNote 可能返回null, 当返回null时, 说明没有这条消息或者消息已经过期
         */
        @org.checkerframework.checker.nullness.qual.Nullable
        default Long getMessageId(String randomId) {
            if (randomId == null || randomId.isEmpty()) {
                return null;
            }
            String key = RANDOM_ID_MAP_CHAT_HISTORY_ID + randomId;
            final String s = Redis.get(key);
            if (s == null) {
                return null;
            }

            return Long.parseLong(s);
        }

        /**
         * 在redis 中缓存 randomId对应的chatHistoryId,过期时间为30s
         */
        default void cacheRandomIdToChatHistoryId(String randomId, Long chatHistoryId) {
            if (Lang.isEmpty(randomId) || Lang.isNull(chatHistoryId)) {
                return;
            }
            String key = RANDOM_ID_MAP_CHAT_HISTORY_ID + randomId;
            Redis.set(key, chatHistoryId.toString(), getExpire());
        }
    }

    public static class TypeClassMapping {

        static Map<String, Class<? extends WsPacket>> map = new HashMap<>();

        static {
            map.put(Type.chat, ChatMessagePacket.class);

            //系统通讯录消息
            map.put(Type.sysContact, SysContactPacket.class);
        }
    }

    public static class TypePacketHandlerMapping {

        static Map<String, Handler<? extends WsPacket>> map = new HashMap<>();

        static {
            Handler<ChatMessagePacket> bean = SpringUtil.getBean(ChatMessagePacketHandler.class);
            Handler<SysContactPacket> sysContactPacketHandler = SpringUtil.getBean(SysContactPacketHandler.class);
            map.put(Type.chat, bean);
            map.put(Type.sysContact, sysContactPacketHandler);
        }

        public static <Packet extends WsPacket> Handler<Packet> get(String type) {
            return (Handler<Packet>) map.get(type);
        }
    }
}