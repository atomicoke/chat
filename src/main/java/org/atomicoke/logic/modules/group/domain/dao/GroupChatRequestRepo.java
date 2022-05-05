package org.atomicoke.logic.modules.group.domain.dao;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.err.Err;
import org.atomicoke.inf.common.util.Time;
import org.atomicoke.inf.middleware.db.BaseRepo;
import org.atomicoke.logic.modules.group.domain.dao.mapper.GroupChatRequestMapper;
import org.atomicoke.logic.modules.group.domain.entity.GroupChatRequest;
import org.springframework.stereotype.Repository;

/**
 * @author oneIdler
 * @since 2022/4/26
 */
@Repository
public class GroupChatRequestRepo extends BaseRepo<GroupChatRequestMapper, GroupChatRequest> {

    public boolean saveIgnore(GroupChatRequest groupChatRequest) {
        return SqlHelper.retBool(this.baseMapper.insertIgnore(groupChatRequest));

    }

    public boolean updateResult(Long id, Long handlerUser, int result) {
        return this.lu()
                .set(GroupChatRequest::getHandlerTime, Time.now())
                .set(GroupChatRequest::getHandlerResult, result)
                .set(GroupChatRequest::getHandlerUser, handlerUser)
                .eq(GroupChatRequest::getId, id)
                .eq(GroupChatRequest::getHandlerResult, ChatConst.FriendAndGroupApplyResult.unOperated)
                .update();
    }
    public GroupChatRequest getApplyIdAndGroupId(Long id) {
        GroupChatRequest request = this.lq()
                .select(GroupChatRequest::getApplyId)
                .select(GroupChatRequest::getGroupId)
                .eq(GroupChatRequest::getId, id)
                .one();
        if (request == null) {
            throw Err.verify("加群请求不存在!");
        }
        return request;
    }
}