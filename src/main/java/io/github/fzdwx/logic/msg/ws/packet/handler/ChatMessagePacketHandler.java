package io.github.fzdwx.logic.msg.ws.packet.handler;

import io.github.fzdwx.logic.msg.ws.UserWsConn;
import io.github.fzdwx.logic.msg.ws.WsPacket;
import io.github.fzdwx.logic.msg.ws.packet.ChatMessagePacket;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/8 17:19
 */
@Component
public class ChatMessagePacketHandler implements WsPacket.Handler<ChatMessagePacket> {

    @Override
    public void handle(final ChatMessagePacket packet) {
        System.out.println("!23");
        System.out.println(UserWsConn.get(packet.webSocket()));
    }
}