package org.atomicoke.logic.modules.chathistory.domain.dao;

import org.atomicoke.inf.middleware.db.BaseRepo;
import org.atomicoke.logic.modules.chathistory.domain.dao.mapper.ChatHistoryMapper;
import org.atomicoke.logic.modules.chathistory.domain.entity.ChatHistory;
import org.springframework.stereotype.Repository;

/**
 * ChatLogDao: 数据操作接口实现
 * <p>
 * 这只是一个减少手工创建的模板文件
 * 可以任意添加方法和实现, 更改作者和重定义类名
 * <p/>@author Powered By Fluent Mybatis
 */
@Repository
public class ChatHistoryRepo extends BaseRepo<ChatHistoryMapper, ChatHistory> {

    public int saveIgnore(final ChatHistory chatHistory) {
        return this.baseMapper.saveIgnore(chatHistory);
    }
}