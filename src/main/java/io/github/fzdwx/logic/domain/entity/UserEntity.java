package io.github.fzdwx.logic.domain.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import io.github.fzdwx.inf.db.config.EntityFiledSetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * UserEntity: 数据映射实体定义
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
        table = "user",
        schema = "chat",
        defaults = EntityFiledSetter.class
)
public class UserEntity extends RichEntity {

    private static final long serialVersionUID = 1L;

    @TableId(
            value = "id",
            auto = false
    )
    private Long id;

    @TableField(
            value = "avatar",
            desc = "头像存oss objectName"
    )
    private String avatar;

    @TableField(
            value = "create_time",
            desc = "更新时间",
            insert = "now()", update = "now()"
    )
    private Date createTime;

    @TableField(
            value = "is_deleted",
            desc = "是否逻辑删除"
    )
    private Integer isDeleted;

    @TableField(
            value = "mobile",
            desc = "手机号"
    )
    private Long mobile;

    @TableField(
            value = "passwd",
            desc = "密码"
    )
    private String passwd;

    @TableField(
            value = "role_key",
            desc = "角色的key"
    )
    private String roleKey;

    @TableField(
            value = "salt",
            desc = "密码盐"
    )
    private String salt;

    @TableField(
            value = "uname",
            desc = "用户名"
    )
    private String uname;

    @TableField(
            value = "update_time",
            desc = "更新时间",
            insert = "now()", update = "now()"
    )
    private Date updateTime;

    @Override
    public final Class entityClass() {
        return UserEntity.class;
    }
}