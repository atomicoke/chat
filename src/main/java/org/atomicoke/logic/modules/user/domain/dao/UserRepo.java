package org.atomicoke.logic.modules.user.domain.dao;

import cn.hutool.core.collection.CollUtil;
import org.atomicoke.inf.common.Assert;
import org.atomicoke.inf.middleware.db.BaseRepo;
import org.atomicoke.logic.modules.user.domain.dao.mapper.UserMapper;
import org.atomicoke.logic.modules.user.domain.entity.User;
import org.atomicoke.logic.modules.user.domain.model.req.SearchUserReq;
import org.atomicoke.logic.modules.user.domain.model.vo.BasicInfoVO;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public BasicInfoVO basicInfo(final String uname) {
        return this.baseMapper.basicInfo(uname);
    }

    public List<BasicInfoVO> search(final SearchUserReq req) {
        return this.baseMapper.search(req);
    }

    public String getNickName(Long id) {
        User user = this.lq()
                .select(User::getNickName)
                .eq(User::getId, id)
                .one();
        Assert.notNull(user, "用户不存在！");
        return user.getNickName();
    }

    public Map<Long, String> getNickName(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<User> list = this.lq()
                .select(User::getId,
                        User::getNickName)
                .in(User::getId, ids)
                .list();
        Assert.ensureTrue(list != null && list.size() == ids.size(), "userId 不合法！");
        return list.stream().collect(Collectors.toMap(User::getId, User::getNickName));
    }
}