package io.github.fzdwx.helloworld.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.LogicDelete;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.IEntity;
import io.github.fzdwx.db.config.EntityFiledSetter;
import lombok.Data;

import java.util.Date;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 12:46
 */
@Data
@FluentMybatis(defaults = EntityFiledSetter.class)
public class HelloWorldEntity implements IEntity {

    private static final long serialVersionUID = 1L;

    @TableId(auto = false)
    private Long id;

    private String sayHello;

    private String yourName;

    @TableField(insert = "now()")
    private Date gmtCreated;

    @TableField(insert = "now()",update = "now()")
    private Date gmtModified;

    @TableField(insert = "0")
    @LogicDelete
    private Boolean isDeleted;

    @Override
    public Class<? extends IEntity> entityClass() {
        return HelloWorldEntity.class;
    }
}