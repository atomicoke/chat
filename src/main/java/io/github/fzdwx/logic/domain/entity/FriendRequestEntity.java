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
 * FriendRequestEntity: 数据映射实体定义
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
        table = "friend_request",
        schema = "chat",
        defaults = EntityFiledSetter.class

)
public class FriendRequestEntity extends RichEntity {

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
            value = "handler_msg",
            desc = "拒绝可以携带msg给发起申请的人"
    )
    private String handlerMsg;

    @TableField(
            value = "handler_result",
            desc = "操作结果 1:未读 2:已读 3:接收 4:拒绝"
    )
    private Integer handlerResult;

    @TableField(
            value = "handler_time",
            desc = "处理时间"
    )
    private Date handlerTime;

    @TableField(
            value = "req_id",
            desc = "添加人"
    )
    private Long reqId;

    @TableField(
            value = "req_message",
            desc = "发送好友申请携带的申请信息"
    )
    private String reqMessage;

    @TableField(
            value = "user_id",
            desc = "被添加人"
    )
    private Long userId;

    @Override
    public final Class entityClass() {
        return FriendRequestEntity.class;
    }
}