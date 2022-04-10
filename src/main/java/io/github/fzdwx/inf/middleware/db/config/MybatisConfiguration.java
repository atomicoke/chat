package io.github.fzdwx.inf.middleware.db.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 12:34
 */
@Configuration
public class MybatisConfiguration {

    private static final Snowflake id = IdUtil.getSnowflake();

    @Bean
    public IdentifierGenerator keyGenerator() {
        return entity -> id.nextId();
    }
}