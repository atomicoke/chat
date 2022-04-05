package io.github.fzdwx.inf.common.web.model;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.function.Supplier;

/**
 * rest response.
 *
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 19:06
 */
public class Rest<OUT> extends LinkedHashMap<String, Object> {

    public static final String DATA = "data";
    public static final String CODE = "code";
    public static final String MESSAGE = "message";
    public static final String STACKTRACE = "stackTrace";

    public static final int SUCCESS = 0;
    public static final String SUCCESS_MESSAGE = "ok";
    public static final int FAILURE = 20001;
    public static final String FAILURE_MESSAGE = "failure";

    public static <OUT> Rest<OUT> failure(final HttpStatus status, final String message) {
        return create(null, status.value(), message);
    }

    public static <OUT> Rest<OUT> failure() {
        return create(null, FAILURE, FAILURE_MESSAGE);
    }

    public static <OUT> Rest<OUT> failure(String message) {
        return create(null, FAILURE, message);
    }

    public static <OUT> Rest<OUT> ok(OUT out) {
        return create(out, SUCCESS, SUCCESS_MESSAGE);
    }

    public static <OUT> Rest<OUT> ok(OUT out, String message) {
        return create(out, SUCCESS, message);
    }

    public static <OUT> Rest<OUT> ok(Supplier<OUT> sup) {
        return create(sup.get(), SUCCESS, SUCCESS_MESSAGE);
    }

    public static <OUT> Rest<OUT> ok(Supplier<OUT> sup, String message) {
        return create(sup.get(), SUCCESS, message);
    }

    public static <OUT> Rest<OUT> ok() {
        return create(null, SUCCESS, SUCCESS_MESSAGE);
    }

    public static <OUT> Rest<OUT> of(final OUT data) {
        if (data instanceof Boolean) {
            if (data == Boolean.TRUE) {
                return ok();
            } else failure();
        }
        return ok(data);
    }

    public static <OUT> Rest<OUT> create(OUT data, int code, String message) {
        Rest<OUT> rest = new Rest<>();
        if (data != null) {
            rest.put(DATA, data);
        }

        rest.put(CODE, code);
        rest.put(MESSAGE, message);
        return rest;
    }

    public Rest<OUT> stackTrace(StackTraceElement[] stackTrace) {
        // this.put(STACKTRACE, stackTrace[0]);
        this.put(STACKTRACE, ArrayUtil.sub(stackTrace,0,5));
        return this;
    }
}