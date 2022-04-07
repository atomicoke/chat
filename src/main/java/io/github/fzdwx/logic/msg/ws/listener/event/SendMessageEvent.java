package io.github.fzdwx.logic.msg.ws.listener.event;

import io.github.fzdwx.inf.common.event.Event;
import io.github.fzdwx.lambada.Lang;
import io.github.fzdwx.logic.msg.ws.packet.ChatMessagePacket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/6 15:02
 * @see io.github.fzdwx.logic.msg.ws.listener.SendMessageHandler#handler(SendMessageEvent)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageEvent implements Event {

    private String senderId;
    private ChatMessagePacket message;

    public boolean noNeedSend(final String id) {
        return Lang.eq(id, this.senderId);
    }
}