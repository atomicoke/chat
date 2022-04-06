package io.github.fzdwx.logic.msg.ws.model.event;

import io.github.fzdwx.inf.common.event.Event;
import io.github.fzdwx.logic.msg.api.model.ChatMessageVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/6 15:02
 * @see io.github.fzdwx.logic.msg.ws.listener.SendMessageHandler
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageEvent implements Event {

    private String senderId;
    private ChatMessageVO message;
}