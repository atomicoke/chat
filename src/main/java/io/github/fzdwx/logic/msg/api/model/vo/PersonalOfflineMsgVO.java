package io.github.fzdwx.logic.msg.api.model.vo;

import io.github.fzdwx.inf.middleware.minio.Minio;
import io.github.fzdwx.lambada.Lang;
import io.github.fzdwx.logic.contants.ChatConst;
import io.github.fzdwx.logic.domain.entity.User;
import lombok.Data;

import java.util.List;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/17 16:42
 */
@Data
public class PersonalOfflineMsgVO {

    private List<ListPersonalChatVO> msgList;

    private SenderInfo senderInfo;

    public static PersonalOfflineMsgVO create(final List<ListPersonalChatVO> chats, final User senderUser) {
        PersonalOfflineMsgVO vo = new PersonalOfflineMsgVO();
        vo.setMsgList(chats);
        chats.forEach(chat -> {
            if (Lang.eq(chat.getFromId(), senderUser.getId())) {
                chat.setLeftOrRight(ChatConst.LeftOrRight.left);
            }
            if (!Lang.eq(chat.getContentType().intValue(), ChatConst.ContentType.Text)) {
                chat.setContent(Minio.getAccessUrl(chat.getContent()));
            }
        });
        vo.setSenderInfo(SenderInfo.from(senderUser));

        return vo;
    }
}