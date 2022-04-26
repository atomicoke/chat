package org.atomicoke.logic.msg.ws.packet.chat;

import io.github.fzdwx.inf.msg.WebSocket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atomicoke.logic.msg.ws.WsPacket;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReplayChatPacket extends WsPacket {

    private String message;

    private String randomId;

    private String type = Type.replayChat;

    private Data data;

    public ReplayChatPacket(final Data data, final String randomId) {
        this.data = data;
        this.randomId = randomId;
    }


    @Override
    public WsPacket mountWebsocket(final WebSocket ws) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String type() {
        return type;
    }

    @lombok.Data
    public static class Data {

        private String messageId;

        private String boxOwnerId;

        private String boxOwnerSeq;

        public Data(final String messageId, final String boxOwnerId, final String seq) {
            this.messageId = messageId;
            this.boxOwnerSeq = seq;
            this.boxOwnerId = boxOwnerId;
        }
    }
}