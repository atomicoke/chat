package org.atomicode.logic.msg.ws.handler;

import org.atomicode.logic.msg.ws.packet.chat.ChatMessagePacket;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/22 10:38
 */
class ChatMessagePacketHandlerTest {


    @Test
    void test1() {
        final ChatMessagePacket packet = mock(ChatMessagePacket.class);
        final ChatMessagePacketHandler chatHandler = mock(ChatMessagePacketHandler.class);

        chatHandler.handle(packet);
    }
}