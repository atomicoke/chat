package io.github.fzdwx.logic.user.api;

import cn.dev33.satoken.annotation.SaCheckRole;
import io.github.fzdwx.inf.common.web.Web;
import io.github.fzdwx.inf.common.web.model.Rest;
import io.github.fzdwx.inf.common.web.model.RoleConstant;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import io.github.fzdwx.logic.user.api.model.EditUserInfoReq;
import io.github.fzdwx.logic.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/5 9:05
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserApi {

    private final UserService userService;

    /**
     * 修改用户信息
     *
     * @apiNote 只有 admin 才能调用该接口
     */
    @PostMapping("editUserInfo")
    @SaCheckRole(RoleConstant.ADMIN)
    public Rest<Boolean> editUserInfo(@RequestBody EditUserInfoReq req) {
        return Rest.of(this.userService.editUserInfo(req));
    }

    /**
     * 修改自己的信息
     *
     * @apiNote 只能修改自己的信息
     */
    @PostMapping("editSelfInfo")
    public Rest<Boolean> editSelfInfo(@RequestBody EditUserInfoReq req) {
        req.setId(Long.parseLong(Web.getUserInfo().getId()));
        req.setRoleKey(null);
        return Rest.of(this.userService.editUserInfo(req));
    }

    /**
     * 获取用户信息
     */
    @GetMapping("getUserInfo")
    public Rest<UserInfo> getUserInfo() {
        return Rest.of(Web.getUserInfo());
    }
}