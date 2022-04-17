package io.github.fzdwx.logic.msg.api.model;

import lombok.Data;

import java.util.Map;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/17 13:51
 */
@Data
public class OfflineMsgCount {

    /**
     * 群聊/渠道 的未读消息数
     */
    Map<String, String> group;

    /**
     * 私聊 的未读消息数
     */
    Map<String, String> personal;

    public static OfflineMsgCount of(final Map<String, String> personal, final Map<String, String> group) {
        final var res = new OfflineMsgCount();
        res.group = group;
        res.personal = personal;
        return res;
    }
}