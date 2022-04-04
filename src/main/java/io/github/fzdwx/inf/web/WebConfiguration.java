package io.github.fzdwx.inf.web;

import io.github.fzdwx.inf.exc.VerifyException;
import io.github.fzdwx.inf.web.model.Rest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 19:04
 */
@Configuration
@RestControllerAdvice
@Slf4j
public class WebConfiguration {

    @ExceptionHandler(Exception.class)
    public Rest<Object> Exception(Exception e) {
        log.error("", e);

        return Rest.failure(e.getClass().getSimpleName() + " : " + e.getMessage())
                .stackTrace(e.getStackTrace());
    }

    @ExceptionHandler(VerifyException.class)
    public Rest<Object> VerifyException(VerifyException e) {
        return Rest.failure(e.getClass().getSimpleName() + " : " + e.getMessage())
                .stackTrace(e.getStackTrace());
    }
}