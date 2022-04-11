package io.github.fzdwx.logic.msg.ws.packet;

import cn.hutool.core.util.ArrayUtil;
import io.github.fzdwx.inf.msg.WebSocket;
import io.github.fzdwx.logic.msg.ws.WsPacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

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