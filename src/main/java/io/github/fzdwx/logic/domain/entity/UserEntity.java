package io.github.fzdwx.logic.domain.entity;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.core.util.RandomUtil;
import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import io.github.fzdwx.inf.middleware.db.config.EntityFiledSetter;
import io.github.fzdwx.lambada.Lang;
import io.github.fzdwx.logic.user.api.model.EditUserInfoReq;
import io.github.fzdwx.logic.user.api.model.SingUpReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

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
            insert = "now()"
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

    public static UserEntity from(final SingUpReq req) {
        final var salt = RandomUtil.randomString(8);
        return new UserEntity().setUname(req.getUname())
                .setSalt(salt)
                .setPasswd(encodePasswd(req.getPasswd(), salt));
    }

    public static UserEntity form(final EditUserInfoReq req, final UserEntity user) {
        final var userEntity = new UserEntity();

        userEntity.setId(user.getId());
        userEntity.setUname(user.getUname());
        if (req.getPasswd() != null) {
            userEntity.setPasswd(encodePasswd(req.getPasswd(), user.getSalt()));
        } else userEntity.setPasswd(user.getPasswd());

        if (req.getMobile() != null) {
            userEntity.setMobile(req.getMobile());
        } else userEntity.setMobile(user.getMobile());

        if (req.getAvatar() != null) {
            userEntity.setAvatar(req.getAvatar());
        } else userEntity.setAvatar(user.getAvatar());

        if (req.getRoleKey() != null) {
            userEntity.setRoleKey(req.getRoleKey());
        } else userEntity.setRoleKey(user.getRoleKey());

        return userEntity;
    }

    public static boolean checkPasswd(final String passwd, final String dbPasswd, final String salt) {
        return Lang.eq(encodePasswd(passwd, salt), dbPasswd);
    }

    @NotNull
    private static String encodePasswd(final String req, final String salt) {
        return SaSecureUtil.md5BySalt(req, salt);
    }
}