package org.atomicode.inf.common.err.impl;

import org.atomicode.inf.common.err.Err;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/5 8:50
 */
public class NotFoundException extends Err {

    public NotFoundException(final String message) {
        super(message);
    }
}