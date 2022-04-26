package org.atomicoke.logic.msg.ws.packet.sys;

import io.github.fzdwx.lambada.Lang;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.err.Err;
import org.atomicoke.logic.msg.ws.WsPacket;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author oneIdler
 * @since 2022/4/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class SysContactPacket extends WsPacket {

    private static final String type = Type.sysContact;

    /**
     * 添加好友或加群的 请求id
     */
    private Long requestId;

    /**
     * 接收者
     */
    private List<Long> toIdList;

    /**
     * 发起申请携带的申请信息
     */
    private String requestMessage;


    /**
     * 消息发送者类型 默认为系统消息
     *
     * @see ChatConst.MsgFrom
     */
    private int msgFrom = ChatConst.MsgFrom.SYS;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * @see ChatConst.ContactType
     */
    private int contactType;


    @Override
    public String type() {
        return type;
    }

    public Err prepare() {
        if (this.randomId == null) {
            return Err.verify("randomId is null");
        }

        if (this.contactType == 0) {
            return Err.verify("contactType can not be null");
        }

        if (Lang.isEmpty(toIdList)) {
            throw Err.verify("toIdList is empty");
        }

        if (this.sendTime == null) {
            this.sendTime = LocalDateTime.now();
        }
        return null;
    }
}