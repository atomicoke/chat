package org.atomicoke.logic.modules.user.domain.model.vo;

import lombok.Data;
import org.atomicoke.inf.common.UserAvatarFixer;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/5/1 14:25
 */
@Data
public class BasicInfoVO implements UserAvatarFixer {

    /**
     * 用户id
     */
    private String userId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 性别
     */
    private Integer gender;

    @Override
    public void avatar(final String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String avatar() {
        return avatar;
    }
}