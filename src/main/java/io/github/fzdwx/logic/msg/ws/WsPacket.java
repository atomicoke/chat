package io.github.fzdwx.logic.msg.ws;

import cn.hutool.extra.spring.SpringUtil;
import io.github.fzdwx.inf.common.exc.Err;
import io.github.fzdwx.inf.common.util.Json;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import io.github.fzdwx.inf.msg.WebSocket;
import io.github.fzdwx.logic.msg.ws.packet.ChatMessagePacket;
import io.github.fzdwx.logic.msg.ws.packet.handler.ChatMessagePacketHandler;
import io.netty.channel.ChannelFuture;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/7 11:20
 */
public abstract class WsPacket {

    public String encode() {
        return Json.toJson(this);
    }

    /**
     * decode json to ws packet.
     *
     * @apiNote if not have type,then return null.
     */
    @Nullable
    public static <T extends WsPacket> T decode(String s) {
        final var jsonObject = Json.parseObj(s);
        final var type = jsonObject.getStr("type");
        if (type == null) {
            return null;
        }

        return jsonObject.toBean(TypeClassMapping.map.get(type), true);
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
        handler.handle(p);
    }

    protected String randomId;

    protected String type;

    protected WebSocket ws;


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

    public ChannelFuture sendError(final Err err) {
        return this.ws.send(new ErrorPacket(err, this.randomId).encode());
    }

    public ChannelFuture sendError(final String errorMessage) {
        return this.ws.send(new ErrorPacket(errorMessage, this.randomId).encode());
    }

    public interface Type {

        String err = "err";

        String chat = "chat";
    }

    public static class TypeClassMapping {

        static Map<String, Class<? extends WsPacket>> map = new HashMap<>();

        static {
            map.put(Type.chat, ChatMessagePacket.class);
        }
    }

    public interface Handler<Packet extends WsPacket> {

        void handle(Packet packet);

        default UserInfo getUserInfo(Packet packet) {
            return UserWsConn.get(packet.webSocket());
        }
    }

    public static class TypePacketHandlerMapping {

        static Map<String, Handler<? extends WsPacket>> map = new HashMap<>();

        static {
            Handler<ChatMessagePacket> bean = SpringUtil.getBean(ChatMessagePacketHandler.class);
            map.put(Type.chat, bean);
        }

        public static <Packet extends WsPacket> Handler<Packet> get(String type) {
            return (Handler<Packet>) map.get(type);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ErrorPacket extends WsPacket {

        private String errorMessage;

        private String randomId;

        public ErrorPacket(final Err err, final String randomId) {
            this.randomId = randomId;
            this.errorMessage = err.getMessage();
        }

        public ErrorPacket(final String errorMessage, final String randomId) {
            this.randomId = randomId;
            this.errorMessage = errorMessage;
        }

        @Override
        public WsPacket mountWebsocket(final WebSocket ws) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String type() {
            return Type.err;
        }
    }
}