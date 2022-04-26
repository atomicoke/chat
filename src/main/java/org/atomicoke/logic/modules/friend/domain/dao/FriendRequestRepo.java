package org.atomicoke.logic.modules.friend.domain.dao;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.atomicoke.inf.middleware.db.BaseRepo;
import org.atomicoke.logic.modules.friend.domain.dao.mapper.FriendRequestMapper;
import org.atomicoke.logic.modules.friend.domain.entity.FriendRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * @author zhiyuan
 * @since 2022/4/26
 */
@Repository
public class FriendRequestRepo extends BaseRepo<FriendRequestMapper, FriendRequest> {
    public boolean updateResult(Long id, LocalDateTime handlerTime, int result) {
        return this.lu()
                .set(FriendRequest::getHandlerTime, handlerTime)
                .set(FriendRequest::getHandlerResult, result)
                .eq(FriendRequest::getId, id)
                .eq(FriendRequest::getHandlerResult, 1)
                .update();
    }

    public boolean saveIgnore(FriendRequest friendRequest) {
        return SqlHelper.retBool(this.baseMapper.insertIgnore(friendRequest));
    }
}
