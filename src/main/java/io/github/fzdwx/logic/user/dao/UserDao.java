package io.github.fzdwx.logic.user.dao;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import io.github.fzdwx.logic.domain.dao.base.UserBaseDao;
import io.github.fzdwx.logic.domain.entity.UserEntity;
import org.springframework.stereotype.Repository;

/**
 * UserDaoImpl: 数据操作接口实现
 * <p>
 * 这只是一个减少手工创建的模板文件
 * 可以任意添加方法和实现, 更改作者和重定义类名
 * <p/>@author Powered By Fluent Mybatis
 */
@Repository
public class UserDao extends UserBaseDao implements IBaseDao<UserEntity> {

    public int countWithUname(final String uname) {
        return this.mapper.count(query()
                .where.uname().eq(uname)
                .and.isDeleted().eq(false)
                .end());
    }

    public UserEntity findOne(final String uname) {
        return this.mapper.findOne(query().where.uname().eq(uname).end());
    }

    public UserEntity findOne(final Long id) {
        return this.mapper.findById(id);
    }
}