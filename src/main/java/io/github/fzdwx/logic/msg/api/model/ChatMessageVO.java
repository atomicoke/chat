package io.github.fzdwx.logic.msg.api.model;

import io.github.fzdwx.inf.common.web.model.UserInfo;
import lombok.Data;

import java.util.Date;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/5 20:26
 */
@Data
public class ChatMessageVO {

    private Long fromId;
    private String fromUname;
    private String fromAvatar;
    private Long toId;
    private String content;
    private Integer contentType;
    private Long sessionType;
    private Integer msgFrom;
    private Date sendTime;

    public static ChatMessageVO from(SendChatMessageReq sendChatMessageReq, UserInfo userInfo) {
        final var chatMessageVO = new ChatMessageVO();
        chatMessageVO.setFromId(sendChatMessageReq.getFromId());
        chatMessageVO.setFromUname(userInfo.getUname());
        chatMessageVO.setFromAvatar(userInfo.getAvatar());
        chatMessageVO.setToId(sendChatMessageReq.getToId());
        chatMessageVO.setContent(sendChatMessageReq.getContent());
        chatMessageVO.setContentType(sendChatMessageReq.getContentType());
        chatMessageVO.setSessionType(sendChatMessageReq.getSessionType());
        chatMessageVO.setMsgFrom(sendChatMessageReq.getMsgFrom());
        chatMessageVO.setSendTime(sendChatMessageReq.getSendTime());

        return chatMessageVO;
    }
}