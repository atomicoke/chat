package org.atomicode.logic.modules.chathistory.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.atomicode.inf.common.contants.ChatConst;
import org.atomicode.logic.msg.domain.resp.ChatMessageResp;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 聊天记录
 *
 * @TableName chat_history
 */
@TableName(value = "chat_history")
@Data
@Accessors(chain = true)
public class ChatHistory implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 发送人
     */
    private Long fromId;
    /**
     * 会话类型 1:单聊 2:群聊
     *
     * @see ChatConst.SessionType
     */
    private Integer sessionType;
    /**
     * 接收者id 当为单聊时为userId,当为群聊是为群id
     */
    private Long toId;
    /**
     * 消息发送者的类型 1:用户 2:系统
     *
     * @see ChatConst.MsgFrom
     */
    private Integer msgFrom;
    /**
     * 消息类型
     *
     * @see ChatConst.ContentType
     */
    private Integer contentType;
    /**
     * 消息体
     */
    private String content;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Integer fileSize;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 随机id(用于去重)
     */
    private String randomId;

    public ChatMessageResp.ChatMessage toResp() {
        final var resp = new ChatMessageResp.ChatMessage();

        resp.setFileName(fileName);
        resp.setFileSize(fileSize);
        resp.setContentType(this.contentType);
        resp.setContent(this.content);

        return resp;
    }
}