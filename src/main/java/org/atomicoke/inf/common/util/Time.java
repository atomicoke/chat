package org.atomicoke.inf.common.util;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/5/5 11:39
 */
public interface Time {

    static Long now() {
        return System.currentTimeMillis();
    }
}