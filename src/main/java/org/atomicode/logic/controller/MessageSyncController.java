package org.atomicode.logic.controller;

import org.atomicode.inf.common.web.model.Rest;
import org.atomicode.inf.common.web.model.UserInfo;
import org.atomicode.logic.msg.sync.MessageSyncer;
import org.atomicode.logic.msg.sync.model.MessageSyncReq;
import org.atomicode.logic.msg.sync.model.MessageSyncResp;
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