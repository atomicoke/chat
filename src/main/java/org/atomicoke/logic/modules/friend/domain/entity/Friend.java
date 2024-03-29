package org.atomicoke.logic.modules.friend.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 用户加的好友表
 *
 * @TableName friend
 */
@Data
@Accessors(chain = true)
@TableName(value = "friend")
public class Friend implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 添加人
     */
    private Long ownerId;

    /**
     * 被添加人
     */
    private Long friendId;

    /**
     * 添加人给被添加人打的备注
     */
    private String remark;

    /**
     * 添加方式
     */
    private Integer addWay;

    /**
     * 添加时间
     */
    private Long addTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 是否逻辑删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public static List<Friend> of(Long userId1, String remark1, Long userId2, String remark2, Long time) {
        return List.of(new Friend()
                        .setOwnerId(userId1)
                        .setFriendId(userId2)
                        .setRemark(remark1)
                        .setAddWay(0)
                        .setAddTime(time)
                        .setUpdateTime(time),
                new Friend()
                        .setOwnerId(userId2)
                        .setFriendId(userId1)
                        .setRemark(remark2)
                        .setAddWay(0)
                        .setAddTime(time)
                        .setUpdateTime(time));

    }

}