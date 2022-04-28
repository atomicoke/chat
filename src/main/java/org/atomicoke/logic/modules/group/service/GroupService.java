package org.atomicoke.logic.modules.group.service;

import lombok.AllArgsConstructor;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.err.Err;
import org.atomicoke.logic.modules.group.domain.dao.GroupChatMemberRepo;
import org.atomicoke.logic.modules.group.domain.dao.GroupChatRequestRepo;
import org.atomicoke.logic.modules.group.domain.entity.GroupChatMember;
import org.atomicoke.logic.modules.group.domain.entity.GroupChatRequest;
import org.atomicoke.logic.msg.ws.packet.sys.SysContactPacket;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhiyuan
 * @since 2022/4/28
 */
@Service
@AllArgsConstructor
public class GroupService {

    private final GroupChatRequestRepo groupChatRequestDao;
    private final GroupChatMemberRepo groupChatMemberDao;

    /**
     * 保存入群请求
     *
     * @param packet        {@link SysContactPacket}
     * @param requestUserId 请求发起人id
     * @return bool
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveGroupRequest(SysContactPacket packet, Long requestUserId) {
        GroupChatRequest groupChatRequest = SysContactPacket.buildGroupRequest(packet, requestUserId);
        switch (packet.getContactType()) {
            case ChatConst.ContactType.joinGroup -> {
                boolean flag = groupChatRequestDao.saveIgnore(groupChatRequest);
                packet.setRequestId(groupChatRequest.getId());
                return flag;
            }
            case ChatConst.ContactType.agreeGroup -> {
                boolean flag = groupChatRequestDao.updateResult(packet.getRequestId(), requestUserId, packet.getSendTime(), ChatConst.FriendAndGroupApplyResult.agree);
                if (flag) {
                    Long reqId = groupChatRequestDao.getReqId(packet.getRequestId());
                    GroupChatMember member = new GroupChatMember();
                    member.setGroupId(packet.getToId());
                    member.setUserId(reqId);
                    //todo 群昵称
                    member.setNickName(null);
                    member.setRoleType(1);
                    member.setAddTime(LocalDateTime.now());
                    groupChatMemberDao.save(member);
                }
                return flag;
            }
            case ChatConst.ContactType.rejectGroup -> {
                return groupChatRequestDao.updateResult(packet.getRequestId(), requestUserId, packet.getSendTime(), ChatConst.FriendAndGroupApplyResult.reject);
            }
            default -> throw Err.verify("未知的系统消息类型" + packet.getContactType());
        }
    }

    public List<Long> getGroupManager(Long groupId) {
        return this.groupChatMemberDao.getGroupManager(groupId);
    }
}
