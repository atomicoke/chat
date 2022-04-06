package io.github.fzdwx.inf.common.web.model;

import cn.hutool.core.util.ArrayUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.function.Supplier;

/**
 * rest response.
 *
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 19:06
 */
public class Rest<OUT> extends ResponseEntity<Rest.Info<OUT>> {

    public static final String DATA = "data";
    public static final String MESSAGE = "message";
    public static final String STACKTRACE = "stackTrace";

    public static final String SUCCESS_MESSAGE = "ok";
    public static final String FAILURE_MESSAGE = "failure";

    public Rest(final HttpStatus status) {
        super(status);
    }

    public Rest(final Info<OUT> body, final HttpStatus status) {
        super(body, status);
    }

    public static <OUT> Rest<OUT> failure(final HttpStatus status, final String message) {
        return create(null, status, message);
    }

    public static <OUT> Rest<OUT> failure() {
        return create(null, HttpStatus.INTERNAL_SERVER_ERROR, FAILURE_MESSAGE);
    }

    public static <OUT> Rest<OUT> failure(String message, StackTraceElement[] stackTrace) {
        return create(null, HttpStatus.INTERNAL_SERVER_ERROR, message, stackTrace);
    }

    public static Rest<Object> failure(final HttpStatus status, final String message, final StackTraceElement[] stackTrace) {
        return create(null, status, message, stackTrace);
    }

    public static <OUT> Rest<OUT> failure(String message) {
        return create(null, HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    @NotNull
    public static <OUT> Rest<OUT> success(OUT out) {
        return create(out, HttpStatus.OK, SUCCESS_MESSAGE);
    }

    public static <OUT> Rest<OUT> success(OUT out, String message) {
        return create(out, HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static <OUT> Rest<OUT> success(Supplier<OUT> sup) {
        return create(sup.get(), HttpStatus.OK, SUCCESS_MESSAGE);
    }

    public static <OUT> Rest<OUT> success(Supplier<OUT> sup, String message) {
        return create(sup.get(), HttpStatus.OK, message);
    }

    public static <OUT> Rest<OUT> success() {
        return create(null, HttpStatus.OK, SUCCESS_MESSAGE);
    }

    public static <OUT> Rest<OUT> of(final OUT data) {
        if (data instanceof Boolean) {
            if (data == Boolean.TRUE) {
                return success();
            } else failure();
        }
        return success(data);
    }

    public static <OUT> Rest<OUT> of(final Runnable action) {
        action.run();
        return success();
    }

    public static <OUT> Rest<OUT> create(OUT data, HttpStatus status, String message) {
        final var outInfo = new Info<OUT>();
        if (data != null) {
            outInfo.put(DATA, data);
        }

        if (message != null) {
            outInfo.put(MESSAGE, message);
        }

        return new Rest<>(outInfo, status);
    }

    public static <OUT> Rest<OUT> create(OUT data, HttpStatus status, String message, StackTraceElement[] stackTrace) {
        final var outInfo = new Info<OUT>();
        if (data != null) {
            outInfo.put(DATA, data);
        }

        if (message != null) {
            outInfo.put(MESSAGE, message);
        }

        if (stackTrace != null) {
            outInfo.put(STACKTRACE, ArrayUtil.sub(stackTrace, 0, 5));
        }

        return new Rest<>(outInfo, status);
    }

    static class Info<OUT> extends LinkedHashMap<String, Object> {

    }
}