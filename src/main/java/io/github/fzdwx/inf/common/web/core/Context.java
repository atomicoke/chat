package io.github.fzdwx.inf.common.web.core;

import com.alibaba.ttl.TransmittableThreadLocal;
import io.github.fzdwx.inf.common.web.model.UserInfo;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 21:49
 */
public class Context {

    public static final ThreadLocal<Context> CONTEXT = new TransmittableThreadLocal<>() {
        @Override
        protected Context initialValue() {
            return new Context();
        }
    };
    private UserInfo user;
    private Object request;

    public static UserInfo user() {
        return CONTEXT.get().user;
    }

    public static void user(UserInfo user) {
        CONTEXT.get().user = user;
    }

    public static void clean() {
        CONTEXT.remove();
    }

    public static void request(final Object request) {
        CONTEXT.get().request = request;
    }

    public static Object request() {
        return CONTEXT.get().request;
    }
}