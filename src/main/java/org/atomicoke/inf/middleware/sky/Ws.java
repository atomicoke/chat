package org.atomicoke.inf.middleware.sky;

import http.ext.HttpHandler;
import org.atomicoke.inf.middleware.sky.config.WsConfiguration;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mark is websocket handler.
 * <p>
 * just mark.
 *
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 18:37
 * @apiNote <pre>{@code @Ws("/hello")
 * public class HelloWs implements HttpHandler {
 *      public void handle(final HttpServerRequest request, final HttpServerResponse resp) throws Exception {
 *          request.upgradeToWebSocket()
 *           .then(ws->{
 *
 *               // your code.
 *
 *           })
 *      }
 * }
 *
 * @Ws("/hello2")
 * public class HelloWs2 {
 *      // method name must is handle, and have httpServerRequest and httpServerResponse parameter.
 *      public void handle(final HttpServerRequest request, final HttpServerResponse resp) throws Exception {
 *          request.upgradeToWebSocket()
 *           .then(ws->{
 *
 *               // your code.
 *
 *           })
 *      }
 * }}
 * </pre>
 * @see HttpHandler
 * @see WsConfiguration
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Ws {

    /**
     * path
     */
    String value();

}