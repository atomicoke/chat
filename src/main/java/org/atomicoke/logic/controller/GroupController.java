package org.atomicoke.logic.controller;

import jakarta.validation.Valid;
import org.atomicoke.logic.modules.group.domain.model.GroupApplyReq;
import org.atomicoke.logic.modules.group.domain.model.GroupHandleReq;
import org.springframework.web.bind.annotation.PostMapping;
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
public class GroupController {

    @PostMapping("apply")
    public void apply(@Valid GroupApplyReq req) {
        // TODO: 2022/4/17 申请入群
    }

    @PostMapping("handle")
    public void handle(@Valid GroupHandleReq req) {
        // TODO: 2022/4/17 入群处理
    }

    @PostMapping("leave")
    public void leave() {
        // TODO: 2022/4/17 退出群聊
    }
}