package org.atomicoke.logic.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.atomicoke.inf.common.web.model.Rest;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.modules.group.domain.model.GroupApplyReq;
import org.atomicoke.logic.modules.group.domain.model.GroupHandleReq;
import org.atomicoke.logic.modules.group.service.GroupService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 朋友
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/17 22:32
 */
@RestController
@RequestMapping("group")
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping("apply")
    public Rest<?> apply(UserInfo userInfo, @Valid @RequestBody GroupApplyReq req) {
        return Rest.of(() -> groupService.apply(userInfo, req));
    }

    @PostMapping("handle")
    public Rest<?> handle(UserInfo userInfo, @Valid @RequestBody GroupHandleReq req) {
        return Rest.of(() -> groupService.handle(userInfo, req));
    }

    @PostMapping("leave")
    public void leave() {
        // TODO: 2022/4/17 退出群聊
    }
}