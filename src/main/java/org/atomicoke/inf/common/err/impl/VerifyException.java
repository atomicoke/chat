package org.atomicoke.inf.common.err.impl;

import org.atomicoke.inf.common.err.Err;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 19:18
 */
public class VerifyException extends Err {

    public VerifyException() {
        super();
    }

    public VerifyException(String message) {
        super(message);
    }

    public VerifyException(final Exception e) {
        super(e);
    }
}