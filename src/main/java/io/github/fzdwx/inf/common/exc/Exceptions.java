package io.github.fzdwx.inf.common.exc;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/5 8:49
 */
public class Exceptions {

    public static ForbiddenException forbidden(String message) {
        return new ForbiddenException(message);
    }

    public static VerifyException verify(String message) {
        return new VerifyException(message);
    }

    public static VerifyException verify(Exception e) {
        return new VerifyException(e);
    }

    public static NotFoundException notFound(String message) {
        return new NotFoundException(message);
    }
}