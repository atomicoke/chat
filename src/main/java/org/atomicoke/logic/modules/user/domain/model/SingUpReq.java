package org.atomicoke.logic.modules.user.domain.model;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.atomicoke.inf.common.err.Err;

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

    public void preCheck() {
        if (uname == null || uname.isEmpty()) {
            throw Err.illegalArgument("用户名不能为空");
        }

        if (this.uname.length() >= 20) {
            throw Err.illegalArgument("用户名长度不能超过20");
        }


        if (passwd == null || passwd.isEmpty()) {
            throw Err.illegalArgument("密码不能为空");
        } else if (passwd.length() < 6 || passwd.length() > 16) {
            throw Err.illegalArgument("密码长度必须在6-16位之间");
        }

        if (!StrUtil.isAllCharMatch(uname, CharUtil::isLetterOrNumber)) {
            throw Err.illegalArgument("用户名只能包含字母和数字");
        }

        if (StrUtil.isBlank(nickName)) {
            this.nickName = uname;
        }

        if (this.nickName.length() >= 20) {
            throw Err.illegalArgument("昵称长度不能超过20");
        }
    }
}