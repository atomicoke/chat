package org.atomicoke.logic.modules.group.domain.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.atomicoke.logic.modules.group.domain.entity.GroupChatApply;

/**
 * @author fzdwx
 */
public interface GroupChatApplyMapper extends BaseMapper<GroupChatApply> {

    Integer insertIgnore(GroupChatApply groupChatApply);
}