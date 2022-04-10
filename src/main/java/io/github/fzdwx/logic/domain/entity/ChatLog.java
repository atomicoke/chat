package io.github.fzdwx.logic.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 聊天记录
 *
 * @TableName chat_log
 */
@TableName(value = "chat_log")
@Data
public class ChatLog implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 发送人
     */
    private Long fromId;
    /**
     * 会话类型 1:单聊 2:群聊
     */
    private Integer sessionType;
    /**
     * 接收者id
     */
    private Long toId;
    /**
     * 消息发送者的类型 1:用户 2:系统
     */
    private Integer msgFrom;
    /**
     * 消息类型
     */
    private Integer contentType;
    /**
     * 消息体
     */
    private String content;
    /**
     * 发送时间
     */
    private Date sendTime;
}