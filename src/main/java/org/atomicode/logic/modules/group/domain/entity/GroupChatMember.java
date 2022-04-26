package org.atomicode.logic.modules.group.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 群聊
 *
 * @TableName group_chat_member
 */
@TableName(value = "group_chat_member")
@Data
public class GroupChatMember implements Serializable {

    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 群聊id
     */
    private Long groupId;

    /**
     * 群成员id
     */
    private Long userId;

    /**
     * 群成员在该群的昵称
     */
    private String nickName;

    /**
     * 群成员角色 1:成员 2:管理员 3:群主
     */
    private Integer roleType;

    /**
     * 入群方法
     */
    private Integer addWay;

    /**
     * 入群时间
     */
    private LocalDateTime addTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}