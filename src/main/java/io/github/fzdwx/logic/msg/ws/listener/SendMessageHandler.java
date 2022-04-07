package io.github.fzdwx.logic.msg.ws.listener;

import io.github.fzdwx.lambada.Lang;
import io.github.fzdwx.logic.contants.ChatConst;
import io.github.fzdwx.logic.msg.ws.UserWsConn;
import io.github.fzdwx.logic.msg.ws.listener.event.SendMessageEvent;
import io.github.fzdwx.logic.msg.ws.packet.ChatMessagePacket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/6 15:00
 */
@Component
@Slf4j
public class SendMessageHandler {

    @EventListener(SendMessageEvent.class)
    public void handler(SendMessageEvent event) {
        final var message = event.getMessage();
        if (Lang.eq(message.getSessionType(), ChatConst.SessionType.ALL)) {
            sendAll(event, message);
            return;
        }
    }

    private void sendAll(final SendMessageEvent event, final ChatMessagePacket message) {
        final var text = message.encode();
        UserWsConn.foreach((id, ws) -> {
            if (event.noNeedSend(id)) {
                return;
            }

            ws.send(text).addListener(f -> {
                if (!f.isSuccess()) {
                    log.info("发送消息失败", f.cause());
                }
            });
        });
    }
}