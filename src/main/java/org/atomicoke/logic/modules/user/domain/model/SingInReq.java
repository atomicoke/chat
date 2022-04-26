package org.atomicoke.logic.modules.user.domain.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 22:46
 */
@Data
@Accessors(chain = true)
public class SingInReq {

    /**
     * 用户名
     */
    private String uname;
    /**
     * 密码
     */
    private String passwd;
}