package org.atomicoke.logic.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atomicoke.inf.common.web.model.Rest;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.modules.group.domain.model.GroupApplyReq;
import org.atomicoke.logic.modules.group.domain.model.GroupHandleReq;
import org.atomicoke.logic.modules.group.domain.model.GroupInviteReq;
import org.atomicoke.logic.modules.group.service.GroupService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 群
 *
 * @author oneIdler
 * @date 2022/4/17 22:32
 */
@RestController
@RequestMapping("group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    /**
     * 申请入群
     *
     * @param userInfo {@link UserInfo}
     * @param req      {@link GroupApplyReq}
     * @return rest
     */
    @PostMapping("apply")
    public Rest<?> apply(UserInfo userInfo, @Valid @RequestBody GroupApplyReq req) {
        return Rest.of(() -> groupService.apply(userInfo, req));
    }

    /**
     * 邀请入群
     *
     * @param userInfo {@link UserInfo}
     * @param req      {@link GroupInviteReq}
     * @return rest
     */
    @PostMapping("invite")
    public Rest<?> invite(UserInfo userInfo, @Valid @RequestBody GroupInviteReq req) {
        return Rest.of(() -> groupService.invite(userInfo, req));
    }

    /**
     * 入群处理
     *
     * @param userInfo {@link UserInfo}
     * @param req      {@link GroupHandleReq}
     * @return rest
     */
    @PostMapping("handle")
    public Rest<?> handle(UserInfo userInfo, @Valid @RequestBody GroupHandleReq req) {
        return Rest.of(() -> groupService.handle(userInfo, req));
    }

    /**
     * 退出群聊
     */
    @PostMapping("leave")
    public void leave() {
        // TODO: 2022/4/17 退出群聊
    }
}