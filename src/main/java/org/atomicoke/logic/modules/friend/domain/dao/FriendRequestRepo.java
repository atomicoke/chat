package org.atomicoke.logic.modules.friend.domain.dao;

import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.err.Err;
import org.atomicoke.inf.common.util.Time;
import org.atomicoke.inf.middleware.db.BaseRepo;
import org.atomicoke.logic.modules.friend.domain.dao.mapper.FriendRequestMapper;
import org.atomicoke.logic.modules.friend.domain.entity.FriendRequest;
import org.springframework.stereotype.Repository;

/**
 * @author oneIdler
 * @since 2022/4/26
 */
@Repository
public class FriendRequestRepo extends BaseRepo<FriendRequestMapper, FriendRequest> {
    public boolean updateResult(Long id, int result) {
        return this.lu()
                .set(FriendRequest::getHandlerTime, Time.now())
                .set(FriendRequest::getHandlerResult, result)
                .eq(FriendRequest::getId, id)
                .eq(FriendRequest::getHandlerResult, ChatConst.FriendAndGroupApplyResult.unOperated)
                .update();
    }

    public Long getApplyId(Long id) {
        FriendRequest request = this.lq()
                .select(FriendRequest::getApplyId)
                .eq(FriendRequest::getId, id)
                .one();
        if (request == null) {
            throw Err.verify("好友请求不存在!");
        }
        return request.getApplyId();
    }

    public boolean existByResult(Long applyId, Long userId) {
        return this.lq()
                .eq(FriendRequest::getApplyId, applyId)
                .eq(FriendRequest::getUserId, userId)
                .eq(FriendRequest::getHandlerResult, ChatConst.FriendAndGroupApplyResult.unOperated)
                .exists();
    }
}