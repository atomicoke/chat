package io.github.fzdwx.inf.exc;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 19:18
 */
public class VerifyException extends RuntimeException {

    public VerifyException() {
        super();
    }

    public VerifyException(String message) {
        super(message);
    }
}