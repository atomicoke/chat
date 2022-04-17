package io.github.fzdwx.inf.common.web.core;

import io.github.fzdwx.inf.common.Assert;
import io.github.fzdwx.inf.common.web.Web;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * 专用于user info 的参数解析器.
 *
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/17 13:11
 */
public class UserInfoArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return UserInfo.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(@NotNull final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new IllegalStateException("Current request is not of type HttpServletRequest: " + webRequest);
        }

        final var userInfo = Web.getUserInfo();

        Assert.notNull(userInfo, "Current user info is null.");

        return userInfo;
    }
}