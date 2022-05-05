package org.atomicoke.logic.modules.group.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.util.Time;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.modules.group.domain.entity.GroupChatRequest;
import org.atomicoke.logic.modules.msg.domain.resp.ContactMessageResp;

import java.time.LocalDateTime;

/**
 * @author oneIdler
 * @since 2022/4/28
 */
@Data
public class GroupApplyReq {
    /**
     * 群id
     */
    @NotNull(message = "目标群id不能为空！")
    private Long toId;

    /**
     * 发起申请携带的申请信息
     */
    private String applyMessage;

    public GroupChatRequest ofEntity(Long applyId) {
        GroupChatRequest request = new GroupChatRequest();
        request.setApplyMessage(this.getApplyMessage());
        request.setHandlerResult(ChatConst.FriendAndGroupApplyResult.unOperated);
        request.setCreateTime(Time.now());
        request.setApplyId(applyId);
        request.setGroupId(this.getToId());
        return request;
    }

    public ContactMessageResp ofResp(Long requestId, Long toUserId, UserInfo userInfo) {
        final var resp = new ContactMessageResp();
        resp.setRequestId(String.valueOf(requestId));
        //todo 系统头像
        resp.setToId(String.valueOf(toUserId));
        resp.setContactType(ChatConst.Notify.Contact.applyGroup);
        resp.setHandlerTime(LocalDateTime.now());
        resp.setHandlerResult(ChatConst.FriendAndGroupApplyResult.unOperated);
        ContactMessageResp.Content msg = new ContactMessageResp.Content();
        msg.setOperatorId(userInfo.getId());
        msg.setApplyMessage(this.getApplyMessage());
        resp.setContent(msg);
        return resp;
    }
}