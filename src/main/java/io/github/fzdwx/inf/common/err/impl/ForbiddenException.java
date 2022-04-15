package io.github.fzdwx.inf.common.err.impl;

import io.github.fzdwx.inf.common.err.Err;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 23:11
 */
public class ForbiddenException extends Err {

    public ForbiddenException(final String message) {
        super(message);
    }
}