package io.github.fzdwx.logic.msg.ws.model;

import io.github.fzdwx.logic.domain.entity.ChatLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/10 11:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChatMessageVO extends ChatLog {

    private Long fromId;
    private String fromName;
    private String fromAvatar;
    private String content;
    private Integer contentType;
    /**
     * @see io.github.fzdwx.logic.contants.ChatConst.MsgFrom
     */
    private Integer msgFrom;
    private Date sendTime;
    /**
     * @see io.github.fzdwx.logic.contants.ChatConst.SessionType
     */
    private Integer sessionType;
    private Long toId;
}