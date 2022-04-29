package org.atomicoke.logic.modules.friend.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.modules.friend.domain.entity.FriendRequest;
import org.atomicoke.logic.msg.domain.resp.ContactNotifyResp;
import org.atomicoke.logic.msg.sync.MessageSyncer;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zhiyuan
 * @since 2022/4/28
 */
@Data
public class FriendApplyReq implements Serializable {

    /**
     * 被添加人id
     */
    @NotNull(message = "被添加人id不能为空！")
    private Long toId;

    /**
     * 发起申请携带的申请信息
     */
    private String applyMessage;


    public FriendRequest ofEntity(Long userId) {
        FriendRequest request = new FriendRequest();
        request.setApplyMessage(this.getApplyMessage());
        request.setHandlerResult(ChatConst.FriendAndGroupApplyResult.unOperated);
        request.setCreateTime(LocalDateTime.now());
        request.setApplyId(userId);
        request.setUserId(this.getToId());
        return request;
    }

    public ContactNotifyResp ofResp(Long requestId, UserInfo userInfo) {
        Long seq = MessageSyncer.incrNotifySeq(String.valueOf(this.getToId()));
        final var resp = new ContactNotifyResp();
        resp.setBoxOwnerId(String.valueOf(toId));
        resp.setBoxOwnerSeq(String.valueOf(seq));
        resp.setRequestId(String.valueOf(requestId));
        resp.setFromId(String.valueOf(ChatConst.Sys.SYS_ID));
        resp.setFromUname(ChatConst.Sys.SYS_NAME);
        //todo 系统头像
        resp.setFromAvatar("");
        resp.setToId(String.valueOf(toId));
        resp.setContactType(ChatConst.Notify.Contact.applyFriend);
        resp.setMsgFrom(ChatConst.MsgFrom.SYS);
        resp.setHandlerTime(LocalDateTime.now());
        resp.setHandlerResult(1);
        ContactNotifyResp.Message msg = new ContactNotifyResp.Message();
        msg.setOperatorId(userInfo.getId());
        msg.setOperatorAvatar(userInfo.getAvatar());
        msg.setOperatorNickName(userInfo.getNickName());
        msg.setApplyMessage(this.getApplyMessage());
        resp.setMessage(msg);
        return resp;
    }
}
