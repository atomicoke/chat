package io.github.fzdwx.logic.user.api.model;

import lombok.Data;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 23:29
 */
@Data
public class SingInAndUpResp {

    private String token;
    private Object id;


    public static SingInAndUpResp of(String token, Object id) {
        SingInAndUpResp resp = new SingInAndUpResp();
        resp.setToken(token);
        resp.setId(id);
        return resp;
    }
}