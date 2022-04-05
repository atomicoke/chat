package io.github.fzdwx.logic.msg;

import io.github.fzdwx.logic.domain.dao.ChatLogDao;
import io.github.fzdwx.logic.msg.model.ChatMessageVO;
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

    public void send(final ChatMessageVO chatMessageVO) {

    }

    public void sendAll(final ChatMessageVO chatMessageVO) {
        chatLogDao.save(chatMessageVO);
        UserWsConn.foreach((id, ws) -> {
            ws.send(chatMessageVO.getContent());
        });
    }
}