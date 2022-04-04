package io.github.fzdwx.logic.user.service;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import io.github.fzdwx.inf.exc.VerifyException;
import io.github.fzdwx.inf.web.Web;
import io.github.fzdwx.lambada.Lang;
import io.github.fzdwx.logic.domain.entity.UserEntity;
import io.github.fzdwx.logic.user.api.model.SingInAndUpResp;
import io.github.fzdwx.logic.user.api.model.SingInReq;
import io.github.fzdwx.logic.user.api.model.SingUpReq;
import io.github.fzdwx.logic.user.dao.UserDao;
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

    public SingInAndUpResp singUp(final SingUpReq singUpReq) {
        final var count = userDao.countWithUname(singUpReq.getUname());
        if (count > 0) {
            throw new VerifyException("用户名已存在");
        }

        final var entity = singUpReq.toEntity();
        final var id = this.userDao.save(entity);

        return doLogin(entity);

    }

    public SingInAndUpResp singIn(final SingInReq req) {
        final var user = this.userDao.findOne(req.getUname());
        if (user == null) {
            throw new VerifyException("用户不存在，请注册");
        }

        if (!Lang.eq(SaSecureUtil.md5BySalt(req.getPasswd(), user.getSalt()), user.getPasswd())) {
            throw new VerifyException("密码错误");
        }


        return doLogin(user);
    }

    private SingInAndUpResp doLogin(final UserEntity entity) {
        StpUtil.login(entity.getId());

        Web.cacheUserToSession(entity);

        return SingInAndUpResp.of(StpUtil.getTokenValue(), entity.getId());
    }
}