package io.github.fzdwx.logic.msg.ws.packet.resp;

import io.github.fzdwx.inf.common.contants.ChatConst;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import io.github.fzdwx.inf.middleware.minio.Minio;
import io.github.fzdwx.logic.domain.entity.ChatLog;
import io.github.fzdwx.logic.msg.ws.packet.ChatMessagePacket;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import static io.github.fzdwx.inf.common.contants.ChatConst.ContentType.Text;
import static io.github.fzdwx.lambada.Lang.eq;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/10 17:20
 */
@Data
@Slf4j
public class ChatMessageResp {

    private String fromId;
    private String fromUname;
    private String fromAvatar;
    private String toId;
    private int sessionType;
    private int msgFrom;
    private Date sendTime;
    private ChatMessage chatMessage;

    @Data
    public static class ChatMessage {

        /**
         * 消息id
         */
        private Long id;

        /**
         * 消息体
         */
        private String content;

        /**
         * 消息类型
         *
         * @see ChatConst.ContentType
         */
        private Integer contentType;

        private String fileName;

        private Integer fileSize;

        public void fixUrl() {
            if (!eq(contentType.intValue(), Text)) {
                this.content = Minio.getAccessUrl(this.content);
            }
        }
    }

    public ChatMessageResp fixUrl() {
        this.chatMessage.fixUrl();
        return this;
    }

    public static ChatMessageResp from(final UserInfo userInfo, final ChatMessagePacket packet, final ChatLog chatLog) {
        final var resp = new ChatMessageResp();
        resp.setFromId(userInfo.getId());
        resp.setFromUname(userInfo.getUname());
        resp.setFromAvatar(userInfo.getAvatar());
        resp.setToId(packet.getToId());
        resp.setSessionType(packet.getSessionType());
        resp.setMsgFrom(packet.getMsgFrom());
        resp.setSendTime(packet.getSendTime());
        resp.setChatMessage(chatLog.toResp());
        return resp;
    }
}