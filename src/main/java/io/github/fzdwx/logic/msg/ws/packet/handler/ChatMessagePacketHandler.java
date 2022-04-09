package io.github.fzdwx.logic.msg.ws.packet.handler;

import io.github.fzdwx.inf.common.exc.Err;
import io.github.fzdwx.logic.contants.ChatConst;
import io.github.fzdwx.logic.domain.dao.ChatLogDao;
import io.github.fzdwx.logic.msg.ws.UserWsConn;
import io.github.fzdwx.logic.msg.ws.WsPacket;
import io.github.fzdwx.logic.msg.ws.packet.ChatMessagePacket;
import io.netty.channel.ChannelFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/8 17:19
 */
@Component
@RequiredArgsConstructor
public class ChatMessagePacketHandler implements WsPacket.Handler<ChatMessagePacket> {

    private final ChatLogDao chatLogDao;

    @Override
    public void handle(final ChatMessagePacket packet) {
        final Err err = packet.prepare();
        if (err != null) {
            packet.sendError(err);
        }

        switch (packet.getSessionType()) {
            case ChatConst.SessionType.ALL -> sendAll(packet);
            case ChatConst.SessionType.group -> sendGroup(packet);
            case ChatConst.SessionType.personal -> sendPersonal(packet);
            default -> packet.sendError("未知的会话类型:" + packet.getSessionType());
        }
    }

    private void sendPersonal(final ChatMessagePacket packet) {
        chatLogDao.saveWithTx(packet.toChatLogs(getUserInfo(packet)));

        UserWsConn.sendTo(packet.getToId(), "!23").then(s -> {
            if (s.isSuccess()) {
                final ChannelFuture channelFuture = s.get();
            }
        });
    }

    private void sendGroup(final ChatMessagePacket packet) {
        packet.toChatLogs(getUserInfo(packet));
    }

    private void sendAll(final ChatMessagePacket packet) {
        packet.toChatLogs(getUserInfo(packet));
    }
}