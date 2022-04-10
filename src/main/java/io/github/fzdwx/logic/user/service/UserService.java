package io.github.fzdwx.logic.user.service;

import io.github.fzdwx.inf.common.exc.Err;
import io.github.fzdwx.inf.common.exc.VerifyException;
import io.github.fzdwx.inf.common.web.Web;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import io.github.fzdwx.logic.domain.dao.UserRepo;
import io.github.fzdwx.logic.domain.entity.User;
import io.github.fzdwx.logic.user.api.model.EditUserInfoReq;
import io.github.fzdwx.logic.user.api.model.SingInReq;
import io.github.fzdwx.logic.user.api.model.SingUpReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 22:30
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userDao;

    /**
     * 注册
     */
    public String singUp(final SingUpReq req) {
        final var count = userDao.countWithUname(req.getUname());
        if (count > 0) {
            throw new VerifyException("用户名已存在");
        }

        final var entity = User.from(req);

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

        if (!User.checkPasswd(req.getPasswd(), user.getPasswd(), user.getSalt())) {
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
            throw Err.verify("用户不存在");
        }

        final var userEntity = User.form(req, user);
        final var b = this.userDao.updateById(userEntity);

        if (b) {
            Web.cacheUserToSession(userEntity);
        }
        return b;
    }

    public List<UserInfo> getAllUser() {
        return this.userDao.list().stream().map(UserInfo::of).collect(Collectors.toList());
    }
}