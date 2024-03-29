package org.atomicoke.logic.modules.friend.domain.dao;

import org.atomicoke.inf.middleware.db.BaseRepo;
import org.atomicoke.logic.modules.friend.domain.dao.mapper.FriendMapper;
import org.atomicoke.logic.modules.friend.domain.entity.Friend;
import org.atomicoke.logic.modules.friend.domain.model.req.SyncFriendReq;
import org.atomicoke.logic.modules.friend.domain.model.vo.FriendInfoVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/21 16:56
 */
@Repository
public class FriendRepo extends BaseRepo<FriendMapper, Friend> {

    /**
     * 是否存在好友关系
     *
     * @param userId1 用户id
     * @param userId2 用户id
     * @return bool
     */
    public boolean existFriend(Long userId1, Long userId2) {
        Long count = lq()
                .in(Friend::getOwnerId, userId1, userId2)
                .in(Friend::getFriendId, userId1, userId2)
                .count();
        return count != null && count == 2;
    }

    public FriendInfoVO info(final Long ownerId, final Long friendId) {
        return this.baseMapper.info(ownerId, friendId);
    }

    public List<FriendInfoVO> sync(final SyncFriendReq req) {
        return this.baseMapper.sync(req);
    }

    public void insertOrUpdate(List<Friend> friends) {
        this.baseMapper.insertOrUpdate(friends);
    }

    public void removeFriend(Long userId, Long friendId) {
        this.lu()
                .eq(Friend::getOwnerId, userId)
                .eq(Friend::getFriendId, friendId)
                .remove();
    }
}