package io.github.fzdwx.logic.user.api;

import io.github.fzdwx.inf.common.web.Web;
import io.github.fzdwx.inf.common.web.model.Rest;
import io.github.fzdwx.inf.middleware.redis.lock.ApiIdempotent;
import io.github.fzdwx.logic.user.api.model.SingInReq;
import io.github.fzdwx.logic.user.api.model.SingUpReq;
import io.github.fzdwx.logic.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 22:25
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthApi {

    private final UserService userService;

    /**
     * 登录
     */
    @PostMapping("singIn")
    @ApiIdempotent
    public Rest<String> singIn(@RequestBody SingInReq req) {
        return Rest.of(userService.singIn(req));
    }

    /**
     * 登出
     */
    @PostMapping("singOut")
    public Rest<Object> singOut() {
        return Rest.of(Web::singOut);
    }

    /**
     * 注册
     */
    @ApiIdempotent
    @PostMapping("singUp")
    public Rest<String> singUp(@RequestBody SingUpReq singUpReq) {
        return Rest.of(userService.singUp(singUpReq));
    }
}