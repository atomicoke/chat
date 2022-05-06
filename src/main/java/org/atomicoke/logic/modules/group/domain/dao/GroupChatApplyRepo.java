package org.atomicoke.logic.modules.group.domain.dao;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.err.Err;
import org.atomicoke.inf.common.util.Time;
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

    public boolean saveIgnore(GroupChatApply groupChatApply) {
        return SqlHelper.retBool(this.baseMapper.insertIgnore(groupChatApply));

    }

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
                .select(GroupChatApply::getType)
                .select(GroupChatApply::getGroupId)
                .select(GroupChatApply::getUserId)
                .select(GroupChatApply::getApplyUserId)
                .eq(GroupChatApply::getId, id)
                .one();
        if (apply == null) {
            throw Err.verify("加群请求不存在!");
        }
        return apply;
    }
}