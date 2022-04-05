package io.github.fzdwx.logic.domain.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import io.github.fzdwx.inf.middleware.db.config.EntityFiledSetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * GroupChatMemberEntity: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Data
@Accessors(
        chain = true
)
@EqualsAndHashCode(
        callSuper = false
)
@AllArgsConstructor
@NoArgsConstructor
@FluentMybatis(
        table = "group_chat_member",
        schema = "chat",
        defaults = EntityFiledSetter.class
)
public class GroupChatMemberEntity extends RichEntity {

    private static final long serialVersionUID = 1L;

    @TableId(
            value = "id",
            auto = false
    )
    private Long id;

    @TableField(
            value = "add_time",
            desc = "入群时间",
            insert = "now()"
    )
    private Date addTime;

    @TableField(
            value = "add_way",
            desc = "入群方法"
    )
    private Integer addWay;

    @TableField(
            value = "group_id",
            desc = "群聊id"
    )
    private Long groupId;

    @TableField(
            value = "nick_name",
            desc = "群成员在该群的昵称"
    )
    private String nickName;

    @TableField(
            value = "role_type",
            desc = "群成员角色 1:成员 2:管理员 3:群主"
    )
    private Integer roleType;

    @TableField(
            value = "user_id",
            desc = "群成员id"
    )
    private Long userId;

    @Override
    public final Class entityClass() {
        return GroupChatMemberEntity.class;
    }
}