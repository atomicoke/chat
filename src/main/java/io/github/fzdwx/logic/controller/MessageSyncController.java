package io.github.fzdwx.logic.controller;

import io.github.fzdwx.inf.common.web.model.Rest;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import io.github.fzdwx.logic.msg.sync.MessageSyncer;
import io.github.fzdwx.logic.msg.sync.model.MessageSyncReq;
import io.github.fzdwx.logic.msg.sync.model.MessageSyncResp;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/23 17:16
 */
@RestController
@RequestMapping("/message/sync")
public class MessageSyncController {

    @PostMapping
    public Rest<MessageSyncResp> sync(UserInfo userInfo, @RequestBody MessageSyncReq req) {
        req.setUserId(userInfo.getId());
        return Rest.of(MessageSyncer.sync(req));
    }
}