package io.github.fzdwx.inf.web;

import cn.dev33.satoken.filter.SaFilterAuthStrategy;
import cn.dev33.satoken.filter.SaFilterErrorStrategy;
import cn.dev33.satoken.filter.SaServletFilter;
import io.github.fzdwx.inf.exc.ForbiddenException;
import io.github.fzdwx.inf.exc.VerifyException;
import io.github.fzdwx.inf.util.Json;
import io.github.fzdwx.inf.web.core.Context;
import io.github.fzdwx.inf.web.core.TokenListener;
import io.github.fzdwx.inf.web.model.Rest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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
 * @date 2022/4/4 19:04
 */
@Configuration
@RestControllerAdvice
@Slf4j
public class WebConfiguration {

    @ExceptionHandler(Exception.class)
    public Rest<Object> Exception(Exception e) {
        log.error("", e);

        return Rest.failure(e.getClass().getSimpleName() + " : " + e.getMessage())
                .stackTrace(e.getStackTrace());
    }

    @ExceptionHandler(VerifyException.class)
    public Rest<Object> VerifyException(VerifyException e) {
        return Rest.failure(e.getClass().getSimpleName() + " : " + e.getMessage())
                .stackTrace(e.getStackTrace());
    }

    @ExceptionHandler(ForbiddenException.class)
    public Rest<Object> ForbiddenException(ForbiddenException e) {
        return Rest.failure(HttpStatus.FORBIDDEN, e.getMessage())
                .stackTrace(e.getStackTrace());
    }


    /**
     * 1
     * 注册 [Sa-Token全局过滤器]
     */
    @Bean
    @ConditionalOnMissingBean(SaServletFilter.class)
    public SaServletFilter saServletFilter() {
        return new SaServletFilter() {
            @Override
            public void doFilter(final ServletRequest request, final ServletResponse response,
                                 final FilterChain chain) throws IOException, ServletException {
                try {
                    super.doFilter(request, response, chain);
                } finally {
                    Context.clean();
                }
            }
        }.addInclude("/**")
                .setBeforeAuth(this.beforeAuth())
                .setAuth(this.auth())
                .setError(this.handlerError());
    }

    @Bean
    TokenListener tokenListener() {
        return new TokenListener();
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
     * 每次[认证函数]发生异常时执行此函数
     *
     * @see #auth()
     */
    private SaFilterErrorStrategy handlerError() {
        return e -> {
            return Json.toJson(Rest.failure(HttpStatus.UNAUTHORIZED,"auth exception: " + e.getMessage()));
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