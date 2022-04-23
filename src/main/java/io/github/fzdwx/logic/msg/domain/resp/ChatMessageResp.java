package io.github.fzdwx.logic.msg.domain.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.fzdwx.inf.common.contants.ChatConst;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import io.github.fzdwx.inf.middleware.minio.Minio;
import io.github.fzdwx.logic.modules.chathistory.domain.entity.ChatHistory;
import io.github.fzdwx.logic.msg.ws.packet.ChatMessagePacket;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import static io.github.fzdwx.inf.common.contants.ChatConst.ContentType.Text;
import static io.github.fzdwx.lambada.Lang.eq;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/10 17:20
 */
@Data
@Slf4j
@Accessors(chain = true)
public class ChatMessageResp {

    /**
     * 当前信箱所属人id
     */
    @JsonIgnore
    private Long boxOwnerId;

    /**
     * 当前信箱所属人 全局seq(每收到或发送一条消息则加1)
     */
    @JsonIgnore
    private Long boxOwnerSeq;

    /**
     * 消息id
     */
    private String messageId;
    private String fromId;
    private String fromUname;
    private String fromAvatar;
    /**
     * 根据sessionType有不同的含义
     * {@link io.github.fzdwx.inf.common.contants.ChatConst.SessionType#single} 实际接收人id
     * {@link io.github.fzdwx.inf.common.contants.ChatConst.SessionType#group} 群聊id
     */
    private String toId;
    private int sessionType;
    private int msgFrom;
    private LocalDateTime sendTime;
    private ChatMessage chatMessage;

    public ChatMessageResp copy(final Long boxOwnerId,final Long boxOwnerSeq) {
        final ChatMessageResp chatMessageResp = new ChatMessageResp();
        chatMessageResp.setBoxOwnerId(boxOwnerId);
        chatMessageResp.setBoxOwnerSeq(boxOwnerSeq);
        chatMessageResp.setMessageId(this.messageId);
        chatMessageResp.setFromId(this.fromId);
        chatMessageResp.setFromUname(this.fromUname);
        chatMessageResp.setFromAvatar(this.fromAvatar);
        chatMessageResp.setToId(this.toId);
        chatMessageResp.setSessionType(this.sessionType);
        chatMessageResp.setMsgFrom(this.msgFrom);
        chatMessageResp.setSendTime(this.sendTime);
        chatMessageResp.setChatMessage(this.chatMessage);
        return chatMessageResp;
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
            if (!eq(contentType.intValue(), Text)) {
                this.content = Minio.getAccessUrl(this.content);
            }
        }
    }

    public ChatMessageResp fixUrl() {
        this.chatMessage.fixUrl();
        return this;
    }

    public static ChatMessageResp from(final UserInfo userInfo, final ChatMessagePacket packet, final ChatHistory chatHistory) {
        final var resp = new ChatMessageResp();
        resp.setMessageId(String.valueOf(chatHistory.getId()));
        resp.setFromId(userInfo.getId());
        resp.setFromUname(userInfo.getUname());
        resp.setFromAvatar(userInfo.getAvatar());
        resp.setToId(String.valueOf(chatHistory.getToId()));
        resp.setSessionType(packet.getSessionType());
        resp.setMsgFrom(packet.getMsgFrom());
        resp.setSendTime(packet.getSendTime());
        resp.setChatMessage(chatHistory.toResp());
        return resp;
    }
}