package io.github.fzdwx.logic.contants;

import io.github.fzdwx.logic.domain.entity.ChatLogEntity;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/5 18:52
 */
public interface ChatConst {

    /**
     * @see ChatLogEntity#getSessionType()
     */
    interface SessionType {

        /**
         * 私聊
         */
        Long personal = 1L;

        /**
         * 群聊/频道
         */
        Long group = 2L;

        /**
         * 发送给所有人
         */
        Long ALL = 3L;
    }

    /**
     * @see ChatLogEntity#getMsgFrom()
     */
    interface MsgFrom {

        /**
         * 用户
         */
        Integer USER = 1;

        /**
         * 系统
         */
        Integer SYS = 2;
    }
}