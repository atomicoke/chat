package org.atomicode.inf.common.contants;


import org.atomicode.logic.modules.chathistory.domain.entity.ChatHistory;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/5 18:52
 */
public interface ChatConst {

    /**
     * @see ChatHistory#getSessionType()
     */
    interface SessionType {

        /**
         * 私聊,一对一的会话类型。聊天双方分别为两个用户和个体
         */
        int single = 1;

        String singleStr = "1";

        /**
         * 群聊/频道,群组会话类型，发送在这个会话的消息会分发到所有的群成员
         */
        int group = 2;

        String groupStr = "2";

        /**
         * 发送给所有人
         */
        int broadcast = 3;
    }

    /**
     * @see ChatHistory#getMsgFrom()
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