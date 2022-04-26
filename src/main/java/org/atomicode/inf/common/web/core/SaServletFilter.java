package org.atomicode.inf.common.web.core;

import cn.dev33.satoken.exception.BackResultException;
import cn.dev33.satoken.exception.StopMatchException;
import cn.dev33.satoken.filter.SaFilterAuthStrategy;
import cn.dev33.satoken.filter.SaFilterErrorStrategy;
import cn.dev33.satoken.router.SaRouter;
import lombok.extern.slf4j.Slf4j;
import org.atomicode.inf.common.err.impl.VerifyException;
import org.atomicode.inf.common.util.Json;
import org.atomicode.inf.common.web.Web;
import org.atomicode.inf.common.web.model.Rest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/5 9:36
 */
@Slf4j
public class SaServletFilter extends cn.dev33.satoken.filter.SaServletFilter {

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
                         final FilterChain chain) throws IOException, ServletException {
        try {
            try {
                // 执行全局过滤器
                SaRouter.match(super.getIncludeList()).notMatch(super.getExcludeList()).check(r -> {
                    beforeAuth.run(null);
                    auth.run(null);
                });

            } catch (StopMatchException e) {

            } catch (Throwable e) {
                final var run = error.run(e);

                if (run instanceof Rest<?> r) {
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().print(Json.toJson(r.getBody()));
                } else {
                    // 1. 获取异常处理策略结果
                    String result = (e instanceof BackResultException) ? e.getMessage() : String.valueOf(run);

                    // 2. 写入输出流
                    if (response.getContentType() == null) {
                        response.setContentType("text/plain; charset=utf-8");
                    }
                    response.getWriter().print(result);
                }

                return;
            }

            // 执行
            chain.doFilter(request, response);
        } finally {
            Context.clean();
        }
    }

    public static SaServletFilter classic() {
        final var classic = new SaServletFilter();
        classic.addInclude("/**")
                .setBeforeAuth(classic.beforeAuth())
                .setAuth(classic.auth())
                .setError(classic.handlerError());
        return classic;
    }

    /**
     * [认证函数]
     */
    private SaFilterAuthStrategy auth() {
        return obj -> {
            final var request = (HttpServletRequest) Context.request();
            if (request.getRequestURI().startsWith("/auth")) {
                return;
            }

            Context.user(Web.getUserInfo());
        };
    }

    /**
     * 在每次[认证函数]之前执行
     *
     * @see #auth()
     */
    private SaFilterAuthStrategy beforeAuth() {
        return o -> {
            Context.request(getHttpServletRequest());
        };
    }

    /**
     * 每次[认证函数]发生异常时执行此函数
     *
     * @see #auth()
     */
    private SaFilterErrorStrategy handlerError() {
        return e -> {
            return Rest.unauthorized("auth exception【 " + e.getMessage() + " 】");
        };
    }

    private static HttpServletRequest getHttpServletRequest() {
        try {
            final var requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (requestAttributes == null) {
                throw new VerifyException("request is null");
            }

            return requestAttributes.getRequest();
        } catch (final Exception e) {
            log.error("can not get Request.", e);
            throw new VerifyException("can not get Request.");
        }
    }
}