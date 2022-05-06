package org.atomicoke.logic.modules.msg.packet.status;

import io.github.fzdwx.inf.socket.WebSocket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atomicoke.inf.common.web.model.Rest;
import org.atomicoke.logic.modules.msg.WsPacket;

@EqualsAndHashCode(callSuper = true)
@Data
public class SuccessPacket<OUT> extends WsPacket {

    private String message;

    private String randomId;

    private final static String type = Type.success;

    private OUT data;

    public SuccessPacket(final OUT data, final String randomId) {
        this(data, Rest.SUCCESS_MESSAGE, randomId);
    }

    public SuccessPacket(final OUT data, final String message, final String randomId) {
        this.randomId = randomId;
        this.data = data;
        this.message = message;
    }

    @Override
    public WsPacket mountWebsocket(final WebSocket ws) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String type() {
        return type;
    }
}