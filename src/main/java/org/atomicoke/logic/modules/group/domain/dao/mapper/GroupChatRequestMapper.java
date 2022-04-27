package org.atomicoke.logic.modules.group.domain.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.atomicoke.logic.modules.group.domain.entity.GroupChatRequest;

/**
* @author 98065
* @description 针对表【group_chat_request(用户发起加群申请表)】的数据库操作Mapper
* @createDate 2022-04-10 13:16:13
* @Entity generator.domain.GroupChatRequest
*/
public interface GroupChatRequestMapper extends BaseMapper<GroupChatRequest> {

    Integer insertIgnore(GroupChatRequest groupChatRequest);
}