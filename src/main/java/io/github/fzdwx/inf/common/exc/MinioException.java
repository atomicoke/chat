package io.github.fzdwx.inf.common.exc;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 19:20
 */
public class MinioException extends RuntimeException {

    public MinioException(final Exception e) {
        super(e);
    }
}