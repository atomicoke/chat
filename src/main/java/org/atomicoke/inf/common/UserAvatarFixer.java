package org.atomicoke.inf.common;

import org.atomicoke.inf.middleware.minio.Minio;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/5/1 14:08
 */
public interface UserAvatarFixer {

    default void fixAvatar() {
        fixAvatar(avatar());
    }

    default void fixAvatar(String avatar) {
        if (avatar != null) {
            avatar(Minio.getPubAccessUrl(avatar));
        }
    }

    void avatar(String avatar);

    String avatar();
}