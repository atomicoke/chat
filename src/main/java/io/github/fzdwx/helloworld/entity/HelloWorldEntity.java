package io.github.fzdwx.helloworld.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.base.IEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 12:46
 */
@Data
@FluentMybatis
public class HelloWorldEntity implements IEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String sayHello;

    private String yourName;

    private Date gmtCreated;

    private Date gmtModified;

    private Boolean isDeleted;

    @Override
    public Class<? extends IEntity> entityClass() {
        return HelloWorldEntity.class;
    }
}