package org.atomicoke.logic.msg.ws.handler;

import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.err.Err;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.config.ProjectProps;
import org.atomicoke.logic.modules.friend.service.FriendService;
import org.atomicoke.logic.modules.group.service.GroupService;
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

    private final FriendService friendService;
    private final GroupService groupService;
    private static Duration randomIdExpire;

    public SysContactPacketHandler(final FriendService friendService,
                                   final GroupService groupService,
                                   final ProjectProps projectProps) {
        this.friendService = friendService;
        this.groupService = groupService;
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
        final var userInfo = packet.userInfo();
        boolean success = false;
        if (ChatConst.ContactType.isFriendOperator(packet.getContactType())) {
            try {
                success = friendService.saveFriendRequest(packet, userInfo.getIdLong());
            } catch (Exception e) {
                log.error("save friend request error packet:{} requestUser:{}", packet, userInfo, e);
            }
        } else if (ChatConst.ContactType.isGroupOperator(packet.getContactType())) {
            try {
                success = groupService.saveGroupRequest(packet, userInfo.getIdLong());
            } catch (Exception e) {
                log.error("save group request error packet:{} requestUser:{}", packet, userInfo, e);
            }
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
            return groupService.getGroupManager(toId);
        }
        throw Err.unsupported();
    }

    @Override
    public Duration getExpire() {
        return randomIdExpire;
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