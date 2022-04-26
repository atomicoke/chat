package io.github.fzdwx.logic.modules.user.domain.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 22:28
 */
@Data
@Accessors(chain = true)
public class SingUpReq {

    /**
     * 用户名
     */
    private String uname;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String passwd;
}