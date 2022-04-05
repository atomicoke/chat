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
 * FriendEntity: 数据映射实体定义
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
        table = "friend",
        defaults = EntityFiledSetter.class,
        schema = "chat"
)
public class FriendEntity extends RichEntity {

    private static final long serialVersionUID = 1L;

    @TableId(
            value = "id",
            auto = false
    )
    private Long id;

    @TableField(
            value = "add_time",
            desc = "添加时间",
            insert = "now()"
    )
    private Date addTime;

    @TableField(
            value = "add_way",
            desc = "添加方式"
    )
    private Integer addWay;

    @TableField(
            value = "friend_id",
            desc = "被添加人"
    )
    private Long friendId;

    @TableField(
            value = "is_deleted",
            desc = "是否逻辑删除"
    )
    private Boolean isDeleted;

    @TableField(
            value = "owner_id",
            desc = "添加人"
    )
    private Long ownerId;

    @TableField(
            value = "remark",
            desc = "添加人给被添加人打的备注"
    )
    private String remark;

    @TableField(
            value = "update_time",
            desc = "更新时间"
    )
    private Date updateTime;

    @Override
    public final Class entityClass() {
        return FriendEntity.class;
    }
}