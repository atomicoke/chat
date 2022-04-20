package io.github.fzdwx.logic.msg.offline;

import cn.hutool.extra.spring.SpringUtil;
import io.github.fzdwx.inf.middleware.redis.Redis;
import io.github.fzdwx.logic.msg.domain.resp.ChatMessageResp;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/12 15:48
 */
@Component
public class OfflineMessageManager implements InitializingBean {

    // 未读消息最小的id
    private final static String MIN_ID_KEY_PREFIX = "msg:minId:";
    private final static String COLLECTION = "chat";

    private static MongoTemplate mongoTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        mongoTemplate = SpringUtil.getBean(MongoTemplate.class);
    }

    /**
     * 推
     *
     * @param resp       chat message resp
     * @param receiverId 实际接收者id
     */
    public static void push(final ChatMessageResp resp, final String receiverId) {
        resp.setReceiverId(receiverId);
        mongoTemplate.insert(resp, COLLECTION);
        // mongoTemplate.insert()
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
}