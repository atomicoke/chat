package io.github.fzdwx.logic.msg.ws.handler;

import io.github.fzdwx.inf.common.contants.ChatConst;
import io.github.fzdwx.inf.common.err.Err;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import io.github.fzdwx.inf.middleware.id.IdGenerate;
import io.github.fzdwx.inf.middleware.redis.Redis;
import io.github.fzdwx.lambada.Lang;
import io.github.fzdwx.lambada.Seq;
import io.github.fzdwx.logic.domain.dao.ChatHistoryRepo;
import io.github.fzdwx.logic.domain.entity.ChatHistory;
import io.github.fzdwx.logic.msg.domain.resp.ChatMessageResp;
import io.github.fzdwx.logic.msg.ws.UserWsConn;
import io.github.fzdwx.logic.msg.ws.WsPacket;
import io.github.fzdwx.logic.msg.ws.packet.ChatMessagePacket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/8 17:19
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessagePacketHandler implements WsPacket.Handler<ChatMessagePacket> {

    private final ChatHistoryRepo chatHistoryDao;

    @Override
    public void handle(final ChatMessagePacket packet) {
        final Err err = packet.prepare();
        if (err != null) {
            packet.sendError(err);
            return;
        }

        packet.setMsgFrom(ChatConst.MsgFrom.USER);

        //region save chat log to mysql (根据randomId保证幂等)
        final var userInfo = userInfo(packet);
        final var chatHistory = packet.buildChatHistory(userInfo);

        /*
          phenomenon: save 和 saveBatch 相差10ms 左右
          possible:   saveBatch 默认加了事务?会导致变快
         */
        final var flag = chatHistoryDao.saveIgnore(chatHistory.setId(IdGenerate.nextId()));

        var chatHistoryId = chatHistory.getId();
        if (Lang.eq(flag, 0)) { // 该randomId已经存在
            chatHistoryId = getChatHistoryIdByRandomId(chatHistory.getRandomId());
            if (chatHistoryId == null) {
                packet.sendError("这条消息的缓存id已经过期,或randomId错误");
                return;
            }
        }
        // endregion

        saveSuccess(packet, chatHistory, chatHistoryId);

        this.switchHandler(packet, userInfo, chatHistory);
    }

    /**
     * switch chat type and send to user.
     */
    private void switchHandler(final ChatMessagePacket packet, final UserInfo userInfo, final ChatHistory chatHistory) {
        final var resp = ChatMessageResp.from(userInfo, packet, chatHistory);
        switch (packet.getSessionType()) {
            case ChatConst.SessionType.broadcast -> sendAll(packet, resp);
            case ChatConst.SessionType.group -> sendGroup(packet, resp);
            case ChatConst.SessionType.personal -> sendPersonal(packet, resp);
            default -> packet.sendError("未知的会话类型:" + packet.getSessionType());
        }
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
        final var chatMessages = packet.buildChatHistory(userInfo(packet));
        // TODO: 2022/4/17 群聊
        chatHistoryDao.saveBatch(Seq.of(chatMessages).typeOf(ChatHistory.class).toList());
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

    /**
     * 保存数据到mysql后,返回成功响应给客户端
     */
    private static void saveSuccess(final ChatMessagePacket packet, final ChatHistory chatHistory, final Long chatHistoryId) {
        packet.sendSuccess(chatHistoryId).addListener(f -> {
            if (f.isSuccess()) {
                setRandomIdToChatHistoryId(chatHistory.getRandomId(), chatHistoryId);
            }
        });
    }

    /**
     * 在redis 中缓存 randomId对应的chatHistoryId,过期时间为30s
     */
    private static void setRandomIdToChatHistoryId(String randomId, Long chatHistoryId) {
        if (Lang.isEmpty(randomId) || Lang.isNull(chatHistoryId)) {
            return;
        }

        Redis.set(randomId, chatHistoryId.toString(), Duration.ofSeconds(30));
    }

    /**
     * 获取在redis 中缓存的randomId对应的chatHistoryId
     *
     * @apiNote 可能返回null, 当返回null时, 说明没有这条消息或者消息已经过期
     */
    @Nullable
    private static Long getChatHistoryIdByRandomId(String randomId) {
        if (randomId == null || randomId.isEmpty()) {
            return null;
        }

        final String s = Redis.get(randomId);
        if (s == null) {
            return null;
        }

        return Long.parseLong(s);
    }
}