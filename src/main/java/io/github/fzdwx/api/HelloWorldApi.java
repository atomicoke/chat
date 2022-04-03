package io.github.fzdwx.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/3 16:07
 */
@RestController
public class HelloWorldApi {

    @GetMapping("hello")
    public String hello() {
        return "hello world123123";
    }
}