package org.atomicoke.logic.modules.user.domain.model.req;

import lombok.Data;
import org.atomicoke.inf.common.err.Err;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/5/3 9:50
 */
@Data
public class SearchUserReq {

    /**
     * 用戶名
     */
    private String uname;

    /**
     * 手机号
     */
    private Long mobile;

    public void preCheck() {
        if (uname == null && mobile == null) {
            throw Err.illegalArgument("uname or mobile is required");
        }
    }
}