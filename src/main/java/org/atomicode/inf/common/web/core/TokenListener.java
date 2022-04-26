package org.atomicode.inf.common.web.core;

import cn.dev33.satoken.dao.SaTokenDaoRedisJackson;
import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenListener implements SaTokenListener {

    private final SaTokenDaoRedisJackson saTokenDaoRedisJackson;

    public TokenListener() {
        this.saTokenDaoRedisJackson = SpringUtil.getBean(SaTokenDaoRedisJackson.class);
    }

    /**
     * 每次登录时触发
     */
    @Override
    public void doLogin(final String loginType, final Object loginId, final SaLoginModel loginModel) {
        log.info("[ login ] 当前有用户登录：id:[ {} ]", loginId);
    }

    /**
     * 每次注销时触发
     */
    @Override
    public void doLogout(final String loginType, final Object loginId, final String tokenValue) {
        log.info("[ logout ] 当前有用户注销：id:[ {} ]", loginId);
        // 删除token
        this.deleteToken(tokenKey(tokenValue));
    }

    @Override
    public void doKickout(final String loginType, final Object loginId, final String tokenValue) {
        log.info("[ logout ] 当前有用户被踢下线：id:[ {} ]", loginId);
        // 删除token
        this.deleteToken(tokenKey(tokenValue));
    }

    @Override
    public void doReplaced(final String loginType, final Object loginId, final String tokenValue) {
        log.info("[ logout ] 当前有用户被顶下线：id:[ {} ] {}", loginId, tokenValue);
        // 删除token
        this.deleteToken(tokenKey(tokenValue));

    }

    /**
     * 每次被封禁时触发
     */
    @Override
    public void doDisable(final String loginType, final Object loginId, final long disableTime) {
        log.info("[ logout ] 当前有用户被封禁：id:[ {} ]", loginId);
        // ...
    }

    /**
     * 每次被解封时触发
     */
    @Override
    public void doUntieDisable(final String loginType, final Object loginId) {
        log.info("[ logout ] 当前有用户被解封：id:[ {} ]", loginId);
        // ...
    }

    /**
     * 每次创建Session时触发
     */
    @Override
    public void doCreateSession(final String id) {
        // ...
    }

    /**
     * 每次注销Session时触发
     */
    @Override
    public void doLogoutSession(final String id) {
        // ...
    }

    // ================================================================================================================  private

    private void deleteToken(final String tokenValue) {
        this.saTokenDaoRedisJackson.delete(tokenValue);
    }

    /**
     * 获取token值
     * <pre>
     *  1.从 Storage里读取
     *  2.从 请求体
     *  3.从 请求头
     *  4.从 cookie
     * </pre>
     *
     * @return {@link String} token value
     */
    private static String token() {
        return StpUtil.getTokenValue();
    }

    /**
     * 获取token名字
     *
     * @return {@link String} token name
     */
    private static String tokenName() {
        return StpUtil.getTokenName();
    }

    /**
     * 获取 sa-token 持久化token的key
     *
     * @param token token value
     * @return {@link String}
     */
    private static String tokenKey(final String token) {
        return StpUtil.stpLogic.splicingKeyTokenValue(token);
    }
}