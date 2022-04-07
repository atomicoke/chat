package io.github.fzdwx.inf.common.web;

import cn.dev33.satoken.stp.StpUtil;
import io.github.fzdwx.inf.common.exc.ForbiddenException;
import io.github.fzdwx.inf.common.web.core.Context;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import io.github.fzdwx.logic.domain.entity.UserEntity;

import javax.annotation.Nullable;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 21:49
 */
public class Web {

    public final static String USER = "user";

    public static String doLogin(final UserEntity user) {
        StpUtil.login(user.getId());

        Web.cacheUserToSession(user);

        return StpUtil.getTokenValue();
    }

    public static void cacheUserToSession(final UserEntity entity) {
        StpUtil.getSessionByLoginId(entity.getId()).set(USER, UserInfo.of(entity));
    }

    public static UserInfo getUserInfo() {
        final var user = Context.user();
        if (user != null) {
            return user;
        }

        final var sessionUser = StpUtil.getSession().get(USER);
        if (sessionUser == null) {
            throw new ForbiddenException("操作非法");
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