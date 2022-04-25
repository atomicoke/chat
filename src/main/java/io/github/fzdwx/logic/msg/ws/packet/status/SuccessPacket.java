package io.github.fzdwx.logic.msg.ws.packet.status;

import io.github.fzdwx.inf.common.web.model.Rest;
import io.github.fzdwx.inf.msg.WebSocket;
import io.github.fzdwx.logic.msg.ws.WsPacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SuccessPacket<OUT> extends WsPacket {

    private String message;

    private String randomId;

    private String type = Type.success;

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