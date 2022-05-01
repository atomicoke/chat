package org.atomicoke.logic.modules.msg.ws.packet.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.fzdwx.inf.msg.WebSocket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atomicoke.logic.modules.msg.ws.WsPacket;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReplayPacket extends WsPacket {

    private String message;

    private String randomId;

    @JsonIgnore
    private String type;

    private Data data;

    public ReplayPacket(final Data data, final String randomId, final String type) {
        this.data = data;
        this.randomId = randomId;
        this.type = type;
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

        private String chatId;

        private String boxOwnerId;

        private String boxOwnerSeq;

        public Data(final String chatId, final String boxOwnerId, final String seq) {
            this.chatId = chatId;
            this.boxOwnerSeq = seq;
            this.boxOwnerId = boxOwnerId;
        }
    }
}