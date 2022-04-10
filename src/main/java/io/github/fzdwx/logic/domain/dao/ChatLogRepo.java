package io.github.fzdwx.logic.domain.dao;

import io.github.fzdwx.inf.middleware.db.BaseRepo;
import io.github.fzdwx.logic.domain.entity.ChatLog;
import io.github.fzdwx.logic.domain.mapper.ChatLogMapper;
import org.springframework.stereotype.Repository;

/**
 * ChatLogDao: 数据操作接口实现
 * <p>
 * 这只是一个减少手工创建的模板文件
 * 可以任意添加方法和实现, 更改作者和重定义类名
 * <p/>@author Powered By Fluent Mybatis
 */
@Repository
public class ChatLogRepo extends BaseRepo<ChatLogMapper, ChatLog> {

}