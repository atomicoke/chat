package org.atomicoke.logic.msg.domain.model;

import lombok.Data;
import org.atomicoke.inf.common.util.Json;

import java.io.Serializable;

/**
 * 通知
 *
 * @author oneIdler
 * @since 2022/4/29
 */
@Data
public class Message implements Serializable {

    /**
     * 当前信箱所属人id
     */
    private String boxOwnerId;

    /**
     * 当前信箱所属人 全局seq(每收到或发送一条消息则加1)
     */
    private String boxOwnerSeq;

    /**
     * 数据
     */
    private String data;

    /**
     * 通知类型
     */
    private String messageType;

    public String encode() {
        return Json.toJson(this);
    }
}