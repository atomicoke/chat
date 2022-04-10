package io.github.fzdwx.logic.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 群聊
 * @TableName group_chat
 */
@TableName(value ="group_chat")
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
     * 群名
     */
    public String getName() {
        return name;
    }

    /**
     * 群名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 群头像
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 群头像
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * 群简介
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * 群简介
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    /**
     * 群公告
     */
    public String getNotification() {
        return notification;
    }

    /**
     * 群公告
     */
    public void setNotification(String notification) {
        this.notification = notification;
    }

    /**
     * 群创建者
     */
    public Long getCreatorUserId() {
        return creatorUserId;
    }

    /**
     * 群创建者
     */
    public void setCreatorUserId(Long creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    /**
     * 群类型
     */
    public Integer getGroupType() {
        return groupType;
    }

    /**
     * 群类型
     */
    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
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
        GroupChat other = (GroupChat) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getAvatar() == null ? other.getAvatar() == null : this.getAvatar().equals(other.getAvatar()))
            && (this.getIntroduction() == null ? other.getIntroduction() == null : this.getIntroduction().equals(other.getIntroduction()))
            && (this.getNotification() == null ? other.getNotification() == null : this.getNotification().equals(other.getNotification()))
            && (this.getCreatorUserId() == null ? other.getCreatorUserId() == null : this.getCreatorUserId().equals(other.getCreatorUserId()))
            && (this.getGroupType() == null ? other.getGroupType() == null : this.getGroupType().equals(other.getGroupType()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getAvatar() == null) ? 0 : getAvatar().hashCode());
        result = prime * result + ((getIntroduction() == null) ? 0 : getIntroduction().hashCode());
        result = prime * result + ((getNotification() == null) ? 0 : getNotification().hashCode());
        result = prime * result + ((getCreatorUserId() == null) ? 0 : getCreatorUserId().hashCode());
        result = prime * result + ((getGroupType() == null) ? 0 : getGroupType().hashCode());
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
        sb.append(", name=").append(name);
        sb.append(", avatar=").append(avatar);
        sb.append(", introduction=").append(introduction);
        sb.append(", notification=").append(notification);
        sb.append(", creatorUserId=").append(creatorUserId);
        sb.append(", groupType=").append(groupType);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}