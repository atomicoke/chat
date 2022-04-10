package io.github.fzdwx.logic.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户发起加群申请表
 * @TableName group_chat_request
 */
@TableName(value ="group_chat_request")
public class GroupChatRequest implements Serializable {
    /**
     * 
     */
    @TableId
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

    /**
     * 
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 群id
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * 群id
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * 申请加入群的人
     */
    public Long getReqId() {
        return reqId;
    }

    /**
     * 申请加入群的人
     */
    public void setReqId(Long reqId) {
        this.reqId = reqId;
    }

    /**
     * 发送加群申请携带的申请信息
     */
    public String getReqMessage() {
        return reqMessage;
    }

    /**
     * 发送加群申请携带的申请信息
     */
    public void setReqMessage(String reqMessage) {
        this.reqMessage = reqMessage;
    }

    /**
     * 操作结果 1:未操作 2:接收 3:拒绝
     */
    public Integer getHandlerResult() {
        return handlerResult;
    }

    /**
     * 操作结果 1:未操作 2:接收 3:拒绝
     */
    public void setHandlerResult(Integer handlerResult) {
        this.handlerResult = handlerResult;
    }

    /**
     * 添加方式
     */
    public Integer getAddWay() {
        return addWay;
    }

    /**
     * 添加方式
     */
    public void setAddWay(Integer addWay) {
        this.addWay = addWay;
    }

    /**
     * 处理时间
     */
    public Date getHandlerTime() {
        return handlerTime;
    }

    /**
     * 处理时间
     */
    public void setHandlerTime(Date handlerTime) {
        this.handlerTime = handlerTime;
    }

    /**
     * 拒绝可以携带msg给发起申请的人
     */
    public String getHandlerMsg() {
        return handlerMsg;
    }

    /**
     * 拒绝可以携带msg给发起申请的人
     */
    public void setHandlerMsg(String handlerMsg) {
        this.handlerMsg = handlerMsg;
    }

    /**
     * 处理人
     */
    public Long getHandlerUser() {
        return handlerUser;
    }

    /**
     * 处理人
     */
    public void setHandlerUser(Long handlerUser) {
        this.handlerUser = handlerUser;
    }

    /**
     * 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        GroupChatRequest other = (GroupChatRequest) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getReqId() == null ? other.getReqId() == null : this.getReqId().equals(other.getReqId()))
            && (this.getReqMessage() == null ? other.getReqMessage() == null : this.getReqMessage().equals(other.getReqMessage()))
            && (this.getHandlerResult() == null ? other.getHandlerResult() == null : this.getHandlerResult().equals(other.getHandlerResult()))
            && (this.getAddWay() == null ? other.getAddWay() == null : this.getAddWay().equals(other.getAddWay()))
            && (this.getHandlerTime() == null ? other.getHandlerTime() == null : this.getHandlerTime().equals(other.getHandlerTime()))
            && (this.getHandlerMsg() == null ? other.getHandlerMsg() == null : this.getHandlerMsg().equals(other.getHandlerMsg()))
            && (this.getHandlerUser() == null ? other.getHandlerUser() == null : this.getHandlerUser().equals(other.getHandlerUser()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getReqId() == null) ? 0 : getReqId().hashCode());
        result = prime * result + ((getReqMessage() == null) ? 0 : getReqMessage().hashCode());
        result = prime * result + ((getHandlerResult() == null) ? 0 : getHandlerResult().hashCode());
        result = prime * result + ((getAddWay() == null) ? 0 : getAddWay().hashCode());
        result = prime * result + ((getHandlerTime() == null) ? 0 : getHandlerTime().hashCode());
        result = prime * result + ((getHandlerMsg() == null) ? 0 : getHandlerMsg().hashCode());
        result = prime * result + ((getHandlerUser() == null) ? 0 : getHandlerUser().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", groupId=").append(groupId);
        sb.append(", reqId=").append(reqId);
        sb.append(", reqMessage=").append(reqMessage);
        sb.append(", handlerResult=").append(handlerResult);
        sb.append(", addWay=").append(addWay);
        sb.append(", handlerTime=").append(handlerTime);
        sb.append(", handlerMsg=").append(handlerMsg);
        sb.append(", handlerUser=").append(handlerUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}