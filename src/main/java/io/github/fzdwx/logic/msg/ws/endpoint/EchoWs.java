package io.github.fzdwx.logic.msg.ws.endpoint;

import cn.dev33.satoken.stp.StpUtil;
import io.github.fzdwx.inf.http.core.HttpHandler;
import io.github.fzdwx.inf.http.core.HttpServerRequest;
import io.github.fzdwx.inf.http.core.HttpServerResponse;
import io.github.fzdwx.inf.middleware.sky.config.Ws;
import io.github.fzdwx.logic.msg.ws.UserWsConn;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 15:15
 */
@Ws("/echo")
@Slf4j
public class EchoWs implements HttpHandler {

    @Override
    public void handle(final HttpServerRequest request, final HttpServerResponse resp) throws Exception {
        final var token = request.params().get(StpUtil.getTokenName());
        if (token == null) {
            resp.end("请携带token");
            return;
        }

        final var userId = StpUtil.getLoginIdByToken(token);
        if (userId == null) {
            resp.end("token无效");
            return;
        }

        request.upgradeToWebSocket()
                .then(ws -> {
                    final var userIdStr = (String) userId;

                    ws.mountOpen(h -> {
                        UserWsConn.add(userIdStr, ws);
                    });

                    ws.mountClose(h -> {
                        UserWsConn.remove(userIdStr);
                    });

                    ws.mountError(h -> {
                        log.error("ws error", h.getCause());
                    });
                });
    }
}