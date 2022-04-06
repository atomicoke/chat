package io.github.fzdwx.logic.domain.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import io.github.fzdwx.inf.middleware.db.config.EntityFiledSetter;
import io.github.fzdwx.logic.msg.api.model.SendChatMessageReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * ChatLogEntity: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Data
@Accessors(
        chain = true
)
@EqualsAndHashCode(
        callSuper = false
)
@AllArgsConstructor
@NoArgsConstructor
@FluentMybatis(
        table = "chat_log",
        schema = "chat",
        defaults = EntityFiledSetter.class
)
public class ChatLogEntity extends RichEntity {

    private static final long serialVersionUID = 1L;

    @TableId(
            value = "id",
            auto = false
    )
    private Long id;

    @TableField(
            value = "content",
            desc = "消息体"
    )
    private String content;

    @TableField(
            value = "content_type",
            desc = "消息类型"
    )
    private Integer contentType;

    @TableField(
            value = "from_id",
            desc = "发送人"
    )
    private Long fromId;

    /**
     * @see io.github.fzdwx.logic.contants.ChatConst.MsgFrom
     */
    @TableField(
            value = "msg_from",
            desc = "消息发送者的类型 1:用户 2:系统"
    )
    private Integer msgFrom;

    @TableField(
            value = "send_time",
            desc = "发送时间"
    )
    private Date sendTime;

    /**
     * @see io.github.fzdwx.logic.contants.ChatConst.SessionType
     */
    @TableField(
            value = "session_type",
            desc = "会话类型 1:单聊 2:群聊"
    )
    private Long sessionType;

    @TableField(
            value = "to_id",
            desc = "接收者id"
    )
    private Long toId;

    @Override
    public final Class entityClass() {
        return ChatLogEntity.class;
    }

    public static ChatLogEntity from(final SendChatMessageReq sendChatMessageReq) {
        final var entity = new ChatLogEntity();
        entity.setFromId(sendChatMessageReq.getFromId());
        entity.setToId(sendChatMessageReq.getToId());
        entity.setContent(sendChatMessageReq.getContent());
        entity.setMsgFrom(sendChatMessageReq.getMsgFrom());
        entity.setContentType(sendChatMessageReq.getContentType());
        entity.setSessionType(sendChatMessageReq.getSessionType());
        entity.setSendTime(sendChatMessageReq.getSendTime());

        return entity;
    }
}