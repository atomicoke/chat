package io.github.fzdwx.logic.msg.api;

import io.github.fzdwx.inf.common.web.model.Rest;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import io.github.fzdwx.logic.msg.api.model.OfflineMsgCount;
import io.github.fzdwx.logic.msg.api.model.vo.PersonalOfflineMsgVO;
import io.github.fzdwx.logic.msg.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    /**
     * 获取单聊离线消息
     *
     * @param userInfo 用户信息
     * @param fromId   发送人 id
     */
    @GetMapping("getPersonalOfflineMsg/{fromId}")
    public Rest<PersonalOfflineMsgVO> getPersonalOfflineMsg(UserInfo userInfo,
                                                            @PathVariable final String fromId) {
        return Rest.of(messageService.getPersonalOfflineMsg(userInfo, fromId));
    }

    /**
     * 清理离线消息
     */
    @PostMapping("cleanOfflineMsg/{fromId}/{sessionType}")
    public Rest<Object> cleanOfflineMsg(UserInfo userInfo, @PathVariable final String fromId, @PathVariable final String sessionType) {
        return Rest.of(() -> messageService.cleanOfflineMsg(userInfo, fromId,sessionType));
    }
}