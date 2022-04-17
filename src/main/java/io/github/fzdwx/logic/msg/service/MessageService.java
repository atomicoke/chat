package io.github.fzdwx.logic.msg.service;

import io.github.fzdwx.inf.common.Assert;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import io.github.fzdwx.logic.msg.api.model.OfflineMsgCount;
import io.github.fzdwx.logic.msg.offline.OfflineMessageManager;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/17 13:08
 */
@Service
public class MessageService {

    /**
     * 获取离线消息数量
     */
    public OfflineMsgCount getOfflineMsgCount(UserInfo userInfo) {
        Assert.notNull(userInfo, "用户未登录");

        return OfflineMessageManager.getOfflineMsgCount(userInfo.getId());
    }
}