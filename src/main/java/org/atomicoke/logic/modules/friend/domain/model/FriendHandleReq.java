package org.atomicoke.logic.modules.friend.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.msg.domain.resp.ContactNotifyResp;
import org.atomicoke.logic.msg.sync.MessageSyncer;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zhiyuan
 * @since 2022/4/28
 */
@Data
public class FriendHandleReq implements Serializable {

    /**
     * 好友申请id
     */
    @NotNull(message = "好友申请id不能为空！")
    private Long requestId;

    /**
     * 操作结果 2:同意 3:拒绝
     *
     * @see ChatConst.FriendAndGroupApplyResult
     */
    @NotNull(message = "处理结果不能为空！")
    private Integer handlerResult;


    public ContactNotifyResp ofResp(Long requestId, Long toId, UserInfo userInfo) {
        Long seq = MessageSyncer.incrNotifySeq(String.valueOf(toId));
        final var resp = new ContactNotifyResp();
        resp.setBoxOwnerId(String.valueOf(toId));
        resp.setBoxOwnerSeq(String.valueOf(seq));
        resp.setRequestId(String.valueOf(requestId));
        resp.setToId(String.valueOf(toId));
        resp.setContactType(ChatConst.Notify.Contact.handleFriend);
        resp.setHandlerTime(LocalDateTime.now());
        resp.setHandlerResult(this.getHandlerResult());
        ContactNotifyResp.Message msg = new ContactNotifyResp.Message();
        msg.setOperatorId(userInfo.getId());
        msg.setOperatorAvatar(userInfo.getAvatar());
        msg.setOperatorNickName(userInfo.getNickName());
        resp.setMessage(msg);
        return resp;
    }
}