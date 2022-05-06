package org.atomicoke.logic.modules.group.service;

import socket.WebSocket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.Assert;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.util.Json;
import org.atomicoke.inf.common.util.Time;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.modules.friend.domain.model.FriendApplyReq;
import org.atomicoke.logic.modules.friend.domain.model.FriendHandleReq;
import org.atomicoke.logic.modules.group.domain.dao.GroupChatApplyRepo;
import org.atomicoke.logic.modules.group.domain.dao.GroupChatMemberRepo;
import org.atomicoke.logic.modules.group.domain.dao.GroupChatRepo;
import org.atomicoke.logic.modules.group.domain.entity.GroupChatApply;
import org.atomicoke.logic.modules.group.domain.entity.GroupChatMember;
import org.atomicoke.logic.modules.group.domain.model.GroupApplyReq;
import org.atomicoke.logic.modules.group.domain.model.GroupHandleReq;
import org.atomicoke.logic.modules.group.domain.model.GroupInviteReq;
import org.atomicoke.logic.modules.msg.UserWsConn;
import org.atomicoke.logic.modules.msg.WsPacket;
import org.atomicoke.logic.modules.msg.domain.model.Message;
import org.atomicoke.logic.modules.msg.domain.resp.ContactMessageResp;
import org.atomicoke.logic.modules.msg.sync.MessageSyncer;
import org.atomicoke.logic.modules.user.domain.dao.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author oneIdler
 * @since 2022/4/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupChatRepo groupChatDao;
    private final GroupChatApplyRepo groupChatApplyDao;
    private final GroupChatMemberRepo groupChatMemberDao;
    private final UserRepo userDao;

    /**
     * 入群申请
     *
     * @param userInfo {@link UserInfo}
     * @param req      {@link FriendApplyReq}
     */
    @Transactional(rollbackFor = Exception.class)
    public void apply(UserInfo userInfo, GroupApplyReq req) {
        List<Long> toIdList = this.groupChatMemberDao.getGroupManager(req.getGroupId());
        Assert.notEmpty(toIdList, "不合法的群！");
        GroupChatApply apply = req.ofEntity(userInfo.getIdLong());
        boolean flag = groupChatApplyDao.saveIgnore(apply);
        if (flag) {
            List<String> messages = toIdList.stream()
                    .map(toUserId -> {
                        ContactMessageResp resp = req.ofResp(apply.getId(), toUserId, userInfo);
                        Message message = resp.toMessage(toUserId, MessageSyncer.incrSeq(toUserId));
                        this.push(req.getGroupId(), message);
                        return Json.toJson(message);
                    }).collect(Collectors.toList());
            MessageSyncer.saveToMongo(messages);
        }
    }

    public void invite(UserInfo userInfo, GroupInviteReq req) {
        Assert.ensureTrue(groupChatDao.exist(req.getGroupId()), "不合法的群！");
        GroupChatApply apply = req.ofEntity(userInfo.getIdLong());
        boolean flag = groupChatApplyDao.saveIgnore(apply);
        if (flag) {
            ContactMessageResp resp = req.ofResp(apply.getId(), req.getToId(), userInfo);
            Message message = resp.toMessage(req.getToId(), MessageSyncer.incrSeq(req.getToId()));
            this.push(req.getToId(), message);
            MessageSyncer.saveToMongo(message);
        }
    }

    /**
     * 申请处理
     *
     * @param userInfo {@link UserInfo}
     * @param req      {@link FriendHandleReq}
     */
    @Transactional(rollbackFor = Exception.class)
    public void handle(UserInfo userInfo, GroupHandleReq req) {
        boolean flag = groupChatApplyDao.updateResult(req.getApplyId(), userInfo.getIdLong(), req.getHandlerResult());
        if (!flag) {
            return;
        }
        GroupChatApply apply = groupChatApplyDao.getApplyById(req.getApplyId());
        // 原申请人id
        Long applyUserId = apply.getApplyUserId();
        Long memberId = apply.getUserId();
        GroupChatMember member = new GroupChatMember();
        member.setGroupId(apply.getGroupId());
        member.setUserId(memberId);
        member.setNickName(userDao.getNickName(memberId));
        member.setRoleType(1);
        member.setAddTime(Time.now());
        member.setAddWay(0);
        groupChatMemberDao.save(member);
        ContactMessageResp resp = req.ofResp(req.getApplyId(), applyUserId, userInfo, apply.getType());
        Message message = resp.toMessage(applyUserId, MessageSyncer.incrSeq(applyUserId));
        //推送消息
        this.push(applyUserId, message);
        MessageSyncer.saveToMongo(message);
    }

    /**
     * 推送消息
     *
     * @param userId  目标用户id
     * @param message 消息
     */
    private void push(Long userId, Message message) {
        WebSocket conn = UserWsConn.get(userId);
        if (conn == null) {
            // TODO: 2022/4/23 离线推送
            log.warn("用户[{}]没有连接", userId);
        } else {
            conn.send(WsPacket.newNotifyPacket(message, ChatConst.Notify.contact).encode());
        }
    }
}