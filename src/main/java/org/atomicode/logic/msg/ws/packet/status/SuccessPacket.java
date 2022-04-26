package org.atomicode.logic.msg.ws.packet.status;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atomicode.inf.common.web.model.Rest;
import org.atomicode.inf.msg.WebSocket;
import org.atomicode.logic.msg.ws.WsPacket;

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