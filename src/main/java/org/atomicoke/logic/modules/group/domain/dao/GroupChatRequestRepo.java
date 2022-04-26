package org.atomicoke.logic.modules.group.domain.dao;

import org.atomicoke.inf.middleware.db.BaseRepo;
import org.atomicoke.logic.modules.group.domain.dao.mapper.GroupChatRequestMapper;
import org.atomicoke.logic.modules.group.domain.entity.GroupChatRequest;
import org.springframework.stereotype.Repository;

/**
 * @author zhiyuan
 * @since 2022/4/26
 */
@Repository
public class GroupChatRequestRepo extends BaseRepo<GroupChatRequestMapper, GroupChatRequest> {

}
