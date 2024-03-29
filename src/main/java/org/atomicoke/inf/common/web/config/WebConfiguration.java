package org.atomicoke.inf.common.web.config;

import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;
import cn.dev33.satoken.stp.StpInterface;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.err.impl.ForbiddenException;
import org.atomicoke.inf.common.err.impl.VerifyException;
import org.atomicoke.inf.common.web.core.TokenListener;
import org.atomicoke.inf.common.web.core.UserInfoArgumentResolver;
import org.atomicoke.inf.common.web.model.Rest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 19:04
 */
@Configuration
@RestControllerAdvice
@Slf4j
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册注解拦截器，并排除不需要注解鉴权的接口地址 (与登录拦截器无关)
        registry.addInterceptor(new SaAnnotationInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserInfoArgumentResolver());
    }

    @ExceptionHandler(Exception.class)
    public Rest<Object> Exception(Exception e) {
        log.error("", e);

        return Rest.failure(e.getClass().getSimpleName() + " 【 " + e.getMessage() + " 】", e.getStackTrace());
    }

    @ExceptionHandler(VerifyException.class)
    public Rest<Object> VerifyException(VerifyException e) {
        return Rest.verify(e.getClass().getSimpleName() + " 【 " + e.getMessage() + " 】", e.getStackTrace());
    }

    @ExceptionHandler(ForbiddenException.class)
    public Rest<Object> ForbiddenException(ForbiddenException e) {
        return Rest.forbidden(e.getMessage(), e.getStackTrace());
    }

    @ExceptionHandler(NotRoleException.class)
    public Rest<Object> nor(NotRoleException e) {
        return Rest.unauthorized("您无此操作权限 : " + e.getRole(), e.getStackTrace());
    }

    /**
     * 注册 [Sa-Token全局过滤器]
     */
    @Bean
    @ConditionalOnMissingBean(SaServletFilter.class)
    public SaServletFilter saServletFilter() {
        return org.atomicoke.inf.common.web.core.SaServletFilter.classic();
    }

    @Bean
    public TokenListener tokenListener() {
        return new TokenListener();
    }

    @Bean
    public StpInterface stpInterface() {
        return new org.atomicoke.inf.common.web.core.StpInterface();
    }
}