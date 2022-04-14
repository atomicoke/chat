package io.github.fzdwx.logic.msg.ws.packet.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import io.github.fzdwx.lambada.Coll;
import io.github.fzdwx.logic.contants.ChatConst;
import io.github.fzdwx.logic.domain.entity.ChatLog;
import io.github.fzdwx.logic.msg.ws.packet.ChatMessagePacket;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/10 17:20
 */
@Data
@Slf4j
public class ChatMessageResp {

    private String fromId;
    private String fromUname;
    private String fromAvatar;
    private String toId;
    private int sessionType;
    private int msgFrom;
    private Date sendTime;
    private List<ChatMessage> chatMessages;

    @JsonIgnore
    private Long minMessageId;

    @Data
    public static class ChatMessage {

        /**
         * 消息id
         */
        private Long id;

        /**
         * 消息体
         */
        private String content;

        /**
         * 消息类型
         *
         * @see ChatConst.ContentType
         */
        private int contentType;

        private String fileName;

        private Integer fileSize;
    }

    public static ChatMessageResp from(final UserInfo userInfo, final ChatMessagePacket packet, final Collection<ChatLog> chatMessages) {
        final var resp = new ChatMessageResp();

        resp.setFromId(userInfo.getId());
        resp.setFromUname(userInfo.getUname());
        resp.setFromAvatar(userInfo.getAvatar());
        resp.setToId(packet.getToId());
        resp.setSessionType(packet.getSessionType());
        resp.setMsgFrom(packet.getMsgFrom());
        resp.setSendTime(packet.getSendTime());

        final List<ChatMessage> list = Coll.list();
        Long minMessageId = Long.MAX_VALUE;
        for (final ChatLog log : chatMessages) {
            list.add(log.toResp());
            if (log.getId() < minMessageId) {
                minMessageId = log.getId();
            }
        }

        resp.setMinMessageId(minMessageId);
        resp.setChatMessages(list);
        return resp;
    }
}