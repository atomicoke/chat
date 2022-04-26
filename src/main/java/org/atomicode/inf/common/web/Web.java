package org.atomicode.inf.common.web;

import cn.dev33.satoken.stp.StpUtil;
import org.atomicode.inf.common.err.Err;
import org.atomicode.inf.common.web.core.Context;
import org.atomicode.inf.common.web.model.UserInfo;
import org.atomicode.logic.modules.user.domain.entity.User;

import javax.annotation.Nullable;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 21:49
 */
public class Web {

    public final static String USER = "user";

    public static String doLogin(final User user) {
        StpUtil.login(user.getId());

        cacheUser(user);

        return StpUtil.getTokenValue();
    }

    public static UserInfo cacheUser(final User entity) {
        final UserInfo userInfo = UserInfo.of(entity);
        Context.user(userInfo);
        cacheUserToSession(userInfo);
        return userInfo;
    }

    public static void cacheUserToSession(final UserInfo userInfo) {
        StpUtil.getSessionByLoginId(userInfo.getIdLong()).set(USER, userInfo);
    }

    public static UserInfo getUserInfo() {
        final var user = Context.user();
        if (user != null) {
            return user;
        }

        final var sessionUser = StpUtil.getSession().get(USER);
        if (sessionUser == null) {
            throw Err.forbidden("操作非法,用户信息丢失");
        }

        Context.user((UserInfo) sessionUser);
        return (UserInfo) sessionUser;
    }

    public static UserInfo getUserInfo(final Object loginId) {
        return (UserInfo) StpUtil.getSessionByLoginId(loginId).get(USER);
    }

    public static void singOut() {
        StpUtil.logout();
    }

    /**
     * @apiNote 当token对应的用户没登录时，返回null
     */
    @Nullable
    public static UserInfo getUserInfoByToken(final String token) {
        final var loginId = StpUtil.getLoginIdByToken(token);
        if (loginId == null) {
            return null;
        }
        return getUserInfo(loginId);
    }
}