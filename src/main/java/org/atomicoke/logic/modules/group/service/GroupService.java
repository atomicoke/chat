package org.atomicoke.logic.modules.group.service;

import io.github.fzdwx.inf.msg.WebSocket;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.contants.ChatConst;
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
import org.atomicoke.logic.msg.domain.resp.NotifyResp;
import org.atomicoke.logic.msg.sync.MessageSyncer;
import org.atomicoke.logic.msg.ws.UserWsConn;
import org.atomicoke.logic.msg.ws.WsPacket;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhiyuan
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
            List<NotifyResp> respList = toIdList.stream()
                    .map(e -> {
                        WebSocket conn = UserWsConn.get(req.getToId());
                        NotifyResp notifyResp = req.ofResp(request.getId(), e, userInfo);
                        if (conn == null) {
                            // TODO: 2022/4/23 离线推送
                            log.warn("用户[{}]没有连接", req.getToId());
                        } else {
                            conn.send(WsPacket.newNotifyPacket(notifyResp, ChatConst.Notify.contact).encode());
                        }
                        return notifyResp;
                    }).collect(Collectors.toList());

            MessageSyncer.saveNotifyToMongo(respList);
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
        NotifyResp notifyResp = req.ofResp(req.getRequestId(), applyId, userInfo);
        WebSocket conn = UserWsConn.get(applyId);
        if (conn == null) {
            // TODO: 2022/4/23 离线推送
            log.warn("用户[{}]没有连接", applyId);
        } else {
            conn.send(WsPacket.newNotifyPacket(notifyResp, ChatConst.Notify.contact).encode());
        }
        MessageSyncer.saveNotifyToMongo(notifyResp);

    }

    public List<Long> getGroupManager(Long groupId) {
        return this.groupChatMemberDao.getGroupManager(groupId);
    }
}
