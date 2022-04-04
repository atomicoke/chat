package io.github.fzdwx.inf.web.model;

import java.util.LinkedHashMap;

/**
 * rest response.
 *
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 19:06
 */
public class Rest<OUT> extends LinkedHashMap<String, Object> {

    public static <OUT> Rest<OUT> failure() {
        return create(null, 20001, "failure");
    }

    public static <OUT> Rest<OUT> failure(String message) {
        return create(null, 20001, message);
    }

    public static <OUT> Rest<OUT> ok(OUT out) {
        return create(out, 0, "OK");
    }

    public static <OUT> Rest<OUT> ok() {
        return create(null, 0, "OK");
    }

    public static <OUT> Rest<OUT> create(OUT data, int code, String message) {
        Rest<OUT> rest = new Rest<>();
        if (data != null) {
            rest.put("data", data);
        }

        rest.put("code", code);
        rest.put("message", message);
        return rest;
    }

    public Rest<OUT> stackTrace(StackTraceElement[] stackTrace) {
        this.put("stackTrace", stackTrace);
        return this;
    }
}