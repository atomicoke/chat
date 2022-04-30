package org.atomicoke.logic.modules.group.service;

import io.github.fzdwx.inf.msg.WebSocket;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.util.Json;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.modules.friend.domain.model.FriendApplyReq;
import org.atomicoke.logic.modules.friend.domain.model.FriendHandleReq;
import org.atomicoke.logic.modules.group.domain.dao.GroupChatMemberRepo;
import org.atomicoke.logic.modules.group.domain.dao.GroupChatRequestRepo;
import org.atomicoke.logic.modules.group.domain.entity.GroupChatMember;
import org.atomicoke.logic.modules.group.domain.entity.GroupChatRequest;
import org.atomicoke.logic.modules.group.domain.model.GroupApplyReq;
import org.atomicoke.logic.modules.group.domain.model.GroupHandleReq;
import org.atomicoke.logic.modules.user.domain.dao.UserRepo;
import org.atomicoke.logic.modules.user.domain.entity.User;
import org.atomicoke.logic.msg.domain.resp.ContactMessageResp;
import org.atomicoke.logic.msg.sync.MessageSyncer;
import org.atomicoke.logic.msg.ws.UserWsConn;
import org.atomicoke.logic.msg.ws.WsPacket;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author oneIdler
 * @since 2022/4/28
 */
@Slf4j
@Service
@AllArgsConstructor
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
            List<Long> toIdList = this.getGroupManager(req.getToId());
            List<String> messages = toIdList.stream()
                    .map(toUserId -> {
                        WebSocket conn = UserWsConn.get(req.getToId());
                        ContactMessageResp resp = req.ofResp(request.getId(), toUserId, userInfo);
                        if (conn == null) {
                            // TODO: 2022/4/23 离线推送
                            log.warn("用户[{}]没有连接", req.getToId());
                        } else {
                            conn.send(WsPacket.newNotifyPacket(resp).encode());
                        }
                        return Json.toJson(resp.toMessage(toUserId, MessageSyncer.incrSeq(String.valueOf(toUserId))));
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
        member.setAddTime(LocalDateTime.now());
        member.setAddWay(0);
        groupChatMemberDao.save(member);
        ContactMessageResp resp = req.ofResp(req.getRequestId(), applyId, userInfo);
        WebSocket conn = UserWsConn.get(applyId);
        if (conn == null) {
            // TODO: 2022/4/23 离线推送
            log.warn("用户[{}]没有连接", applyId);
        } else {
            conn.send(WsPacket.newNotifyPacket(resp).encode());
        }
        MessageSyncer.saveToMongo(resp.toMessage(applyId, MessageSyncer.incrSeq(String.valueOf(applyId))));

    }

    public List<Long> getGroupManager(Long groupId) {
        return this.groupChatMemberDao.getGroupManager(groupId);
    }
}
