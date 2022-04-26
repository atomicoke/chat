package org.atomicode.inf.common.err;

import org.atomicode.inf.common.err.impl.ForbiddenException;
import org.atomicode.inf.common.err.impl.NormalException;
import org.atomicode.inf.common.err.impl.NotFoundException;
import org.atomicode.inf.common.err.impl.VerifyException;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/5 8:49
 */
public class Err extends RuntimeException {

    public Err(final String message) {
        super(message);
    }

    public Err(final Exception e) {
        super(e);
    }

    public Err() {
        super();
    }

    public static ForbiddenException forbidden(String message) {
        return new ForbiddenException(message);
    }

    public static VerifyException verify(String message) {
        return new VerifyException(message);
    }

    public static VerifyException verify(Exception e) {
        return new VerifyException(e);
    }

    public static NormalException normal(String message) {
        return new NormalException(message);
    }

    public static NotFoundException notFound(String message) {
        return new NotFoundException(message);
    }

    public static UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException();
    }

    public static void thrVerify(final String message) {
        throw verify(message);
    }
}