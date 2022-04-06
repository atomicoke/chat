package io.github.fzdwx.inf.common.event;

import cn.hutool.extra.spring.SpringUtil;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/6 14:57
 */
public interface Event {

    static void routing(Event event) {
        SpringUtil.publishEvent(event);
    }
}