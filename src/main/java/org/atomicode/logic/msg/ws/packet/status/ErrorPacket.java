package org.atomicode.logic.msg.ws.packet.status;

import cn.hutool.core.util.ArrayUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atomicode.inf.msg.WebSocket;
import org.atomicode.logic.msg.ws.WsPacket;

@EqualsAndHashCode(callSuper = true)
@Data
public class ErrorPacket extends WsPacket {

    private String message;

    private String randomId;

    private StackTraceElement[] stackTrace;

    private String type = Type.err;

    public ErrorPacket(final Exception err, final String randomId) {
        this.randomId = randomId;
        this.message = err.getMessage();
        this.stackTrace = ArrayUtil.sub(err.getStackTrace(), 0, 5);
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