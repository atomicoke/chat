package org.atomicode.logic.msg.ws.endpoint;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.atomicode.inf.common.web.Web;
import org.atomicode.inf.http.core.HttpHandler;
import org.atomicode.inf.http.core.HttpServerRequest;
import org.atomicode.inf.http.core.HttpServerResponse;
import org.atomicode.inf.middleware.sky.Ws;
import org.atomicode.logic.msg.ws.UserWsConn;
import org.atomicode.logic.msg.ws.WsPacket;

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

        final var userInfo = Web.getUserInfoByToken(token);
        if (userInfo == null) {
            resp.end("token无效");
            return;
        }

        request.upgradeToWebSocket()
                .then(ws -> {

                    UserWsConn.attachUserInfo(ws, userInfo);

                    final var id = userInfo.getIdLong();

                    ws.mountOpen(h -> {
                        UserWsConn.add(id, ws);
                    });

                    ws.mountClose(h -> {
                        UserWsConn.remove(id);
                    });

                    ws.mountError(h -> {
                        log.error("handle packet error", h);
                    });

                    ws.mountText(s -> {
                        final var wsPacket = WsPacket.decode(s);
                        if (wsPacket == null) {
                            log.error("[decode packet] | unrecognized : {}", s);
                        } else {
                            WsPacket.routing(wsPacket.mountWebsocket(ws));
                        }

                        // switch (wsPacket.type()) {
                        //
                        // }
                    });
                });
    }
}