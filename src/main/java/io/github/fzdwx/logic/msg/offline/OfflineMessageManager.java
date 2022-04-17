package io.github.fzdwx.logic.msg.offline;

import cn.hutool.core.text.StrPool;
import io.github.fzdwx.inf.middleware.redis.Redis;
import io.github.fzdwx.logic.contants.ChatConst;
import io.github.fzdwx.logic.msg.api.model.OfflineMsgCount;
import io.github.fzdwx.logic.msg.ws.packet.resp.ChatMessageResp;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/12 15:48
 */
@Component
public class OfflineMessageManager implements InitializingBean {

    // 未读消息最小的id
    private final static String MIN_ID_KEY_PREFIX = "msg:minId:";
    // 未读消息的数量
    private final static String MEG_SUM_KEY_PREFIX = "msg:sum:";

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public static void push(final ChatMessageResp chatMessageResp) {
        setMinMessageId(chatMessageResp);
        incrMessageSum(chatMessageResp);
    }

    public static OfflineMsgCount getOfflineMsgCount(final String toId) {
        final var personalKey = incrKey(toId, ChatConst.SessionType.personal);
        final var groupKey = incrKey(toId, ChatConst.SessionType.group);

        final var personalMsgCntMap = Redis.hGetAll(personalKey);
        final var groupMsgCntMap = Redis.hGetAll(groupKey);

        return OfflineMsgCount.of(personalMsgCntMap, groupMsgCntMap);
    }

    private static void incrMessageSum(final ChatMessageResp chatMessageResp) {
        String key = incrKey(chatMessageResp.getToId(), chatMessageResp.getSessionType());
        Redis.hIncr(key, chatMessageResp.getFromId(), chatMessageResp.getChatMessages().size());
    }

    private static void setMinMessageId(final ChatMessageResp chatMessageResp) {
        String key = minIdKey(chatMessageResp.getToId(), chatMessageResp.getSessionType());

        // 当 msgId 为null 或 msgId > minMsgId 时，更新 msgId
        final String msgId = Redis.hGet(key, chatMessageResp.getFromId());
        if (msgId == null || Long.parseLong(msgId) > chatMessageResp.getMinMessageId()) {
            Redis.hSet(key, chatMessageResp.getFromId(), chatMessageResp.getMinMessageId().toString());
        }
    }


    /*
    key生成规则：
        如果是单聊：
            key: offline:msg:userId:SessionType.personal
            filed: fromId | value:  min message id
        如果是群聊：
            key: offline:msg:groupId:SessionType.group
            filed: fromId | value:  min message id
     */
    private static String minIdKey(final String toId, final int sessionType) {
        return MIN_ID_KEY_PREFIX + toId + StrPool.COLON + sessionType;
    }

    private static String incrKey(final String toId, final int sessionType) {
        return MEG_SUM_KEY_PREFIX + toId + StrPool.COLON + sessionType;
    }

}