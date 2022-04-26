package org.atomicoke.inf.common.err.impl;

import org.atomicoke.inf.common.err.Err;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 23:11
 */
public class ForbiddenException extends Err {

    public ForbiddenException(final String message) {
        super(message);
    }
}