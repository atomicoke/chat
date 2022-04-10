package io.github.fzdwx.logic.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 群聊
 * @TableName group_chat_member
 */
@TableName(value ="group_chat_member")
public class GroupChatMember implements Serializable {
    /**
     * 
     */
    @TableId
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
    private Date addTime;

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
     * 群聊id
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * 群聊id
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * 群成员id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 群成员id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 群成员在该群的昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 群成员在该群的昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * 群成员角色 1:成员 2:管理员 3:群主
     */
    public Integer getRoleType() {
        return roleType;
    }

    /**
     * 群成员角色 1:成员 2:管理员 3:群主
     */
    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    /**
     * 入群方法
     */
    public Integer getAddWay() {
        return addWay;
    }

    /**
     * 入群方法
     */
    public void setAddWay(Integer addWay) {
        this.addWay = addWay;
    }

    /**
     * 入群时间
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * 入群时间
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
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
        GroupChatMember other = (GroupChatMember) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getNickName() == null ? other.getNickName() == null : this.getNickName().equals(other.getNickName()))
            && (this.getRoleType() == null ? other.getRoleType() == null : this.getRoleType().equals(other.getRoleType()))
            && (this.getAddWay() == null ? other.getAddWay() == null : this.getAddWay().equals(other.getAddWay()))
            && (this.getAddTime() == null ? other.getAddTime() == null : this.getAddTime().equals(other.getAddTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getNickName() == null) ? 0 : getNickName().hashCode());
        result = prime * result + ((getRoleType() == null) ? 0 : getRoleType().hashCode());
        result = prime * result + ((getAddWay() == null) ? 0 : getAddWay().hashCode());
        result = prime * result + ((getAddTime() == null) ? 0 : getAddTime().hashCode());
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
        sb.append(", userId=").append(userId);
        sb.append(", nickName=").append(nickName);
        sb.append(", roleType=").append(roleType);
        sb.append(", addWay=").append(addWay);
        sb.append(", addTime=").append(addTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}