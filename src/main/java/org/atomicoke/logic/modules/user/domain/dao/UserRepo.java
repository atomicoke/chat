package org.atomicoke.logic.modules.user.domain.dao;

import org.atomicoke.inf.middleware.db.BaseRepo;
import org.atomicoke.logic.modules.user.domain.dao.mapper.UserMapper;
import org.atomicoke.logic.modules.user.domain.entity.User;
import org.atomicoke.logic.modules.user.domain.model.vo.BasicInfoVO;
import org.springframework.stereotype.Repository;

/**
 * UserDaoImpl: 数据操作接口实现
 * <p>
 * 这只是一个减少手工创建的模板文件
 * 可以任意添加方法和实现, 更改作者和重定义类名
 * <p/>@author Powered By Fluent Mybatis
 */
@Repository
public class UserRepo extends BaseRepo<UserMapper, User> {

    public long countWithUname(final String uname) {
        return super.count(newLambdaQuery().eq(User::getUname, uname));
    }

    public User findOne(final String uname) {
        return this.getBy(User::getUname, uname);
    }

    public User findOne(final Long id) {
        return super.getById(id);
    }

    public BasicInfoVO basicInfo(final Long userId) {
        return this.baseMapper.basicInfo(userId);
    }
}