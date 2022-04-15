package io.github.fzdwx.logic.user.api.model;

import io.github.fzdwx.inf.common.err.Err;
import lombok.Data;

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
    }
}