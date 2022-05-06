package org.atomicoke.logic.modules.group.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.util.Time;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.modules.msg.domain.resp.ContactMessageResp;

import java.io.Serializable;

/**
 * @author oneIdler
 * @since 2022/4/28
 */
@Data
public class GroupHandleReq implements Serializable {

    /**
     * 入群申请id
     */
    @NotNull(message = "入群申请id不能为空！")
    private Long applyId;

    /**
     * @see ChatConst.FriendAndGroupApplyResult
     */
    @NotNull(message = "处理结果不能为空！")
    private Integer handlerResult;

    public ContactMessageResp ofResp(Long requestId, Long toId, UserInfo userInfo) {
        final var resp = new ContactMessageResp();
        resp.setApplyId(String.valueOf(requestId));
        resp.setToId(String.valueOf(toId));
        resp.setContactType(ChatConst.Notify.Contact.handleGroup);
        resp.setHandlerTime(Time.now());
        resp.setHandlerResult(this.getHandlerResult());
        ContactMessageResp.Content msg = new ContactMessageResp.Content();
        msg.setOperatorId(userInfo.getId());
        resp.setContent(msg);
        return resp;
    }
}