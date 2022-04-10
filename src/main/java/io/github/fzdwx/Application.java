package io.github.fzdwx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/3 16:06
 */
@SpringBootApplication
@MapperScan("io.github.fzdwx.logic.domain.mapper")
public class Application {

    public static void main(String[] args) throws InterruptedException {
        final var context = SpringApplication.run(Application.class);
    }
}