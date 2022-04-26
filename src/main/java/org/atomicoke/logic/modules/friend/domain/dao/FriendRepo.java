package org.atomicoke.logic.modules.friend.domain.dao;

import org.atomicoke.inf.middleware.db.BaseRepo;
import org.atomicoke.logic.modules.friend.domain.dao.mapper.FriendMapper;
import org.atomicoke.logic.modules.friend.domain.entity.Friend;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/21 16:56
 */
@Repository
public class FriendRepo extends BaseRepo<FriendMapper, Friend> {
}