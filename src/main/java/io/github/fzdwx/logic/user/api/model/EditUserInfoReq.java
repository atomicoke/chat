package io.github.fzdwx.logic.user.api.model;

import io.github.fzdwx.inf.common.exc.Exceptions;
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
     * 密码
     */
    private String passwd;

    /**
     * role key[admin,user,...]
     */
    private String roleKey;

    public void preCheck() {
        if (id == null) {
            throw Exceptions.verify("id不能为空");
        }
    }
}