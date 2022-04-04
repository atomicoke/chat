package io.github.fzdwx.logic.ws;

import io.github.fzdwx.inf.http.core.HttpHandler;
import io.github.fzdwx.inf.http.core.HttpServerRequest;
import io.github.fzdwx.inf.http.core.HttpServerResponse;
import io.github.fzdwx.inf.ws.config.Ws;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 15:15
 */
@Ws("/echo")
public class EchoWs implements HttpHandler {

    @Override
    public void handle(final HttpServerRequest request, final HttpServerResponse resp) throws Exception {
        request.upgradeToWebSocket()
                .then(ws -> {
                    ws.mountOpen(h -> {
                        ws.send("hello world");
                    });
                });
    }
}