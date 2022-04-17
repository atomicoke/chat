package io.github.fzdwx.inf.common.contants;


import io.github.fzdwx.logic.domain.entity.ChatLog;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/5 18:52
 */
public interface ChatConst {

    /**
     * @see ChatLog#getSessionType()
     */
    interface SessionType {

        /**
         * 私聊
         */
        int personal = 1;

        String personalStr = "1";

        /**
         * 群聊/频道
         */
        int group = 2;

        String groupStr = "2";

        /**
         * 发送给所有人
         */
        int broadcast = 3;
    }

    /**
     * @see ChatLog#getMsgFrom()
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

    interface TurnPageType {

        /**
         * 下一页
         */
        Integer down = 1;
        /**
         * 上一页
         */
        Integer up = 2;
    }

    interface LeftOrRight {

        Integer left = 1;
        Integer right = 2;
    }
}