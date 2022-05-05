package org.atomicoke.logic.modules.user.domain.entity;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.fzdwx.lambada.Lang;
import lombok.Data;
import lombok.experimental.Accessors;
import org.atomicoke.inf.common.util.Time;
import org.atomicoke.inf.common.web.model.RoleConstant;
import org.atomicoke.logic.modules.user.domain.model.EditUserInfoReq;
import org.atomicoke.logic.modules.user.domain.model.SingUpReq;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * 简单演示表
 *
 * @TableName user
 */
@TableName(value = "user")
@Data
@Accessors(chain = true)
public class User implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 用户名
     */
    private String uname;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 手机号
     */
    private Long mobile;
    /**
     * 头像存oss objectName
     */
    private String avatar;
    /**
     * 密码
     */
    private String passwd;
    /**
     * 密码盐
     */
    private String salt;
    /**
     * 角色的key
     */
    private String roleKey;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 是否逻辑删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer delFlag;

    /**
     * 性别 0:未知 1:男 2:女
     */
    private Integer gender;

    public static User from(final SingUpReq req) {
        final var salt = RandomUtil.randomString(8);
        String nickName = StrUtil.isEmpty(req.getNickName()) ? req.getUname() : req.getNickName();
        return new User()
                .setUname(req.getUname())
                .setNickName(nickName)
                .setSalt(salt)
                .setCreateTime(Time.now())
                .setRoleKey(RoleConstant.COMMON)
                .setPasswd(encodePasswd(req.getPasswd(), salt));
    }

    public static User form(final EditUserInfoReq req, final User user) {
        final var newUser = new User();

        newUser.setId(user.getId())
                .setUpdateTime(Time.now())
                .setUname(user.getUname());

        if (req.getNickName() != null) {
            newUser.setNickName(req.getNickName());
        } else newUser.setNickName(user.getNickName());

        if (req.getPasswd() != null) {
            newUser.setPasswd(encodePasswd(req.getPasswd(), user.getSalt()));
        } else newUser.setPasswd(user.getPasswd());

        if (req.getMobile() != null) {
            newUser.setMobile(req.getMobile());
        } else newUser.setMobile(user.getMobile());

        if (req.getAvatar() != null) {
            newUser.setAvatar(req.getAvatar());
        } else newUser.setAvatar(user.getAvatar());

        if (req.getGender() != null) {
            newUser.setGender(req.getGender());
        } else newUser.setGender(user.getGender());

        if (req.getRoleKey() != null) {
            newUser.setRoleKey(req.getRoleKey());
        } else newUser.setRoleKey(user.getRoleKey());

        return newUser;
    }

    public static boolean checkPasswd(final String passwd, final String dbPasswd, final String salt) {
        return Lang.eq(encodePasswd(passwd, salt), dbPasswd);
    }

    @NotNull
    private static String encodePasswd(final String req, final String salt) {
        return SaSecureUtil.md5BySalt(req, salt);
    }
}