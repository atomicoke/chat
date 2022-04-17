package io.github.fzdwx.logic.msg.api;

import io.github.fzdwx.inf.common.web.model.Rest;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import io.github.fzdwx.logic.msg.api.model.OfflineMsgCount;
import io.github.fzdwx.logic.msg.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * message api.
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/14 17:40
 */
@RestController
@RequestMapping("msg")
@RequiredArgsConstructor
public class MessageApi {

    private final MessageService messageService;

    /**
     * 获取所有未读取的离线消息数量.
     */
    @GetMapping("getOfflineMsgCount")
    public Rest<OfflineMsgCount> getOfflineMsgCount(UserInfo userInfo) {
        return Rest.of(messageService.getOfflineMsgCount(userInfo));
    }
}