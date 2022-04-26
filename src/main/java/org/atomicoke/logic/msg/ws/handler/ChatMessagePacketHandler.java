package org.atomicoke.logic.msg.ws.handler;

import io.github.fzdwx.lambada.Lang;
import io.github.fzdwx.lambada.Seq;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.err.Err;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.inf.middleware.id.IdGenerate;
import org.atomicoke.inf.middleware.redis.Redis;
import org.atomicoke.logic.config.ProjectProps;
import org.atomicoke.logic.modules.chathistory.domain.dao.ChatHistoryRepo;
import org.atomicoke.logic.modules.chathistory.domain.entity.ChatHistory;
import org.atomicoke.logic.msg.domain.resp.ChatMessageResp;
import org.atomicoke.logic.msg.sync.MessageSyncer;
import org.atomicoke.logic.msg.ws.UserWsConn;
import org.atomicoke.logic.msg.ws.WsPacket;
import org.atomicoke.logic.msg.ws.packet.chat.ChatMessagePacket;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
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
        final var userInfo = userInfo(packet);
        final var chatHistory = packet.buildChatHistory(userInfo);

        /*
          phenomenon: save 和 saveBatch 相差10ms 左右
          possible:   saveBatch 默认加了事务?会导致变快
         */
        final var cnt = chatHistoryDao.saveIgnore(chatHistory.setId(IdGenerate.nextId()));

        final Long chatHistoryId;
        final var flag = Lang.eq(cnt, 0);
        if (flag) { // 该randomId已经存在
            chatHistoryId = getChatHistoryIdByRandomId(chatHistory.getRandomId());
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

    private ChatMessageResp sendPersonal(final ChatMessagePacket packet, final ChatMessageResp resp, Long toUserId) {
        final var copyResp = resp.copy(toUserId, MessageSyncer.incrSeq(toUserId.toString()));

        final var conn = UserWsConn.get(toUserId);
        if (conn == null) {
            // TODO: 2022/4/23 离线推送
            log.warn("用户[{}]没有连接", toUserId);
        } else {
            conn.send(packet.newSuccessPacket(copyResp.fixUrl()).encode());
        }

        return copyResp;
    }

    private void sendGroup(final ChatMessagePacket packet, final ChatMessageResp resp) {
        final var chatMessageResps = Seq.of(packet.getToIdList())
                .map(toUserId -> sendPersonal(packet, resp, toUserId))
                .toList();

        MessageSyncer.saveToMongo(chatMessageResps);
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
    private static void sendSuccessRespToClient(final ChatMessagePacket packet, final Long chatHistoryId) {
        packet.sendSuccess().addListener(f -> {
            if (f.isSuccess()) {
                cacheRandomIdToChatHistoryId(packet.getRandomId(), chatHistoryId);
            } else {
                log.error("发送成功响应失败: randomId:{},chatHistoryId:{}", packet.getRandomId(), chatHistoryId, f.cause());
                // saveSuccess(packet, chatHistoryId);
            }
        });
    }

    /**
     * 在redis 中缓存 randomId对应的chatHistoryId,过期时间为30s
     */
    private static void cacheRandomIdToChatHistoryId(String randomId, Long chatHistoryId) {
        if (Lang.isEmpty(randomId) || Lang.isNull(chatHistoryId)) {
            return;
        }

        Redis.set(getKey(randomId), chatHistoryId.toString(), randomIdToChatHistoryIdExpire);
    }

    private static String getKey(final String randomId) {
        return RANDOM_ID_MAP_CHAT_HISTORY_ID + randomId;
    }

    private static final String RANDOM_ID_MAP_CHAT_HISTORY_ID = "msg:map:";

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

        final String s = Redis.get(getKey(randomId));
        if (s == null) {
            return null;
        }

        return Long.parseLong(s);
    }
}