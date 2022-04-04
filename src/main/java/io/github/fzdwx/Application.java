package io.github.fzdwx;

import cn.org.atool.fluent.mybatis.spring.FluentMybatisAutoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/3 16:06
 */
@SpringBootApplication(exclude = FluentMybatisAutoConfig.class)
public class Application {

    public static void main(String[] args) throws InterruptedException {
        final var context = SpringApplication.run(Application.class);
    }
}