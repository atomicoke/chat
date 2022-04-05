package io.github.fzdwx.logic.msg.model;

import lombok.Data;

import java.util.Date;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/5 18:30
 */
@Data
public class ChatMessageVO {

    /**
     * 发送者id
     */
    private Long fromId;

    /**
     * 接收者id
     */
    private Long toId;
    /**
     * 内容
     */
    private String content;
    /**
     * 内容类型
     */
    private Integer contentType;
    /**
     * 会话类型 1:单聊 2:群聊
     */
    private Long sessionType;
    /**
     * 消息发送者的类型 1:用户 2:系统
     */
    private Integer msgFrom;
    /**
     * 发送时间
     */
    private Date sendTime;
}