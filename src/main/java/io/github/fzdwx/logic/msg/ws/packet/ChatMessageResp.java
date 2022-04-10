package io.github.fzdwx.logic.msg.ws.packet;

import io.github.fzdwx.logic.contants.ChatConst;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

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
    private Date sendTime;
    private List<ChatMessage> chatMessages;

    @Data
    public static class ChatMessage {

        /**
         * 消息体
         */
        private String content;

        /**
         * 消息类型
         *
         * @see ChatConst.ContentType
         */
        private int contentType;

        private String fileName;
    }
}