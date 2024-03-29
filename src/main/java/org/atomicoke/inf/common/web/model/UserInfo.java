package org.atomicoke.inf.common.web.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.atomicoke.inf.common.UserAvatarFixer;
import org.atomicoke.logic.modules.user.domain.entity.User;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 23:36
 */
@Data
@Accessors(chain = true)
public class UserInfo implements UserAvatarFixer {

    private String id;
    private String uname;
    private String nickName;
    private Long mobile;
    private Integer gender;
    private String roleKey;
    private String avatar;
    private Long idLong;

    public static UserInfo of(User userEntity) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(String.valueOf(userEntity.getId()));
        userInfo.setIdLong(userEntity.getId());
        userInfo.setUname(userEntity.getUname());
        userInfo.setNickName(userEntity.getNickName());
        userInfo.setMobile(userEntity.getMobile());
        userInfo.setRoleKey(userEntity.getRoleKey());
        userInfo.setGender(userEntity.getGender());
        userInfo.fixAvatar(userEntity.getAvatar());
        return userInfo;
    }

    public void avatar(final String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String avatar() {
        return this.avatar;
    }
}