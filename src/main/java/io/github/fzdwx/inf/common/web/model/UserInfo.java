package io.github.fzdwx.inf.common.web.model;

import io.github.fzdwx.inf.middleware.minio.Minio;
import io.github.fzdwx.logic.domain.entity.User;
import lombok.Data;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 23:36
 */
@Data
public class UserInfo {

    private String id;
    private String uname;
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
        userInfo.setMobile(userEntity.getMobile());
        userInfo.setRoleKey(userEntity.getRoleKey());
        userInfo.setGender(userEntity.getGender());
        if (userEntity.getAvatar() != null) {
            userInfo.setAvatar(Minio.getAccessUrl(userEntity.getAvatar()));
        }
        return userInfo;
    }
}