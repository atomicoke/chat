package org.atomicoke.logic.modules.user.domain.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.atomicoke.inf.common.err.Err;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/5 8:42
 */
@Data
public class EditUserInfoReq {

    /**
     * id
     */
    private Long id;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 手机号码
     */
    private Long mobile;

    /**
     * 性别 0:未知 1:男 2:女
     */
    private Integer gender;

    /**
     * 密码
     */
    private String passwd;

    /**
     * role key[admin,user,...]
     */
    private String roleKey;

    public void preCheck() {
        if (id == null) {
            throw Err.verify("id不能为空");
        }

        if (StringUtils.isNotEmpty(nickName)) {
            if (nickName.length() >= 20) {
                throw Err.illegalArgument("昵称长度不能超过20");
            }
        }

        if (StringUtils.isNotEmpty(passwd)) {
            if (passwd.length() < 6 || passwd.length() > 16) {
                throw Err.illegalArgument("密码长度必须在6-16位之间");
            }
        }
    }
}