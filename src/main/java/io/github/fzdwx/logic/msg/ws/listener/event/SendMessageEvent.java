package io.github.fzdwx.logic.msg.ws.listener.event;

import io.github.fzdwx.inf.common.event.Event;
import io.github.fzdwx.lambada.Lang;
import io.github.fzdwx.logic.msg.ws.listener.EventHandler;
import io.github.fzdwx.logic.msg.ws.packet.ChatMessagePacket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/6 15:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageEvent implements Event {

    private String senderId;
    private ChatMessagePacket message;

    /**
     * routing
     *
     * @see EventHandler#handler(SendMessageEvent)
     */
    public static void routing(final String id, final ChatMessagePacket packet) {
        Event.routing(new SendMessageEvent(id, packet));
    }

    public boolean noNeedSend(final String id) {
        return Lang.eq(id, this.senderId);
    }
}