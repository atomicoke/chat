package org.atomicoke.logic.modules.friend.domain.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.atomicoke.logic.modules.friend.domain.entity.Friend;
import org.atomicoke.logic.modules.friend.domain.model.vo.FriendInfoVO;

/**
 * @author 98065
 * @description 针对表【friend(用户加的好友表)】的数据库操作Mapper
 * @createDate 2022-04-10 13:16:13
 * @Entity generator.domain.Friend
 */
public interface FriendMapper extends BaseMapper<Friend> {

    FriendInfoVO info(final @Param("ownerId") Long ownerId, @Param("friendId") Long friendId);
}