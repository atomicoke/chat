package io.github.fzdwx.logic.user.api.model;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import io.github.fzdwx.lambada.Lang;
import io.github.fzdwx.logic.domain.entity.UserEntity;
import lombok.Data;

import java.nio.charset.StandardCharsets;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 22:28
 */
@Data
public class SingUpReq {

    private String uname;
    private String passwd;

    public UserEntity toEntity() {
        final var salt = RandomUtil.randomString(8);
        return new UserEntity().setUname(uname)
                .setSalt(salt)
                .setPasswd(SaSecureUtil.md5BySalt(passwd, salt));
    }

}