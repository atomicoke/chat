package io.github.fzdwx.inf.db.config;

import cn.hutool.core.util.IdUtil;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter;

import java.util.function.Supplier;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 13:30
 */
public interface EntityFiledSetter extends IDefaultSetter {

    @Override
    default Supplier<Object> pkGenerator(IEntity entity) {
        return IdUtil::getSnowflakeNextId;
    }
}