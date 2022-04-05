package io.github.fzdwx.logic.msg;

import io.github.fzdwx.inf.common.util.Json;
import io.github.fzdwx.inf.common.web.Web;
import io.github.fzdwx.logic.domain.dao.ChatLogDao;
import io.github.fzdwx.logic.msg.api.model.ChatMessageVO;
import io.github.fzdwx.logic.msg.api.model.SendChatMessageReq;
import io.github.fzdwx.logic.ws.UserWsConn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/5 19:38
 */
@Service
@RequiredArgsConstructor
public class MessageService {

    private final ChatLogDao chatLogDao;

    public void send(final SendChatMessageReq sendChatMessageReq) {

    }

    public void sendAll(final SendChatMessageReq sendChatMessageReq) {
        final var userInfo = Web.getUserInfo();
        chatLogDao.save(sendChatMessageReq);

        final var msgVO = ChatMessageVO.from(sendChatMessageReq, userInfo);
        UserWsConn.foreach((id, ws) -> {
            if (!id.equals(userInfo.getId())) {
                ws.send(Json.toJson(msgVO));
            }
        });
    }
}