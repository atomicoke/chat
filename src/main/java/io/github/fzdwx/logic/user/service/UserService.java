package io.github.fzdwx.logic.user.service;

import io.github.fzdwx.inf.common.exc.Exceptions;
import io.github.fzdwx.inf.common.exc.VerifyException;
import io.github.fzdwx.inf.common.web.Web;
import io.github.fzdwx.logic.domain.entity.UserEntity;
import io.github.fzdwx.logic.user.api.model.EditUserInfoReq;
import io.github.fzdwx.logic.user.api.model.SingInReq;
import io.github.fzdwx.logic.user.api.model.SingUpReq;
import io.github.fzdwx.logic.domain.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 22:30
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    /**
     * 注册
     */
    public String singUp(final SingUpReq req) {
        final var count = userDao.countWithUname(req.getUname());
        if (count > 0) {
            throw new VerifyException("用户名已存在");
        }

        final var entity = UserEntity.from(req);

        this.userDao.save(entity);

        return Web.doLogin(entity);
    }

    /**
     * 登录
     */
    public String singIn(final SingInReq req) {
        final var user = this.userDao.findOne(req.getUname());
        if (user == null) {
            throw new VerifyException("用户不存在，请注册");
        }

        if (!UserEntity.checkPasswd(req.getPasswd(), user.getPasswd(), user.getSalt())) {
            throw new VerifyException("密码错误");
        }

        return Web.doLogin(user);
    }

    /**
     * 编辑用户信息
     */
    public boolean editUserInfo(final EditUserInfoReq req) {
        req.preCheck();

        final var user = this.userDao.findOne(req.getId());
        if (user == null) {
            throw Exceptions.verify("用户不存在");
        }

        final var userEntity = UserEntity.form(req, user);
        final var b = this.userDao.updateById(userEntity);

        if (b) {
            Web.cacheUserToSession(userEntity);
        }
        return b;
    }
}