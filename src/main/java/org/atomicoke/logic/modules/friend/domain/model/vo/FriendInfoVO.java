package org.atomicoke.logic.modules.friend.domain.model.vo;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.atomicoke.inf.common.UserAvatarFixer;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/5/1 14:00
 */
@Data
@Accessors(chain = true)
public class FriendInfoVO implements UserAvatarFixer {

    /**
     * 当前用户id
     */
    private String ownerId;

    /**
     * 好友id
     */
    private String friendId;

    /**
     * 好友昵称
     */
    private String friendNickName;

    /**
     * 好友头像
     */
    private String friendAvatar;

    /**
     * 好友性别
     */
    private Integer friendGender;

    /**
     * 好友手机号码
     */
    private Long friendMobile;

    /**
     * 当前用户对好友的备注
     */
    private String remark;

    /**
     * 添加方式
     */
    private Integer addWay;

    /**
     * 添加时间
     */
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JSONField(format = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime addTime;

    @Override
    public void avatar(final String avatar) {
        this.friendAvatar = avatar;
    }

    @Override
    public String avatar() {
        return friendAvatar;
    }
}