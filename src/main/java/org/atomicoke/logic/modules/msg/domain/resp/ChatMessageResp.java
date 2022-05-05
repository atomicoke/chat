package org.atomicoke.logic.modules.msg.domain.resp;

import io.github.fzdwx.lambada.Lang;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.inf.middleware.minio.Minio;
import org.atomicoke.logic.modules.chathistory.domain.entity.ChatHistory;
import org.atomicoke.logic.modules.msg.domain.model.Message;
import org.atomicoke.logic.modules.msg.packet.chat.ChatMessagePacket;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/10 17:20
 */
@Data
@Slf4j
@Accessors(chain = true)
public class ChatMessageResp implements MessageResp {

    private final static String type = "chat";

    /**
     * 消息id
     */
    private String chatId;
    /**
     * 消息发送人id
     */
    private String fromId;
    /**
     * 根据sessionType有不同的含义
     * {@link ChatConst.SessionType#single} 实际接收人id
     * {@link ChatConst.SessionType#group} 群聊id
     */
    private String toId;
    /**
     * @see ChatConst.SessionType
     */
    private int sessionType;
    /**
     * @see ChatConst.MsgFrom
     */
    private int msgFrom;
    /**
     * 发送时间
     */
    private Long sendTime;

    private ChatMessage chatMessage;

    @Override
    public String type() {
        return type;
    }

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
        private Integer contentType;

        private String fileName;

        private Integer fileSize;

        public void fixUrl() {
            if (!Lang.eq(contentType.intValue(), ChatConst.ContentType.Text)) {
                this.content = Minio.getPubAccessUrl(this.content);
            }
        }
    }

    public ChatMessageResp fixUrl() {
        this.chatMessage.fixUrl();
        return this;
    }

    public static ChatMessageResp from(final UserInfo userInfo, final ChatMessagePacket packet, final ChatHistory chatHistory) {
        final var resp = new ChatMessageResp();
        resp.setChatId(String.valueOf(chatHistory.getId()));
        resp.setFromId(userInfo.getId());
        resp.setToId(String.valueOf(chatHistory.getToId()));
        resp.setSessionType(packet.getSessionType());
        resp.setMsgFrom(packet.getMsgFrom());
        resp.setSendTime(packet.getChatMessage().getSendTime());
        resp.setChatMessage(chatHistory.toResp());
        return resp;
    }

    public Message toMessage(Long boxOwnerId, Long boxOwnerSeq) {
        Message message = new Message();
        message.setBoxOwnerId(String.valueOf(boxOwnerId));
        message.setBoxOwnerSeq(String.valueOf(boxOwnerSeq));
        message.setMessageType(this.type());
        message.setBody(this.fixUrl());
        return message;
    }
}