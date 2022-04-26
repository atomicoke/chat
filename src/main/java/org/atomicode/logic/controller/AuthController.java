package org.atomicode.logic.controller;

import lombok.RequiredArgsConstructor;
import org.atomicode.inf.common.web.Web;
import org.atomicode.inf.common.web.model.Rest;
import org.atomicode.inf.middleware.redis.lock.ApiIdempotent;
import org.atomicode.logic.modules.user.domain.model.SingInReq;
import org.atomicode.logic.modules.user.domain.model.SingUpReq;
import org.atomicode.logic.modules.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/22 10:59
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

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