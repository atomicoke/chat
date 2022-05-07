package org.atomicoke.logic.modules.group.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户发起加群申请表
 *
 * @TableName group_chat_apply
 */
@Data
@TableName(value = "group_chat_apply")
public class GroupChatApply implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 请求类型 1:申请入群 2:邀请入群
     */
    private Integer type;

    /**
     * 群id
     */
    private Long groupId;

    /**
     * 申请/被邀请 入群的用户id
     */
    private Long userId;

    /**
     * 申请/邀请 入群的申请人id
     */
    private Long applyUserId;

    /**
     * 申请/邀请 入群携带的申请信息
     */
    private String applyMessage;

    /**
     * 操作结果 1:未操作 2:同意 3:拒绝
     */
    private Integer handlerResult;

    /**
     * 添加方式
     */
    private Integer addWay;

    /**
     * 处理时间
     */
    private Long handlerTime;

    /**
     * 处理人
     */
    private Long handlerUser;

    /**
     * 创建时间
     */
    private Long createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}