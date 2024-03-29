package org.atomicoke.inf.middleware.sky.config;

import cn.hutool.extra.spring.SpringUtil;
import http.HttpServer;
import http.HttpServerRequest;
import http.HttpServerResponse;
import http.Router;
import http.ext.HttpHandler;
import io.github.fzdwx.lambada.internal.Tuple2;
import io.github.fzdwx.lambada.lang.NvMap;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.middleware.sky.Ws;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
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
    public void afterPropertiesSet() {
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

        HttpServer.create()
                .handle((req, resp) -> {

                    final Tuple2<HttpHandler, NvMap> t2 = router.match(req);

                    final HttpHandler httpHandler = t2.v1;
                    if (httpHandler == null) {
                        resp.notFound();
                        return;
                    }

                    httpHandler.handle(req, resp);
                })
                .withGroup(0, 0)
                .start(port);
    }

    @NotNull
    private HttpHandler parseHandleFromMethod(final Object bean) {
        HttpHandler handler;
        try {
            final Method handle = bean.getClass().getMethod("handle", HttpServerRequest.class, HttpServerResponse.class);
            handler = (request, resp) -> {
                try {
                    handle.invoke(bean, request, resp);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };
        } catch (NoSuchMethodException e) {
            log.info("unRegister ws endpoint: {}", bean.getClass().getName());
            throw new RuntimeException(e);
        }
        return handler;
    }
}