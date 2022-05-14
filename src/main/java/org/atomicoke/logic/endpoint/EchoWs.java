package org.atomicoke.logic.endpoint;

import cn.dev33.satoken.stp.StpUtil;
import http.HttpServerRequest;
import http.HttpServerResponse;
import http.ext.HttpHandler;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.web.Web;
import org.atomicoke.inf.middleware.sky.Ws;
import org.atomicoke.logic.modules.msg.UserWsConn;
import org.atomicoke.logic.modules.msg.WsPacket;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 15:15
 */
@Ws("/echo")
@Slf4j
public class EchoWs implements HttpHandler {

    @Override
    public void handle(final HttpServerRequest request, final HttpServerResponse resp) {
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
                            ws.send("无效的数据包");
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