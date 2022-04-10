package io.github.fzdwx.logic.domain.mapper;

import io.github.fzdwx.logic.domain.entity.FriendRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 98065
* @description 针对表【friend_request(用户发起好友申请表)】的数据库操作Mapper
* @createDate 2022-04-10 13:16:13
* @Entity generator.domain.FriendRequest
*/
public interface FriendRequestMapper extends BaseMapper<FriendRequest> {

}