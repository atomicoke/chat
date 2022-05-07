package org.atomicoke.inf.middleware.db.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.apache.ibatis.reflection.MetaObject;
import org.atomicoke.inf.common.util.Time;
import org.atomicoke.inf.middleware.id.IdGenerate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 12:34
 */
@Configuration
public class MybatisConfiguration implements MetaObjectHandler {

    @Bean
    public IdentifierGenerator keyGenerator() {
        return entity -> IdGenerate.nextId();
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", Long.class, Time.now()); // 起始版本 3.3.0(推荐使用)
    }

    @Override
    public void updateFill(final MetaObject metaObject) {

    }
}