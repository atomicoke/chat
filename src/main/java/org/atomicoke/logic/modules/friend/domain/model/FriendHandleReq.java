package org.atomicoke.logic.modules.friend.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.msg.domain.resp.NotifyResp;
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
    private Long requestId;

    /**
     * 操作结果 1:未操作 2:同意 3:拒绝
     * @see ChatConst.FriendAndGroupApplyResult
     */
    @NotNull(message = "处理结果不能为空！")
    private Integer handlerResult;


    public NotifyResp ofResp(Long requestId, Long toId, UserInfo userInfo) {
        Long seq = MessageSyncer.incrNotifySeq(String.valueOf(toId));
        final var resp = new NotifyResp();
        resp.setBoxOwnerId(String.valueOf(toId));
        resp.setBoxOwnerSeq(String.valueOf(seq));
        resp.setRequestId(String.valueOf(requestId));
        resp.setToId(String.valueOf(toId));
        resp.setContactType(ChatConst.Notify.Contact.handleFriend);
        resp.setHandlerTime(LocalDateTime.now());
        resp.setHandlerResult(this.getHandlerResult());
        NotifyResp.Message msg = new NotifyResp.Message();
        msg.setOperatorId(userInfo.getId());
        msg.setOperatorAvatar(userInfo.getAvatar());
        msg.setOperatorNickName(userInfo.getNickName());
        resp.setMessage(msg);
        return resp;
    }
}