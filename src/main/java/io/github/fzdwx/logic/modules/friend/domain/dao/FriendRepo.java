package io.github.fzdwx.logic.modules.friend.domain.dao;

import io.github.fzdwx.inf.middleware.db.BaseRepo;
import io.github.fzdwx.logic.modules.friend.domain.dao.mapper.FriendMapper;
import io.github.fzdwx.logic.modules.friend.domain.entity.Friend;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/21 16:56
 */
@Repository
public class FriendRepo extends BaseRepo<FriendMapper, Friend> {
}