package io.github.fzdwx.logic.msg.ws;

import cn.hutool.extra.spring.SpringUtil;
import io.github.fzdwx.inf.common.exc.Err;
import io.github.fzdwx.inf.common.util.Json;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import io.github.fzdwx.inf.msg.WebSocket;
import io.github.fzdwx.logic.msg.ws.handler.ChatMessagePacketHandler;
import io.github.fzdwx.logic.msg.ws.packet.ChatMessagePacket;
import io.github.fzdwx.logic.msg.ws.packet.ErrorPacket;
import io.github.fzdwx.logic.msg.ws.packet.SuccessPacket;
import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * websocket packet.
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/7 11:20
 */
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
    public static <T extends WsPacket> T decode(String s) {
        final var jsonObject = Json.parseObj(s);
        final var type = jsonObject.getStr("type");
        if (type == null) {
            return null;
        }

        return jsonObject.toBean(TypeClassMapping.map.get(type), true);
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

    public void sendSuccess() {
        this.ws.send(this.newSuccessPacket(null).encode());
    }

    public interface Type {

        String err = "err";

        String success = "success";

        String chat = "chat";
    }

    public interface Handler<Packet extends WsPacket> {

        void handle(Packet packet);

        default UserInfo userInfo(@NotNull Packet packet) {
            return UserWsConn.userInfo(packet.webSocket());
        }
    }

    public static class TypeClassMapping {

        static Map<String, Class<? extends WsPacket>> map = new HashMap<>();

        static {
            map.put(Type.chat, ChatMessagePacket.class);
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
}