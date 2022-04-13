package io.github.fzdwx.inf.middleware.db.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import io.github.fzdwx.inf.middleware.id.IdGenerate;
import me.ahoo.cosid.IdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 12:34
 */
@Configuration
public class MybatisConfiguration {

    @Bean
    public IdentifierGenerator keyGenerator() {
        return entity -> IdGenerate.nextId();
    }
}