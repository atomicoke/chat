package org.atomicoke.logic.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import lombok.RequiredArgsConstructor;
import org.atomicoke.inf.common.web.model.Rest;
import org.atomicoke.inf.common.web.model.RoleConstant;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.modules.user.domain.model.EditUserInfoReq;
import org.atomicoke.logic.modules.user.domain.model.req.SearchUserReq;
import org.atomicoke.logic.modules.user.domain.model.vo.BasicInfoVO;
import org.atomicoke.logic.modules.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/22 11:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * 搜索(只能根据uname 或 手机号 精确搜索)
     *
     * @param req req
     * @return {@link Rest }<{@link List }<{@link BasicInfoVO }>>
     */
    @GetMapping("search")
    public Rest<List<BasicInfoVO>> search(SearchUserReq req) {
        return Rest.of(userService.search(req));
    }

    /**
     * 获取所有用户信息
     *
     * @apiNote 暂时使用这个
     */
    @GetMapping("getAll")
    public Rest<List<UserInfo>> getAll() {
        return Rest.of(userService.getAllUser());
    }

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
    public Rest<Boolean> editSelfInfo(UserInfo userInfo, @RequestBody EditUserInfoReq req) {
        req.setId(userInfo.getIdLong());
        req.setRoleKey(null);
        return Rest.of(this.userService.editUserInfo(req));
    }

    /**
     * 获取当前登录用户的信息
     */
    @GetMapping("getUserInfo")
    public Rest<UserInfo> getUserInfo() {
        return Rest.of(userService.getUserInfo());
    }

    /**
     * 获取用户的基本信息
     *
     * @param uname uname
     * @return {@link Rest }<{@link BasicInfoVO }>
     */
    @GetMapping("basicInfo/{uname}")
    public Rest<BasicInfoVO> basicInfo(@PathVariable final String uname) {
        return Rest.of(userService.basicInfo(uname));
    }
}