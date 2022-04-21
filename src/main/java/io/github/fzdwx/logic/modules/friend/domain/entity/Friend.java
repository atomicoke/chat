package io.github.fzdwx.logic.modules.friend.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户加的好友表
 * @TableName friend
 */
@Data
@TableName(value ="friend")
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
    private LocalDateTime addTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否逻辑删除
     */
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}