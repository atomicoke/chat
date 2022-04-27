package org.atomicoke.logic.msg.ws.packet.sys;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.err.Err;
import org.atomicoke.logic.modules.friend.domain.entity.FriendRequest;
import org.atomicoke.logic.modules.group.domain.entity.GroupChatRequest;
import org.atomicoke.logic.msg.ws.WsPacket;

import java.time.LocalDateTime;

/**
 * @author oneIdler
 * @since 2022/4/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class SysContactPacket extends WsPacket {

    private static final String type = Type.sysContact;

    /**
     * 添加好友或加群的 请求id
     */
    private Long requestId;

    /**
     * 接收者 被添加人id或者群id
     */
    private Long toId;

    /**
     * 发起申请携带的申请信息
     */
    private String requestMessage;


    /**
     * 消息发送者类型 默认为系统消息
     *
     * @see ChatConst.MsgFrom
     */
    private int msgFrom = ChatConst.MsgFrom.SYS;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * @see ChatConst.ContactType
     */
    private int contactType;


    @Override
    public String type() {
        return type;
    }

    public Err prepare() {
        if (this.randomId == null) {
            return Err.verify("randomId is null");
        }

        if (this.contactType == 0) {
            return Err.verify("contactType can not be null");
        }

        if (this.toId ==null) {
            throw Err.verify("toId must not be null");
        }

        if (this.sendTime == null) {
            this.sendTime = LocalDateTime.now();
        }
        return null;
    }

    public static FriendRequest buildFriendRequest(SysContactPacket packet, Long reqId) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setId(packet.getRequestId());
        friendRequest.setReqMessage(packet.getRequestMessage());
        friendRequest.setHandlerResult(ChatConst.FriendAndGroupApplyResult.unOperated);
        friendRequest.setCreateTime(packet.getSendTime());
        friendRequest.setReqId(reqId);
        friendRequest.setUserId(packet.getToId());
        return friendRequest;
    }

    public static GroupChatRequest buildGroupRequest(SysContactPacket packet, Long groupId) {
        GroupChatRequest groupChatRequest = new GroupChatRequest();
        groupChatRequest.setId(packet.getRequestId());
        groupChatRequest.setReqMessage(packet.getRequestMessage());
        groupChatRequest.setHandlerResult(ChatConst.FriendAndGroupApplyResult.unOperated);
        groupChatRequest.setCreateTime(packet.getSendTime());
        groupChatRequest.setReqId(packet.getToId());
        groupChatRequest.setGroupId(groupId);
        return groupChatRequest;
    }
}