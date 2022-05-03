package org.atomicoke.logic.modules.group.domain.entity;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户发起加群申请表
 *
 * @TableName group_chat_request
 */
@Data
@TableName(value = "group_chat_request")
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
    private Long applyId;

    /**
     * 发送加群申请携带的申请信息
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
    private LocalDateTime handlerTime;

    /**
     * 处理人
     */
    private Long handlerUser;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JSONField(format = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime createTime;

    /**
     * 随机id(用于去重)
     */
    private String randomId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}