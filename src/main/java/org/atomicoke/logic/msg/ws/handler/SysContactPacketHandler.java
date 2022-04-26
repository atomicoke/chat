package org.atomicoke.logic.msg.ws.handler;

import io.github.fzdwx.lambada.Lang;
import io.github.fzdwx.lambada.Seq;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.err.Err;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.inf.middleware.redis.Redis;
import org.atomicoke.logic.config.ProjectProps;
import org.atomicoke.logic.modules.chathistory.domain.entity.ChatHistory;
import org.atomicoke.logic.modules.friend.domain.dao.FriendRequestRepo;
import org.atomicoke.logic.modules.friend.domain.entity.FriendRequest;
import org.atomicoke.logic.modules.group.domain.dao.GroupChatRequestRepo;
import org.atomicoke.logic.msg.domain.resp.ChatMessageResp;
import org.atomicoke.logic.msg.sync.MessageSyncer;
import org.atomicoke.logic.msg.ws.UserWsConn;
import org.atomicoke.logic.msg.ws.WsPacket;
import org.atomicoke.logic.msg.ws.packet.chat.ChatMessagePacket;
import org.atomicoke.logic.msg.ws.packet.sys.SysContactPacket;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/8 17:19
 */
@Slf4j
@Component
public class SysContactPacketHandler implements WsPacket.Handler<SysContactPacket> {

    private final FriendRequestRepo friendRequestDao;
    private final GroupChatRequestRepo groupChatRequestDao;
    private static Duration randomIdToChatHistoryIdExpire;

    public SysContactPacketHandler(final FriendRequestRepo friendRequestDao, final GroupChatRequestRepo groupChatRequestDao, final ProjectProps projectProps) {
        this.friendRequestDao = friendRequestDao;
        this.groupChatRequestDao = groupChatRequestDao;
        randomIdToChatHistoryIdExpire = Duration.ofSeconds(projectProps.getRandomIdToChatHistoryIdExpire());
    }

    @Override
    public void handle(final SysContactPacket packet) {
        Err err = packet.prepare();
        if (err != null) {
            packet.sendError(err);
            return;
        }

        //region save chat log to mysql (根据randomId保证幂等)
        packet.setMsgFrom(ChatConst.MsgFrom.SYS);
        final var userInfo = userInfo(packet);
        FriendRequest friendRequest = this.fetchEntity(packet, userInfo.getIdLong());
        boolean success = false;
        switch (packet.getContactType()) {
            case ChatConst.ContactType.addFriend -> success = friendRequestDao.saveIgnore(friendRequest);
            case ChatConst.ContactType.agreeFriend ->
                    success = friendRequestDao.updateResult(packet.getRequestId(), packet.getSendTime(), ChatConst.FriendAndGroupApplyResult.agree);
            case ChatConst.ContactType.rejectFriend ->
                    success = friendRequestDao.updateResult(packet.getRequestId(), packet.getSendTime(), ChatConst.FriendAndGroupApplyResult.reject);
            default -> packet.sendError("未知的消息类型:" + packet.getContactType());
        }

    }

    @NotNull
    private FriendRequest fetchEntity(SysContactPacket packet, Long reqId) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setId(packet.getRequestId());
        friendRequest.setReqMessage(packet.getRequestMessage());
        friendRequest.setHandlerResult(1);
        friendRequest.setCreateTime(packet.getSendTime());
        friendRequest.setReqId(reqId);
        friendRequest.setUserId(packet.getToIdList().get(0));
        return friendRequest;
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