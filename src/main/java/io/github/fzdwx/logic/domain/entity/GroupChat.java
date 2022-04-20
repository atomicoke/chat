package io.github.fzdwx.logic.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 群聊
 * @TableName group_chat
 */
@TableName(value ="group_chat")
@Data
public class GroupChat implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 群名
     */
    private String name;

    /**
     * 群头像
     */
    private String avatar;

    /**
     * 群简介
     */
    private String introduction;

    /**
     * 群公告
     */
    private String notification;

    /**
     * 群创建者
     */
    private Long creatorUserId;

    /**
     * 群类型
     */
    private Integer groupType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}