package io.github.fzdwx.logic.msg.ws;

import io.github.fzdwx.inf.common.util.Json;
import io.github.fzdwx.logic.msg.ws.packet.ChatMessagePacket;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/7 11:20
 */
public abstract class WsPacket {

    protected String type;

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

    public interface Type {

        String chat = "chat";
    }

    public static class TypeClassMapping {

        static Map<String, Class<? extends WsPacket>> map = new HashMap<>();

        static {
            map.put(Type.chat, ChatMessagePacket.class);
        }
    }
}