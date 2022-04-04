package io.github.fzdwx.logic.user.api.model;

import lombok.Data;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 22:46
 */
@Data
public class SingInReq {

    private String uname;
    private String passwd;
}