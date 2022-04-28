package org.atomicoke.logic.modules.group.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.modules.group.domain.entity.GroupChatRequest;
import org.atomicoke.logic.msg.domain.resp.NotifyResp;
import org.atomicoke.logic.msg.sync.MessageSyncer;

import java.time.LocalDateTime;

/**
 * @author zhiyuan
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
        request.setCreateTime(LocalDateTime.now());
        request.setApplyId(applyId);
        request.setGroupId(this.getToId());
        return request;
    }

    public NotifyResp ofResp(Long requestId, Long toId, UserInfo userInfo) {
        Long seq = MessageSyncer.incrNotifySeq(String.valueOf(this.getToId()));
        final var resp = new NotifyResp();
        resp.setBoxOwnerId(String.valueOf(toId));
        resp.setBoxOwnerSeq(String.valueOf(seq));
        resp.setRequestId(String.valueOf(requestId));
        //todo 系统头像
        resp.setFromAvatar("");
        resp.setToId(String.valueOf(toId));
        resp.setContactType(ChatConst.Notify.Contact.applyGroup);
        resp.setHandlerTime(LocalDateTime.now());
        resp.setHandlerResult(ChatConst.FriendAndGroupApplyResult.unOperated);
        NotifyResp.Message msg = new NotifyResp.Message();
        msg.setOperatorId(userInfo.getId());
        msg.setOperatorAvatar(userInfo.getAvatar());
        msg.setOperatorNickName(userInfo.getNickName());
        resp.setMessage(msg);
        return resp;
    }
}
