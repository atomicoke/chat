package org.atomicoke.logic.msg.domain.resp;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.msg.ws.packet.sys.SysContactPacket;

import java.time.LocalDateTime;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/10 17:20
 */
@Data
@Slf4j
@Accessors(chain = true)
public class SysInfoResp {

    /**
     * 当前信箱所属人id
     */
    private String boxOwnerId;

    /**
     * 当前信箱所属人 全局seq(每收到或发送一条消息则加1)
     */
    private String boxOwnerSeq;

    /**
     * 消息id
     */
    private String messageId;
    /**
     * 消息发送人id
     */
    private String fromId;
    /**
     * 消息发送人用户名
     */
    private String fromUname;
    /**
     * 消息发送人头像
     */
    private String fromAvatar;
    /**
     * 实际接收人id
     */
    private String toId;
    /**
     * @see ChatConst.ContactType
     */
    private int contactType;
    private int msgFrom;
    private LocalDateTime sendTime;

    /**
     * 消息
     */
    private Message message;

    @Data
    public static class Message {

        /**
         * 申请人id
         */
        private String requestId;

        /**
         * 申请人头像
         */
        private String requestAvatar;

        /**
         * 申请人昵称
         */
        private String requestNickName;
    }


    public SysInfoResp copy(final Long boxOwnerId, final Long boxOwnerSeq) {
        final SysInfoResp chatMessageResp = new SysInfoResp();
        chatMessageResp.setBoxOwnerId(boxOwnerId.toString());
        chatMessageResp.setBoxOwnerSeq(boxOwnerSeq.toString());
        chatMessageResp.setMessageId(this.messageId);
        chatMessageResp.setFromId(this.fromId);
        chatMessageResp.setFromUname(this.fromUname);
        chatMessageResp.setFromAvatar(this.fromAvatar);
        chatMessageResp.setToId(this.toId);
        chatMessageResp.setContactType(this.contactType);
        chatMessageResp.setMsgFrom(this.msgFrom);
        chatMessageResp.setSendTime(this.sendTime);
        return chatMessageResp;
    }

    public static SysInfoResp from(SysContactPacket packet,
                                   Long messageId, Long toId,
                                   final UserInfo userInfo) {
        final var resp = new SysInfoResp();
        resp.setMessageId(String.valueOf(messageId));
        resp.setFromId(String.valueOf(ChatConst.Sys.SYS_ID));
        resp.setFromUname(ChatConst.Sys.SYS_NAME);
        //todo 系统头像
        resp.setFromAvatar("");
        resp.setToId(String.valueOf(toId));
        resp.setContactType(packet.getContactType());
        resp.setMsgFrom(packet.getMsgFrom());
        resp.setSendTime(packet.getSendTime());
        Message msg = new Message();
        msg.setRequestId(userInfo.getId());
        msg.setRequestAvatar(userInfo.getAvatar());
        msg.setRequestNickName(userInfo.getNickName());
        resp.setMessage(msg);
        return resp;
    }
}