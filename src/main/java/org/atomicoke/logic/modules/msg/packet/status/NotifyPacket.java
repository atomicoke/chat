package org.atomicoke.logic.modules.msg.packet.status;

import socket.WebSocket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atomicoke.logic.modules.msg.WsPacket;

/**
 * @author zhiyuan
 * @since 2022/4/28
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NotifyPacket<OUT> extends WsPacket {

    private final static String type = WsPacket.Type.notify;

    private OUT data;

    private String notifyType;


    public NotifyPacket(final OUT data, final String notifyType) {
        this.data = data;
        this.notifyType = notifyType;
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