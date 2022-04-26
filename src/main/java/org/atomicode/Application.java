package org.atomicode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/3 16:06
 */
@SpringBootApplication
@MapperScan("org.atomicode.logic.modules.*.domain.dao.mapper")
public class Application {

    public static void main(String[] args) throws InterruptedException {
        final var context = SpringApplication.run(Application.class);
    }
}