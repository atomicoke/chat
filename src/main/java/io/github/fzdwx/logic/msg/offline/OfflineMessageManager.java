package io.github.fzdwx.logic.msg.offline;

import cn.hutool.core.text.StrPool;
import io.github.fzdwx.inf.common.Assert;
import io.github.fzdwx.inf.middleware.redis.Redis;
import io.github.fzdwx.logic.contants.ChatConst;
import io.github.fzdwx.logic.msg.api.model.OfflineMsgCount;
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

    /**
     * 推
     *
     * @param recvUserId   接收消息的用户id
     * @param channelId    通道标识(根据sessionType区分 比如说发送消息的用户id，群聊的群id)
     * @param sessionType  会话类型
     * @param minMessageId 当前一组消息中最小的msg id
     * @param messageSize  当前一组消息的数量
     */
    public static void push(final String recvUserId, String channelId, String sessionType, Long minMessageId, int messageSize) {
        setMinMessageId(recvUserId, channelId, minMessageId);
        incrMessageSum(recvUserId, channelId, sessionType, messageSize);
    }

    /**
     * 获取离线消息数量
     *
     * @param recvUserId 接收消息的用户id
     * @return {@link OfflineMsgCount }
     */
    public static OfflineMsgCount getOfflineMsgCount(final String recvUserId) {
        final var personalKey = incrKey(recvUserId, ChatConst.SessionType.personalStr);
        final var groupKey = incrKey(recvUserId, ChatConst.SessionType.groupStr);

        final var personalMsgCntMap = Redis.hGetAll(personalKey);
        final var groupMsgCntMap = Redis.hGetAll(groupKey);

        return OfflineMsgCount.of(personalMsgCntMap, groupMsgCntMap);
    }

    /**
     * 获取离线消息最小的id
     *
     * @param recvUserId 接收消息的用户id
     * @param channelId  通道标识(根据sessionType区分 比如说发送消息的用户id，群聊的群id)
     * @return {@link Long }
     */
    public static Long getOfflineMsgMinId(final String recvUserId, final String channelId) {
        String key = minIdKey(recvUserId);
        final var minId = Redis.hGet(key, channelId);
        Assert.notNull(minId, "minId is null");
        return Long.valueOf(minId);
    }

    /**
     * 清楚离线消息数量与最小的id
     *
     * @param recvUserId  接收消息的用户id
     * @param channelId   通道标识(根据sessionType区分 比如说发送消息的用户id，群聊的群id)
     * @param sessionType 会话类型
     */
    public static void cleanOfflineMsg(final String recvUserId, final String channelId, final String sessionType) {
        Redis.hDel(minIdKey(recvUserId), channelId);
        Redis.hDel(incrKey(recvUserId, sessionType), channelId);
    }

    private static void incrMessageSum(final String recvUserId, String channelId, String sessionType, int msgSize) {
        String key = incrKey(recvUserId, String.valueOf(sessionType));
        Redis.hIncr(key, channelId, msgSize);
    }

    private static void setMinMessageId(final String recvUserId, String channelId, final Long minId) {
        String key = minIdKey(recvUserId);

        // 当 msgId 为null 或 msgId > minMsgId 时，更新 msgId
        final String msgId = Redis.hGet(key, channelId);
        if (msgId == null || Long.parseLong(msgId) > minId) {
            Redis.hSet(key, channelId, minId.toString());
        }
    }


    /*
    key生成规则：
        如果是单聊：
            key: offline:msg:recvUserId:SessionType.personal
            filed: channelId | value:  min message id
        如果是群聊：
            key: offline:msg:recvUserId:SessionType.group
            filed: channelId | value:  min message id
     */
    private static String minIdKey(final String recvUserId) {
        return MIN_ID_KEY_PREFIX + recvUserId;
    }

    private static String incrKey(final String recvUserId, final String sessionType) {
        return MEG_SUM_KEY_PREFIX + recvUserId + StrPool.COLON + sessionType;
    }
}