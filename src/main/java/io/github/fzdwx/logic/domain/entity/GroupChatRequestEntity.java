package io.github.fzdwx.logic.domain.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import io.github.fzdwx.inf.middleware.db.config.EntityFiledSetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * GroupChatRequestEntity: 数据映射实体定义
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
        table = "group_chat_request",
        schema = "chat",
        defaults = EntityFiledSetter.class
)
public class GroupChatRequestEntity extends RichEntity {

    private static final long serialVersionUID = 1L;

    @TableId(
            value = "id",
            auto = false
    )
    private Long id;

    @TableField(
            value = "add_way",
            desc = "添加方式"
    )
    private Integer addWay;

    @TableField(
            value = "create_time",
            desc = "创建时间",
            insert = "now()"
    )
    private Date createTime;

    @TableField(
            value = "group_id",
            desc = "群id"
    )
    private Long groupId;

    @TableField(
            value = "handler_msg",
            desc = "拒绝可以携带msg给发起申请的人"
    )
    private String handlerMsg;

    @TableField(
            value = "handler_result",
            desc = "操作结果 1:未操作 2:接收 3:拒绝"
    )
    private Integer handlerResult;

    @TableField(
            value = "handler_time",
            desc = "处理时间"
    )
    private Date handlerTime;

    @TableField(
            value = "handler_user",
            desc = "处理人"
    )
    private Long handlerUser;

    @TableField(
            value = "req_id",
            desc = "申请加入群的人"
    )
    private Long reqId;

    @TableField(
            value = "req_message",
            desc = "发送加群申请携带的申请信息"
    )
    private String reqMessage;

    @Override
    public final Class entityClass() {
        return GroupChatRequestEntity.class;
    }
}