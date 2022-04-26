package org.atomicode.logic.modules.user.service;

import lombok.RequiredArgsConstructor;
import org.atomicode.fzdwx.lambada.Coll;
import org.atomicode.fzdwx.lambada.Lang;
import org.atomicode.inf.common.err.Err;
import org.atomicode.inf.common.web.Web;
import org.atomicode.inf.common.web.model.UserInfo;
import org.atomicode.logic.modules.user.domain.dao.UserRepo;
import org.atomicode.logic.modules.user.domain.entity.User;
import org.atomicode.logic.modules.user.domain.model.EditUserInfoReq;
import org.atomicode.logic.modules.user.domain.model.SingInReq;
import org.atomicode.logic.modules.user.domain.model.SingUpReq;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.atomicode.inf.common.err.Err.verify;

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
            Err.thrVerify("用户名已存在");
        }

        final var entity = User.from(req);

        if (this.userDao.save(entity)) {
            return Web.doLogin(entity);
        }

        throw verify("注册失败");
    }

    /**
     * 登录
     */
    public String singIn(final SingInReq req) {
        final var user = this.userDao.findOne(req.getUname());
        if (user == null) {
            Err.thrVerify("用户不存在，请注册");
        }

        if (!User.checkPasswd(req.getPasswd(), user.getPasswd(), user.getSalt())) {
            Err.thrVerify("密码错误");
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
            throw verify("用户不存在");
        }

        final var userEntity = User.form(req, user);
        final var b = this.userDao.updateById(userEntity);

        if (b) {
            Web.cacheUser(userEntity);
        }
        return b;
    }

    public List<UserInfo> getAllUser() {
        final List<User> users = this.userDao.list();
        if (Lang.isEmpty(users)) {
            return Coll.list();
        }

        return users.stream().map(UserInfo::of).collect(Collectors.toList());
    }

    public UserInfo getUserInfo() {
        return Web.getUserInfo();
    }
}