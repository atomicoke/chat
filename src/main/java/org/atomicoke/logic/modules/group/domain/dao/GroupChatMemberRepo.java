package org.atomicoke.logic.modules.group.domain.dao;

import cn.hutool.core.collection.CollUtil;
import org.atomicoke.inf.middleware.db.BaseRepo;
import org.atomicoke.logic.modules.group.domain.dao.mapper.GroupChatMemberMapper;
import org.atomicoke.logic.modules.group.domain.entity.GroupChatMember;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author oneIdler
 * @since 2022/4/27
 */
@Repository
public class GroupChatMemberRepo extends BaseRepo<GroupChatMemberMapper, GroupChatMember> {
    public List<Long> getGroupManager(Long toId) {
        List<GroupChatMember> list = this.lq()
                .select(GroupChatMember::getUserId)
                .eq(GroupChatMember::getGroupId, toId)
                .in(GroupChatMember::getRoleType, 2, 3)
                .list();
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(GroupChatMember::getUserId).collect(Collectors.toList());
    }

    public boolean existMember(Long userId, Long groupId) {
        return this.lq()
                .eq(GroupChatMember::getUserId, userId)
                .eq(GroupChatMember::getGroupId, groupId)
                .exists();
    }
}
