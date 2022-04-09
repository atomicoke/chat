package io.github.fzdwx.logic.domain.dao;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import io.github.fzdwx.logic.domain.dao.base.ChatLogBaseDao;
import io.github.fzdwx.logic.domain.entity.ChatLogEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * ChatLogDao: 数据操作接口实现
 * <p>
 * 这只是一个减少手工创建的模板文件
 * 可以任意添加方法和实现, 更改作者和重定义类名
 * <p/>@author Powered By Fluent Mybatis
 */
@Repository
public class ChatLogDao extends ChatLogBaseDao implements IBaseDao<ChatLogEntity> {

    @Transactional(rollbackFor = Exception.class)
    public int saveWithTx(final Collection<ChatLogEntity> toChatLogs) {
        if (toChatLogs == null || toChatLogs.isEmpty()) {
            return 0;
        }

        final int i = super.save(toChatLogs);
        if (i != toChatLogs.size()) {
            throw new RuntimeException("保存失败");
        }

        return i;
    }
}