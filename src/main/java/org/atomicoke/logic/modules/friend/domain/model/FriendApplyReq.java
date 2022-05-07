package org.atomicoke.logic.modules.friend.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.util.Time;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.modules.friend.domain.entity.FriendApply;
import org.atomicoke.logic.modules.msg.domain.resp.ContactMessageResp;

import java.io.Serializable;

/**
 * @author oneIdler
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


    public FriendApply ofEntity(Long userId) {
        FriendApply request = new FriendApply();
        request.setApplyMessage(this.getApplyMessage());
        request.setHandlerResult(ChatConst.FriendAndGroupApplyResult.unOperated);
        request.setCreateTime(Time.now());
        request.setAddWay(0);
        request.setApplyUserId(userId);
        request.setUserId(this.getToId());
        return request;
    }

    public ContactMessageResp ofResp(Long applyId, UserInfo userInfo) {
        final var resp = new ContactMessageResp();
        resp.setApplyId(String.valueOf(applyId));
        resp.setFromId(String.valueOf(ChatConst.Sys.SYS_ID));
        resp.setToId(String.valueOf(this.getToId()));
        resp.setContactType(ChatConst.Notify.Contact.applyFriend);
        resp.setMsgFrom(ChatConst.MsgFrom.SYS);
        resp.setHandlerTime(Time.now());
        resp.setHandlerResult(1);
        ContactMessageResp.Content msg = new ContactMessageResp.Content();
        msg.setOperatorId(userInfo.getId());
        msg.setApplyMessage(this.getApplyMessage());
        resp.setContent(msg);
        return resp;
    }
}