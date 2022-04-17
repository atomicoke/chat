package io.github.fzdwx.logic.msg.service;

import io.github.fzdwx.inf.common.Assert;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import io.github.fzdwx.logic.domain.dao.ChatLogRepo;
import io.github.fzdwx.logic.domain.dao.UserRepo;
import io.github.fzdwx.logic.msg.api.model.ListPersonalChatReq;
import io.github.fzdwx.logic.msg.api.model.OfflineMsgCount;
import io.github.fzdwx.logic.msg.api.model.vo.PersonalOfflineMsgVO;
import io.github.fzdwx.logic.msg.offline.OfflineMessageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/17 13:08
 */
@Service
@RequiredArgsConstructor
public class MessageService {

    private final ChatLogRepo chatLogRepo;
    private final UserRepo userRepo;

    /**
     * 获取离线消息数量
     */
    public OfflineMsgCount getOfflineMsgCount(UserInfo userInfo) {
        Assert.notNull(userInfo, "用户未登录");

        return OfflineMessageManager.getOfflineMsgCount(userInfo.getId());
    }

    public PersonalOfflineMsgVO getPersonalOfflineMsg(final UserInfo userInfo, final String fromId) {
        Assert.notNull(userInfo, "用户未登录");

        final var senderUser = userRepo.getById(Long.valueOf(fromId));
        final var minId = OfflineMessageManager.getOfflineMsgMinId(userInfo.getId(), fromId);
        final var chats = chatLogRepo.listPersonal(ListPersonalChatReq.createUser(fromId, userInfo.getId(), minId));


        return PersonalOfflineMsgVO.create(chats, senderUser);
    }

    public void cleanOfflineMsg(final UserInfo userInfo, final String fromId, final String sessionType) {
        Assert.notNull(userInfo, "用户未登录");

        OfflineMessageManager.cleanOfflineMsg(userInfo.getId(), fromId,sessionType);

    }
}