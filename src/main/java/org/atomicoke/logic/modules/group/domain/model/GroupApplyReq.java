package org.atomicoke.logic.modules.group.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.util.Time;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.modules.group.domain.entity.GroupChatApply;
import org.atomicoke.logic.modules.msg.domain.resp.ContactMessageResp;

/**
 * @author oneIdler
 * @since 2022/4/28
 */
@Data
public class GroupApplyReq {
    /**
     * 群id
     */
    @NotNull(message = "群id不能为空！")
    private Long groupId;

    /**
     * 发起申请携带的申请信息
     */
    private String applyMessage;

    public GroupChatApply ofEntity(Long applyUserId) {
        GroupChatApply request = new GroupChatApply();
        request.setType(ChatConst.GroupApplyType.apply);
        request.setApplyUserId(applyUserId);
        request.setApplyMessage(this.getApplyMessage());
        request.setUserId(applyUserId);
        request.setHandlerResult(ChatConst.FriendAndGroupApplyResult.unOperated);
        request.setCreateTime(Time.now());
        request.setGroupId(this.getGroupId());
        return request;
    }

    public ContactMessageResp ofResp(Long requestId, Long toUserId, UserInfo userInfo) {
        final var resp = new ContactMessageResp();
        resp.setApplyId(String.valueOf(requestId));
        //todo 系统头像
        resp.setToId(String.valueOf(toUserId));
        resp.setContactType(ChatConst.Notify.Contact.applyGroup);
        resp.setHandlerTime(Time.now());
        resp.setHandlerResult(ChatConst.FriendAndGroupApplyResult.unOperated);
        ContactMessageResp.Content msg = new ContactMessageResp.Content();
        msg.setOperatorId(userInfo.getId());
        msg.setApplyMessage(this.getApplyMessage());
        resp.setContent(msg);
        return resp;
    }
}