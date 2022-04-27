package org.atomicoke.logic.msg.ws.handler;

import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.err.Err;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.config.ProjectProps;
import org.atomicoke.logic.modules.friend.domain.dao.FriendRequestRepo;
import org.atomicoke.logic.modules.friend.domain.entity.FriendRequest;
import org.atomicoke.logic.modules.group.domain.dao.GroupChatMemberRepo;
import org.atomicoke.logic.modules.group.domain.dao.GroupChatRequestRepo;
import org.atomicoke.logic.modules.group.domain.entity.GroupChatRequest;
import org.atomicoke.logic.msg.domain.resp.SysInfoResp;
import org.atomicoke.logic.msg.sync.MessageSyncer;
import org.atomicoke.logic.msg.ws.UserWsConn;
import org.atomicoke.logic.msg.ws.WsPacket;
import org.atomicoke.logic.msg.ws.packet.sys.SysContactPacket;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/8 17:19
 */
@Slf4j
@Component
public class SysContactPacketHandler implements WsPacket.Handler<SysContactPacket> {

    private final FriendRequestRepo friendRequestDao;
    private final GroupChatRequestRepo groupChatRequestDao;
    private final GroupChatMemberRepo groupChatMemberDao;
    private static Duration randomIdExpire;

    public SysContactPacketHandler(final FriendRequestRepo friendRequestDao, final GroupChatRequestRepo groupChatRequestDao,
                                   final GroupChatMemberRepo groupChatMemberDao, final ProjectProps projectProps) {
        this.friendRequestDao = friendRequestDao;
        this.groupChatRequestDao = groupChatRequestDao;
        this.groupChatMemberDao = groupChatMemberDao;
        randomIdExpire = Duration.ofSeconds(projectProps.getRandomIdToChatHistoryIdExpire());
    }

    @Override
    public void handle(final SysContactPacket packet) {
        Err err = packet.prepare();
        if (err != null) {
            packet.sendError(err);
            return;
        }
        String randomId = packet.getRandomId();
        //region save chat log to mysql (根据randomId保证幂等)
        packet.setMsgFrom(ChatConst.MsgFrom.SYS);
        final var userInfo = userInfo(packet);
        boolean success = false;
        if (ChatConst.ContactType.isFriendOperator(packet.getContactType())) {
            success = updateFriend(packet, userInfo.getIdLong());
        } else if (ChatConst.ContactType.isGroupOperator(packet.getContactType())) {
            success = updateGroup(packet, userInfo.getIdLong());
        }
        final Long requestId;
        if (success) {
            requestId = packet.getRequestId();
        } else {
            requestId = this.getMessageId(randomId);
            if (requestId == null) {
                packet.sendError("这条消息的缓存id已经过期,或randomId错误");
                return;
            }
        }

        sendSuccessRespToClient(packet, requestId);

        if (success) {
            sendSysInfo(packet, userInfo);
        }
    }

    /**
     * switch chat type and send to user.
     */
    private void sendSysInfo(final SysContactPacket packet, final UserInfo userInfo) {
        List<Long> toIdList = this.fetchToIdList(packet.getContactType(), packet.getToId());
//        packet.replay(new ReplayPacket.Data(String.valueOf(packet.getRequestId()), userInfo.getId(), null));
        var respList = toIdList.stream()
                .map(toUserId -> send(packet, toUserId, userInfo))
                .collect(Collectors.toList());
        MessageSyncer.saveSysInfoToMongo(respList);

    }

    @NotNull
    private SysInfoResp send(SysContactPacket packet, Long toUserId, final UserInfo userInfo) {
        Long seq = MessageSyncer.incrSysInfoSeq(String.valueOf(toUserId));
        SysInfoResp resp = SysInfoResp.from(toUserId, seq, packet, packet.getRequestId(), toUserId, userInfo);
        final var conn = UserWsConn.get(toUserId);
        if (conn == null) {
            // TODO: 2022/4/23 离线推送
            log.warn("用户[{}]没有连接", toUserId);
        } else {
            conn.send(packet.newSuccessPacket(resp).encode());
        }
        return resp;
    }

    private List<Long> fetchToIdList(int contactType, Long toId) {
        if (ChatConst.ContactType.isFriendOperator(contactType)) {
            return Collections.singletonList(toId);
        } else if (ChatConst.ContactType.isGroupOperator(contactType)) {
            return groupChatMemberDao.getGroupManager(toId);
        }
        throw Err.unsupported();
    }

    @Override
    public Duration getExpire() {
        return randomIdExpire;
    }

    private boolean updateGroup(SysContactPacket packet, Long userId) {
        GroupChatRequest groupChatRequest = SysContactPacket.buildGroupRequest(packet, userId);
        switch (packet.getContactType()) {
            case ChatConst.ContactType.joinGroup -> {
                boolean flag = groupChatRequestDao.saveIgnore(groupChatRequest);
                packet.setRequestId(groupChatRequest.getId());
                return flag;
            }
            case ChatConst.ContactType.agreeGroup -> {
                return groupChatRequestDao.updateResult(packet.getRequestId(), userId, packet.getSendTime(), ChatConst.FriendAndGroupApplyResult.agree);
            }
            case ChatConst.ContactType.rejectGroup -> {
                return groupChatRequestDao.updateResult(packet.getRequestId(), userId, packet.getSendTime(), ChatConst.FriendAndGroupApplyResult.reject);
            }
            default -> {
                packet.sendError("未知的消息类型:" + packet.getContactType());
                throw Err.verify("未知的系统消息类型" + packet.getContactType());
            }
        }
    }

    private boolean updateFriend(SysContactPacket packet, Long userId) {
        FriendRequest friendRequest = SysContactPacket.buildFriendRequest(packet, userId);
        switch (packet.getContactType()) {
            case ChatConst.ContactType.addFriend -> {
                boolean flag = friendRequestDao.saveIgnore(friendRequest);
                packet.setRequestId(friendRequest.getId());
                return flag;
            }
            case ChatConst.ContactType.agreeFriend -> {
                return friendRequestDao.updateResult(packet.getRequestId(), packet.getSendTime(), ChatConst.FriendAndGroupApplyResult.agree);
            }
            case ChatConst.ContactType.rejectFriend -> {
                return friendRequestDao.updateResult(packet.getRequestId(), packet.getSendTime(), ChatConst.FriendAndGroupApplyResult.reject);
            }
            default -> {
                packet.sendError("未知的系统消息类型:" + packet.getContactType());
                throw Err.verify("未知的系统消息类型" + packet.getContactType());
            }
        }

    }


    /**
     * 保存数据到mysql后,返回成功响应给客户端
     */
    private void sendSuccessRespToClient(final SysContactPacket packet, final Long requestId) {
        packet.sendSuccess().addListener(f -> {
            cacheRandomIdToChatHistoryId(packet.getRandomId(), requestId);
            if (!f.isSuccess()) {
                log.error("发送成功响应失败: randomId:{},requestId:{}", packet.getRandomId(), requestId, f.cause());
            }
        });
    }


}