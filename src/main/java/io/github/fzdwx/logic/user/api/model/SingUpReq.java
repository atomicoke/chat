package io.github.fzdwx.logic.user.api.model;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.core.util.RandomUtil;
import io.github.fzdwx.logic.domain.entity.UserEntity;
import lombok.Data;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 22:28
 */
@Data
public class SingUpReq {

    private String uname;
    private String passwd;
}