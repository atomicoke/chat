package org.atomicoke.logic.modules.msg.domain.resp;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.logic.modules.msg.domain.model.Message;

import java.time.LocalDateTime;

/**
 * @author oneIdler
 * @date 2022/4/10 17:20
 */
@Data
@Slf4j
@Accessors(chain = true)
public class ContactMessageResp implements MessageResp {

    private static final String type = ChatConst.Notify.contact;

    /**
     * 申请id
     */
    private String requestId;
    /**
     * 消息发送人id（此时默认为0表示系统通知）
     */
    private String fromId = String.valueOf(ChatConst.Sys.SYS_ID);
    /**
     * 消息发送人用户名
     */
    private String fromUname = ChatConst.Sys.SYS_NAME;
    /**
     * 消息发送人头像
     */
    //todo 系统头像
    private String fromAvatar;
    /**
     * 实际接收人id
     */
    private String toId;
    /**
     * @see ChatConst.Notify.Contact
     */
    private int contactType;

    /**
     * 消息发送者的类型 1:用户 2:系统
     */
    private int msgFrom = ChatConst.MsgFrom.SYS;

    /**
     * 操作时间
     */
    private LocalDateTime handlerTime;

    /**
     * 操作结果 1:未操作 2:同意 3:拒绝
     */
    private Integer handlerResult;

    /**
     * 消息
     */
    private Content content;

    @Override
    public String type() {
        return type;
    }

    @Data
    public static class Content {

        /**
         * 操作人id
         */
        private String operatorId;

        /**
         * 操作人头像
         */
        private String operatorAvatar;

        /**
         * 操作人昵称
         */
        private String operatorNickName;

        /**
         * 携带的申请信息
         */
        private String applyMessage;
    }

    public Message toMessage(Long boxOwnerId, Long boxOwnerSeq) {
        Message message = new Message();
        message.setBoxOwnerId(String.valueOf(boxOwnerId));
        message.setBoxOwnerSeq(String.valueOf(boxOwnerSeq));
        message.setMessageType(this.type());
        message.setBody(this);
        return message;
    }
}