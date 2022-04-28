package org.atomicoke.logic.modules.group.domain.model;

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
public class GroupHandleReq implements Serializable {

    /**
     * 入群申请id
     */
    private Long requestId;

    @NotNull(message = "处理结果不能为空！")
    private Integer handlerResult;

    @NotNull(message = "处理人id不能为空！")
    private Long handlerUser;

    public NotifyResp ofResp(Long requestId, Long toId, UserInfo userInfo) {
        Long seq = MessageSyncer.incrNotifySeq(String.valueOf(toId));
        final var resp = new NotifyResp();
        resp.setBoxOwnerId(String.valueOf(toId));
        resp.setBoxOwnerSeq(String.valueOf(seq));
        resp.setRequestId(String.valueOf(requestId));
        resp.setToId(String.valueOf(toId));
        resp.setContactType(ChatConst.Notify.Contact.handleGroup);
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
