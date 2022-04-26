package org.atomicode.inf.middleware.sky.config;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.atomicode.inf.Netty;
import org.atomicode.inf.http.core.HttpHandler;
import org.atomicode.inf.http.core.HttpServerRequest;
import org.atomicode.inf.http.core.HttpServerResponse;
import org.atomicode.inf.middleware.sky.Ws;
import org.atomicode.inf.route.Router;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 18:25
 */
@Configuration
@Slf4j
public class WsConfiguration implements InitializingBean {

    private final int port;
    private final Router router;

    public WsConfiguration(@Value("${server.ws.port}") final int port) {
        this.port = port;
        this.router = Router.router();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SpringUtil.getConfigurableBeanFactory().getBeansWithAnnotation(Ws.class)
                .forEach((name, bean) -> {
                    final HttpHandler handler;
                    final var ws = bean.getClass().getAnnotation(Ws.class);

                    if (!bean.getClass().isAssignableFrom(HttpHandler.class)) {
                        handler = parseHandleFromMethod(bean);
                    } else {
                        handler = ((HttpHandler) bean);
                    }

                    router.GET(ws.value(), handler);
                });

        Netty.HTTP(port, router)
                .dev()
                .bind();
    }

    @NotNull
    private HttpHandler parseHandleFromMethod(final Object bean) {
        HttpHandler handler;
        try {
            final Method handle = bean.getClass().getMethod("handle", HttpServerRequest.class, HttpServerResponse.class);
            handler = (request, resp) -> {
                handle.invoke(bean, request, resp);
            };
        } catch (NoSuchMethodException e) {
            log.info("unRegister ws endpoint: {}", bean.getClass().getName());
            throw new RuntimeException(e);
        }
        return handler;
    }
}