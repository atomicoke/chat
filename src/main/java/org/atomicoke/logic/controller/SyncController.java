package org.atomicoke.logic.controller;

import org.atomicoke.inf.common.web.model.Rest;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.modules.msg.sync.MessageSyncer;
import org.atomicoke.logic.modules.msg.sync.model.MessageSyncReq;
import org.atomicoke.logic.modules.msg.sync.model.MessageSyncResp;
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
public class SyncController {

    @PostMapping
    public Rest<MessageSyncResp> syncMessage(UserInfo userInfo, @RequestBody MessageSyncReq req) {
        req.setUserId(userInfo.getId());
        return Rest.of(MessageSyncer.syncMessage(req));
    }
}