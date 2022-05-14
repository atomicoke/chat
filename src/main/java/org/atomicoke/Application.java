package org.atomicoke;

import io.github.fzdwx.lambada.Console;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/3 16:06
 */
@Slf4j
@SpringBootApplication
@MapperScan("org.atomicoke.logic.modules.*.domain.dao.mapper")
public class Application {

    public static void main(String[] args) throws InterruptedException {
        log.info(Console.banner());
        final var context = SpringApplication.run(Application.class);
    }
}