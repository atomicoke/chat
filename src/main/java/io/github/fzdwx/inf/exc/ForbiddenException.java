package io.github.fzdwx.inf.exc;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 23:11
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(final String message) {
        super(message);
    }
}