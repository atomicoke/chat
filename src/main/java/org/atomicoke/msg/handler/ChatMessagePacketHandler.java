package org.atomicoke.msg.handler;

import io.github.fzdwx.lambada.Lang;
import io.github.fzdwx.lambada.Seq;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.err.Err;
import org.atomicoke.inf.common.util.Json;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.inf.middleware.id.IdGenerate;
import org.atomicoke.logic.config.ProjectProps;
import org.atomicoke.logic.modules.chathistory.domain.dao.ChatHistoryRepo;
import org.atomicoke.logic.modules.chathistory.domain.entity.ChatHistory;
import org.atomicoke.msg.domain.model.Message;
import org.atomicoke.msg.UserWsConn;
import org.atomicoke.msg.packet.chat.ChatMessagePacket;
import org.atomicoke.msg.domain.resp.ChatMessageResp;
import org.atomicoke.msg.sync.MessageSyncer;
import org.atomicoke.msg.WsPacket;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/8 17:19
 */
@Slf4j
@Component
public class ChatMessagePacketHandler implements WsPacket.Handler<ChatMessagePacket> {

    private final ChatHistoryRepo chatHistoryDao;
    private static Duration randomIdToChatHistoryIdExpire;

    public ChatMessagePacketHandler(final ChatHistoryRepo chatHistoryDao, final ProjectProps projectProps) {
        this.chatHistoryDao = chatHistoryDao;
        randomIdToChatHistoryIdExpire = Duration.ofSeconds(projectProps.getRandomIdToChatHistoryIdExpire());
    }

    @Override
    public void handle(final ChatMessagePacket packet) {
        final Err err = packet.prepare();
        if (err != null) {
            packet.sendError(err);
            return;
        }

        //region save chat log to mysql (根据randomId保证幂等)
        packet.setMsgFrom(ChatConst.MsgFrom.USER);
        final var userInfo = packet.userInfo();
        final var chatHistory = packet.buildChatHistory(userInfo);

        /*
          phenomenon: save 和 saveBatch 相差10ms 左右
          possible:   saveBatch 默认加了事务?会导致变快
         */
        final var cnt = chatHistoryDao.saveIgnore(chatHistory.setId(IdGenerate.nextId()));

        final Long chatHistoryId;
        final var flag = Lang.eq(cnt, 0);
        if (flag) { // 该randomId已经存在
            chatHistoryId = this.getMessageId(chatHistory.getRandomId());
            if (chatHistoryId == null) {
                packet.sendError("这条消息的缓存id已经过期,或randomId错误");
                return;
            }
        } else {
            chatHistoryId = chatHistory.getId();
        }
        // endregion

        // ack to client
        sendSuccessRespToClient(packet, chatHistoryId);

        if (!flag) {
            switchHandler(packet, userInfo, chatHistory);
        }
    }

    @Override
    public Duration getExpire() {
        return randomIdToChatHistoryIdExpire;
    }

    /**
     * switch chat type and send to user.
     */
    private void switchHandler(final ChatMessagePacket packet, final UserInfo userInfo, final ChatHistory chatHistory) {
        final var resp = ChatMessageResp.from(userInfo, packet, chatHistory);
        switch (packet.getSessionType()) {
            case ChatConst.SessionType.broadcast -> sendAll(packet, resp);
            case ChatConst.SessionType.group -> {
                // 返回给发送人的响应
                packet.replay(MessageSyncer.incrSeqAndSaveToMongo(chatHistory.getFromId(), resp));
                sendGroup(packet, resp);
            }
            case ChatConst.SessionType.single -> {
                // 返回给发送人的响应
                packet.replay(MessageSyncer.incrSeqAndSaveToMongo(chatHistory.getFromId(), resp));
                MessageSyncer.saveToMongo(sendPersonal(packet, resp, packet.getToId()));
            }
            default -> packet.sendError("未知的会话类型:" + packet.getSessionType());
        }
    }

    private Message sendPersonal(final ChatMessagePacket packet, final ChatMessageResp resp, Long toUserId) {
        Message message = resp.toMessage(toUserId, MessageSyncer.incrSeq(toUserId));
        final var conn = UserWsConn.get(toUserId);
        if (conn == null) {
            // TODO: 2022/4/23 离线推送
            log.warn("用户[{}]没有连接", toUserId);
        } else {
            conn.send(packet.newSuccessPacket(message).encode());
        }

        return message;
    }

    private void sendGroup(final ChatMessagePacket packet, final ChatMessageResp resp) {
        // todo toIdList 要不要前端传入
        final var messages = Seq.of(packet.getToIdList())
                .map(toUserId -> Json.toJson(sendPersonal(packet, resp, toUserId)))
                .toList();

        MessageSyncer.saveToMongo(messages);
    }

    private void sendAll(final ChatMessagePacket packet, final ChatMessageResp resp) {
        final var data = packet.newSuccessPacket(resp).encode();

        final var fromIdLong = Long.parseLong(resp.getFromId());
        UserWsConn.foreach((id, ws) -> {
            if (id.equals(fromIdLong)) {
                return;
            }
            ws.send(data);
        });
    }

    /**
     * 保存数据到mysql后,返回成功响应给客户端
     */
    private void sendSuccessRespToClient(final ChatMessagePacket packet, final Long chatHistoryId) {
        packet.sendSuccess().addListener(f -> {
            cacheRandomIdToChatHistoryId(packet.getRandomId(), chatHistoryId);
            if (!f.isSuccess()) {
                log.error("发送成功响应失败: randomId:{},chatHistoryId:{}", packet.getRandomId(), chatHistoryId, f.cause());
            }
        });
    }
}