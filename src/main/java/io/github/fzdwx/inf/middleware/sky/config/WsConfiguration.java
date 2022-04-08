package io.github.fzdwx.inf.middleware.sky.config;

import cn.hutool.extra.spring.SpringUtil;
import io.github.fzdwx.inf.Netty;
import io.github.fzdwx.inf.http.core.HttpHandler;
import io.github.fzdwx.inf.middleware.sky.Ws;
import io.github.fzdwx.inf.route.Router;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 18:25
 */
@Configuration
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
                    final var ws = bean.getClass().getAnnotation(Ws.class);
                    router.GET(ws.value(), (HttpHandler) bean);
                });

        Netty.HTTP(port, router)
                .dev().bind();
    }

}