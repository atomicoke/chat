package org.atomicoke.logic.modules.msg.domain.resp;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.logic.modules.msg.domain.model.Message;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JSONField(format = DatePattern.NORM_DATETIME_PATTERN)
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