package io.github.fzdwx.ws.config;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 15:16
 */
@Configuration
public class WebSocketConfiguration {

    @Bean
    public HandlerMapping webSocketMapping() {
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        Map<String, WebSocketHandler> handlerMap = new LinkedHashMap<>();
        final var wsMap = SpringUtil.getConfigurableBeanFactory().getBeansWithAnnotation(Ws.class);

        wsMap.forEach((k, v) -> {
            handlerMap.put(k, (WebSocketHandler) v);
        });


        simpleUrlHandlerMapping.setUrlMap(handlerMap);
        simpleUrlHandlerMapping.setOrder(-1);
        return simpleUrlHandlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}