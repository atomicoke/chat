package io.github.fzdwx.logic.modules.friend.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户发起好友申请表
 * @TableName friend_request
 */
@Data
@TableName(value ="friend_request")
public class FriendRequest implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 添加人
     */
    private Long reqId;

    /**
     * 被添加人
     */
    private Long userId;

    /**
     * 发送好友申请携带的申请信息
     */
    private String reqMessage;

    /**
     * 添加方式
     */
    private Integer addWay;

    /**
     * 操作结果 1:未读 2:已读 3:接收 4:拒绝
     */
    private Integer handlerResult;

    /**
     * 处理时间
     */
    private LocalDateTime handlerTime;

    /**
     * 拒绝可以携带msg给发起申请的人
     */
    private String handlerMsg;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}