package org.atomicoke.logic.modules.msg.packet.status;

import cn.hutool.core.util.ArrayUtil;
import socket.WebSocket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atomicoke.logic.modules.msg.WsPacket;

@EqualsAndHashCode(callSuper = true)
@Data
public class ErrorPacket extends WsPacket {

    private String message;

    private String randomId;

    private StackTraceElement[] stackTrace;

    private final static String type = Type.err;

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