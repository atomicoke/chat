package io.github.fzdwx.logic.msg.ws.handler;

import io.github.fzdwx.inf.common.contants.ChatConst;
import io.github.fzdwx.inf.common.err.Err;
import io.github.fzdwx.lambada.Seq;
import io.github.fzdwx.logic.domain.dao.ChatLogRepo;
import io.github.fzdwx.logic.domain.entity.ChatHistory;
import io.github.fzdwx.logic.msg.domain.resp.ChatMessageResp;
import io.github.fzdwx.logic.msg.ws.UserWsConn;
import io.github.fzdwx.logic.msg.ws.WsPacket;
import io.github.fzdwx.logic.msg.ws.packet.ChatMessagePacket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/8 17:19
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessagePacketHandler implements WsPacket.Handler<ChatMessagePacket> {

    private final ChatLogRepo chatLogDao;

    @Override
    public void handle(final ChatMessagePacket packet) {
        final Err err = packet.prepare();
        if (err != null) {
            packet.sendError(err);
            return;
        }

        packet.setMsgFrom(ChatConst.MsgFrom.USER);

        //region save chat log to mysql
        final var userInfo = userInfo(packet);
        final var chatLog = packet.buildChatLog(userInfo);

        // todo save 和 saveBatch 相差10ms 左右
        // 原因： saveBatch 默认加了事务?会导致变快
        chatLogDao.insert(chatLog);
        // final var flag = chatLogDao.saveBatch(Coll.list(chatLog));
        // if (!flag) {
        //     packet.sendError("保存失败");
        //     return;
        // } else
            packet.sendSuccess();
        //endregion

        //region switch chat type and send to user.
        final var resp = ChatMessageResp.from(userInfo, packet, chatLog);
        switch (packet.getSessionType()) {
            case ChatConst.SessionType.broadcast -> sendAll(packet, resp);
            case ChatConst.SessionType.group -> sendGroup(packet, resp);
            case ChatConst.SessionType.personal -> sendPersonal(packet, resp);
            default -> packet.sendError("未知的会话类型:" + packet.getSessionType());
        }
        //endregion
    }

    private void sendPersonal(final ChatMessagePacket packet, final ChatMessageResp resp) {
        final var conn = UserWsConn.get(packet);
        if (conn == null) {
            // OfflineMessageManager.push(resp, resp.getToId(), resp.getFromId());
            return;
        }

        final var data = packet.newSuccessPacket(resp.fixUrl()).encode();
        conn.send(data);
    }

    private void sendGroup(final ChatMessagePacket packet, final ChatMessageResp resp) {
        final var chatMessages = packet.buildChatLog(userInfo(packet));
        // TODO: 2022/4/17 群聊
        chatLogDao.saveBatch(Seq.of(chatMessages).typeOf(ChatHistory.class).toList());
    }

    private void sendAll(final ChatMessagePacket packet, final ChatMessageResp resp) {
        final var data = packet.newSuccessPacket(resp).encode();
        UserWsConn.foreach((id, ws) -> {
            if (id.equals(resp.getFromId())) {
                return;
            }
            ws.send(data);
        });
    }
}