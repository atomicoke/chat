package org.atomicoke.logic.modules.group.domain.dao;

import io.github.fzdwx.lambada.Time;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.err.Err;
import org.atomicoke.inf.middleware.db.BaseRepo;
import org.atomicoke.logic.modules.group.domain.dao.mapper.GroupChatApplyMapper;
import org.atomicoke.logic.modules.group.domain.entity.GroupChatApply;
import org.springframework.stereotype.Repository;

/**
 * @author oneIdler
 * @since 2022/4/26
 */
@Repository
public class GroupChatApplyRepo extends BaseRepo<GroupChatApplyMapper, GroupChatApply> {

    public boolean updateResult(Long id, Long handlerUser, int result) {
        return this.lu()
                .set(GroupChatApply::getHandlerTime, Time.now())
                .set(GroupChatApply::getHandlerResult, result)
                .set(GroupChatApply::getHandlerUser, handlerUser)
                .eq(GroupChatApply::getId, id)
                .eq(GroupChatApply::getHandlerResult, ChatConst.FriendAndGroupApplyResult.unOperated)
                .update();
    }

    public GroupChatApply getApplyById(Long id) {
        GroupChatApply apply = this.lq()
                .select(GroupChatApply::getType,
                        GroupChatApply::getGroupId,
                        GroupChatApply::getUserId,
                        GroupChatApply::getApplyUserId)
                .eq(GroupChatApply::getId, id)
                .one();
        if (apply == null) {
            throw Err.verify("加群请求不存在!");
        }
        return apply;
    }

    public boolean existApply(Long applyUserId, Long groupId) {
        return this.lq()
                .eq(GroupChatApply::getApplyUserId, applyUserId)
                .eq(GroupChatApply::getUserId, applyUserId)
                .eq(GroupChatApply::getGroupId, groupId)
                .eq(GroupChatApply::getHandlerResult, ChatConst.FriendAndGroupApplyResult.unOperated)
                .eq(GroupChatApply::getType, ChatConst.GroupApplyType.apply)
                .exists();
    }

    public boolean existInvite(Long applyUserId, Long userId, Long groupId) {
        return this.lq()
                .eq(GroupChatApply::getApplyUserId, applyUserId)
                .eq(GroupChatApply::getUserId, userId)
                .eq(GroupChatApply::getGroupId, groupId)
                .eq(GroupChatApply::getHandlerResult, ChatConst.FriendAndGroupApplyResult.unOperated)
                .eq(GroupChatApply::getType, ChatConst.GroupApplyType.invite)
                .exists();
    }
}