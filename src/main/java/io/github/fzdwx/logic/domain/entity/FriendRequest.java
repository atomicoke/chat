package io.github.fzdwx.logic.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户发起好友申请表
 * @TableName friend_request
 */
@TableName(value ="friend_request")
public class FriendRequest implements Serializable {
    /**
     * 
     */
    @TableId
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
    private Date handlerTime;

    /**
     * 拒绝可以携带msg给发起申请的人
     */
    private String handlerMsg;

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
     * 添加人
     */
    public Long getReqId() {
        return reqId;
    }

    /**
     * 添加人
     */
    public void setReqId(Long reqId) {
        this.reqId = reqId;
    }

    /**
     * 被添加人
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 被添加人
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 发送好友申请携带的申请信息
     */
    public String getReqMessage() {
        return reqMessage;
    }

    /**
     * 发送好友申请携带的申请信息
     */
    public void setReqMessage(String reqMessage) {
        this.reqMessage = reqMessage;
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
     * 操作结果 1:未读 2:已读 3:接收 4:拒绝
     */
    public Integer getHandlerResult() {
        return handlerResult;
    }

    /**
     * 操作结果 1:未读 2:已读 3:接收 4:拒绝
     */
    public void setHandlerResult(Integer handlerResult) {
        this.handlerResult = handlerResult;
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
        FriendRequest other = (FriendRequest) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getReqId() == null ? other.getReqId() == null : this.getReqId().equals(other.getReqId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getReqMessage() == null ? other.getReqMessage() == null : this.getReqMessage().equals(other.getReqMessage()))
            && (this.getAddWay() == null ? other.getAddWay() == null : this.getAddWay().equals(other.getAddWay()))
            && (this.getHandlerResult() == null ? other.getHandlerResult() == null : this.getHandlerResult().equals(other.getHandlerResult()))
            && (this.getHandlerTime() == null ? other.getHandlerTime() == null : this.getHandlerTime().equals(other.getHandlerTime()))
            && (this.getHandlerMsg() == null ? other.getHandlerMsg() == null : this.getHandlerMsg().equals(other.getHandlerMsg()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getReqId() == null) ? 0 : getReqId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getReqMessage() == null) ? 0 : getReqMessage().hashCode());
        result = prime * result + ((getAddWay() == null) ? 0 : getAddWay().hashCode());
        result = prime * result + ((getHandlerResult() == null) ? 0 : getHandlerResult().hashCode());
        result = prime * result + ((getHandlerTime() == null) ? 0 : getHandlerTime().hashCode());
        result = prime * result + ((getHandlerMsg() == null) ? 0 : getHandlerMsg().hashCode());
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
        sb.append(", reqId=").append(reqId);
        sb.append(", userId=").append(userId);
        sb.append(", reqMessage=").append(reqMessage);
        sb.append(", addWay=").append(addWay);
        sb.append(", handlerResult=").append(handlerResult);
        sb.append(", handlerTime=").append(handlerTime);
        sb.append(", handlerMsg=").append(handlerMsg);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}