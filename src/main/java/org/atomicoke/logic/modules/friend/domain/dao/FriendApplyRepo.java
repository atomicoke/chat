package org.atomicoke.logic.modules.friend.domain.dao;

import io.github.fzdwx.lambada.Time;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.err.Err;
import org.atomicoke.inf.middleware.db.BaseRepo;
import org.atomicoke.logic.modules.friend.domain.dao.mapper.FriendApplyMapper;
import org.atomicoke.logic.modules.friend.domain.entity.FriendApply;
import org.springframework.stereotype.Repository;

/**
 * @author oneIdler
 * @since 2022/4/26
 */
@Repository
public class FriendApplyRepo extends BaseRepo<FriendApplyMapper, FriendApply> {
    public boolean updateResult(Long id, int result) {
        return this.lu()
                .set(FriendApply::getHandlerTime, Time.now())
                .set(FriendApply::getHandlerResult, result)
                .eq(FriendApply::getId, id)
                .eq(FriendApply::getHandlerResult, ChatConst.FriendAndGroupApplyResult.unOperated)
                .update();
    }

    public Long getApplyUserId(Long id) {
        FriendApply request = this.lq()
                .select(FriendApply::getApplyUserId)
                .eq(FriendApply::getId, id)
                .one();
        if (request == null) {
            throw Err.verify("好友请求不存在!");
        }
        return request.getApplyUserId();
    }

    public boolean existByResult(Long applyUserId, Long userId) {
        return this.lq()
                .eq(FriendApply::getApplyUserId, applyUserId)
                .eq(FriendApply::getUserId, userId)
                .eq(FriendApply::getHandlerResult, ChatConst.FriendAndGroupApplyResult.unOperated)
                .exists();
    }
}