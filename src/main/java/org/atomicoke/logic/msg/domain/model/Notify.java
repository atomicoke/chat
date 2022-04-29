package org.atomicoke.logic.msg.domain.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 通知
 *
 * @author oneIdler
 * @since 2022/4/29
 */
@Data
public class Notify<Data> implements Serializable {
    /**
     * 数据
     */
    private Data data;

    /**
     * 通知类型
     */
    private String notifyType;

    public static <Data> Notify<Data> of(Data data, String notifyType) {
        Notify<Data> notify = new Notify<>();
        notify.setData(data);
        notify.setNotifyType(notifyType);
        return notify;
    }
}
