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
 * GroupChatEntity: 数据映射实体定义
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
        table = "group_chat",
        schema = "chat",
        defaults = EntityFiledSetter.class
)
public class GroupChatEntity extends RichEntity {

    private static final long serialVersionUID = 1L;

    @TableId(
            value = "id",
            auto = false
    )
    private Long id;

    @TableField(
            value = "avatar",
            desc = "群头像"
    )
    private String avatar;

    @TableField(
            value = "create_time",
            desc = "创建时间",
            insert = "now()"
    )
    private Date createTime;

    @TableField(
            value = "creator_user_id",
            desc = "群创建者"
    )
    private Long creatorUserId;

    @TableField(
            value = "group_type",
            desc = "群类型"
    )
    private Integer groupType;

    @TableField(
            value = "introduction",
            desc = "群简介"
    )
    private String introduction;

    @TableField(
            value = "name",
            desc = "群名"
    )
    private String name;

    @TableField(
            value = "notification",
            desc = "群公告"
    )
    private String notification;

    @Override
    public final Class entityClass() {
        return GroupChatEntity.class;
    }
}