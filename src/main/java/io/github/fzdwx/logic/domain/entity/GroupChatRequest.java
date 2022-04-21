package io.github.fzdwx.logic.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户发起加群申请表
 * @TableName group_chat_request
 */
@Data
@TableName(value ="group_chat_request")
public class GroupChatRequest implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 群id
     */
    private Long groupId;

    /**
     * 申请加入群的人
     */
    private Long reqId;

    /**
     * 发送加群申请携带的申请信息
     */
    private String reqMessage;

    /**
     * 操作结果 1:未操作 2:接收 3:拒绝
     */
    private Integer handlerResult;

    /**
     * 添加方式
     */
    private Integer addWay;

    /**
     * 处理时间
     */
    private Date handlerTime;

    /**
     * 拒绝可以携带msg给发起申请的人
     */
    private String handlerMsg;

    /**
     * 处理人
     */
    private Long handlerUser;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}