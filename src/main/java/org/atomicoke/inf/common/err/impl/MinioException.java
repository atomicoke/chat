package org.atomicoke.inf.common.err.impl;

import org.atomicoke.inf.common.err.Err;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 19:20
 */
public class MinioException extends Err {

    public MinioException(final Exception e) {
        super(e);
    }
}