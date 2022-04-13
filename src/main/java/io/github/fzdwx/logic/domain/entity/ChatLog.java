package io.github.fzdwx.logic.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.fzdwx.inf.middleware.minio.Minio;
import io.github.fzdwx.logic.msg.ws.packet.resp.ChatMessageResp;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import static io.github.fzdwx.lambada.Lang.eq;
import static io.github.fzdwx.logic.contants.ChatConst.ContentType.Text;

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
     *
     * @see io.github.fzdwx.logic.contants.ChatConst.SessionType
     */
    private Integer sessionType;
    /**
     * 接收者id
     */
    private Long toId;
    /**
     * 消息发送者的类型 1:用户 2:系统
     *
     * @see io.github.fzdwx.logic.contants.ChatConst.MsgFrom
     */
    private Integer msgFrom;
    /**
     * 消息类型
     *
     * @see io.github.fzdwx.logic.contants.ChatConst.ContentType
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
    private Date sendTime;

    public ChatMessageResp.ChatMessage toResp() {
        final var resp = new ChatMessageResp.ChatMessage();
        resp.setId(id);
        resp.setFileName(fileName);
        resp.setFileSize(fileSize);
        resp.setContentType(this.contentType);

        if (eq(contentType.intValue(), Text)) {
            resp.setContent(this.content);
        } else {
            resp.setContent(Minio.getAccessUrl(this.content));
        }

        return resp;
    }
}