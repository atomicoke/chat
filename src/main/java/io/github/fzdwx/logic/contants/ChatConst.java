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
        int personal = 1;

        /**
         * 群聊/频道
         */
        int group = 2;

        /**
         * 发送给所有人
         */
        int ALL = 3;
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

    interface ContentType {

        int Text = 1;

        int image = 2;

        /**
         * 音频
         */
        int audio = 3;

        int video = 4;

        int file = 5;
    }

}