package io.github.fzdwx.logic.msg.ws.packet;

import io.github.fzdwx.inf.msg.WebSocket;
import io.github.fzdwx.logic.msg.ws.WsPacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

        public Data(final String messageId, final Long boxOwnerId, final Long seq) {
            this.messageId = messageId;
            this.boxOwnerSeq = seq.toString();
            this.boxOwnerId = boxOwnerId.toString();
        }
    }
}