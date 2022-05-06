package org.atomicoke.logic.modules.group.service;

import io.github.fzdwx.inf.socket.WebSocket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.util.Json;
import org.atomicoke.inf.common.util.Time;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.modules.friend.domain.model.FriendApplyReq;
import org.atomicoke.logic.modules.friend.domain.model.FriendHandleReq;
import org.atomicoke.logic.modules.group.domain.dao.GroupChatMemberRepo;
import org.atomicoke.logic.modules.group.domain.dao.GroupChatRequestRepo;
import org.atomicoke.logic.modules.group.domain.entity.GroupChatMember;
import org.atomicoke.logic.modules.group.domain.entity.GroupChatRequest;
import org.atomicoke.logic.modules.group.domain.model.GroupApplyReq;
import org.atomicoke.logic.modules.group.domain.model.GroupHandleReq;
import org.atomicoke.logic.modules.msg.UserWsConn;
import org.atomicoke.logic.modules.msg.WsPacket;
import org.atomicoke.logic.modules.msg.domain.model.Message;
import org.atomicoke.logic.modules.msg.domain.resp.ContactMessageResp;
import org.atomicoke.logic.modules.msg.sync.MessageSyncer;
import org.atomicoke.logic.modules.user.domain.dao.UserRepo;
import org.atomicoke.logic.modules.user.domain.entity.User;
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

    private final GroupChatRequestRepo groupChatRequestDao;
    private final GroupChatMemberRepo groupChatMemberDao;
    private final UserRepo userDao;

    /**
     * 好友申请
     *
     * @param userInfo {@link UserInfo}
     * @param req      {@link FriendApplyReq}
     */
    @Transactional(rollbackFor = Exception.class)
    public void apply(UserInfo userInfo, GroupApplyReq req) {
        GroupChatRequest request = req.ofEntity(userInfo.getIdLong());
        boolean flag = groupChatRequestDao.saveIgnore(request);
        if (flag) {
            List<Long> toIdList = this.groupChatMemberDao.getGroupManager(req.getToId());
            List<String> messages = toIdList.stream()
                    .map(toUserId -> {
                        ContactMessageResp resp = req.ofResp(request.getId(), toUserId, userInfo);
                        Message message = resp.toMessage(toUserId, MessageSyncer.incrSeq(toUserId));
                        WebSocket conn = UserWsConn.get(req.getToId());
                        if (conn == null) {
                            // TODO: 2022/4/23 离线推送
                            log.warn("用户[{}]没有连接", req.getToId());
                        } else {
                            conn.send(WsPacket.newNotifyPacket(message, ChatConst.Notify.contact).encode());
                        }
                        return Json.toJson(message);
                    }).collect(Collectors.toList());
            MessageSyncer.saveToMongo(messages);
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
        boolean flag = groupChatRequestDao.updateResult(req.getRequestId(), userInfo.getIdLong(), req.getHandlerResult());
        if (!flag) {
            return;
        }
        GroupChatRequest request = groupChatRequestDao.getApplyIdAndGroupId(req.getRequestId());
        Long applyId = request.getApplyId();
        User user = userDao.findOne(applyId);
        GroupChatMember member = new GroupChatMember();
        member.setGroupId(request.getGroupId());
        member.setUserId(applyId);
        member.setNickName(user.getNickName());
        member.setRoleType(1);
        member.setAddTime(Time.now());
        member.setAddWay(0);
        groupChatMemberDao.save(member);
        ContactMessageResp resp = req.ofResp(req.getRequestId(), applyId, userInfo);
        Message message = resp.toMessage(applyId, MessageSyncer.incrSeq(applyId));
        WebSocket conn = UserWsConn.get(applyId);
        if (conn == null) {
            // TODO: 2022/4/23 离线推送
            log.warn("用户[{}]没有连接", applyId);
        } else {
            conn.send(WsPacket.newNotifyPacket(message, ChatConst.Notify.contact).encode());
        }
        MessageSyncer.saveToMongo(message);
    }
}