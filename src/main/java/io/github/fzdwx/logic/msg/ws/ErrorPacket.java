package io.github.fzdwx.logic.msg.ws;

import io.github.fzdwx.inf.common.exc.Err;
import io.github.fzdwx.inf.msg.WebSocket;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ErrorPacket extends WsPacket {

    private String message;

    private String randomId;

    private String type = Type.err;

    public ErrorPacket(final Err err, final String randomId) {
        this.randomId = randomId;
        this.message = err.getMessage();
    }

    public ErrorPacket(final String message, final String randomId) {
        this.randomId = randomId;
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