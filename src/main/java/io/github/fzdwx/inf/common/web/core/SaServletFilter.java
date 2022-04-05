package io.github.fzdwx.inf.common.web.core;

import cn.dev33.satoken.filter.SaFilterAuthStrategy;
import cn.dev33.satoken.filter.SaFilterErrorStrategy;
import io.github.fzdwx.inf.common.exc.VerifyException;
import io.github.fzdwx.inf.common.util.Json;
import io.github.fzdwx.inf.common.web.Web;
import io.github.fzdwx.inf.common.web.model.Rest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
            super.doFilter(request, response, chain);
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
            return Json.toJson(Rest.failure(HttpStatus.UNAUTHORIZED, "auth exception: " + e.getMessage()));
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