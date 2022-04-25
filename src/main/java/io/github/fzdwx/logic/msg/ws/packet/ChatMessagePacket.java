package io.github.fzdwx.logic.msg.ws.packet;

import io.github.fzdwx.inf.common.contants.ChatConst;
import io.github.fzdwx.inf.common.err.Err;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import io.github.fzdwx.lambada.Lang;
import io.github.fzdwx.logic.modules.chathistory.domain.entity.ChatHistory;
import io.github.fzdwx.logic.msg.ws.WsPacket;
import io.github.fzdwx.logic.msg.ws.packet.status.ReplayPacket;
import io.netty.channel.ChannelFuture;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

import static io.github.fzdwx.inf.common.contants.ChatConst.ContentType.Text;
import static io.github.fzdwx.lambada.Lang.eq;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/5 20:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ChatMessagePacket extends WsPacket {

    private String type = Type.chat;


    /**
     * 单聊时有效，接收者
     */
    private Long toId;

    /**
     * 接收者 id列表
     * 群聊时有效，为群聊中所有成员的id
     */
    private List<Long> toIdList;

    /**
     * 群聊时有效，为群聊的id
     */
    private Long groupId;

    /**
     * 会话类型
     *
     * @see ChatConst.SessionType
     */
    private int sessionType;

    /**
     * 消息发送者
     *
     * @see ChatConst.MsgFrom
     */
    private int msgFrom;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 聊天信息
     */
    private ChatMessage chatMessage;

    @Override
    public String type() {
        return type;
    }

    public Err prepare() {
        if (this.randomId == null) {
            return Err.verify("randomId is null");
        }

        if (this.chatMessage == null) {
            return Err.verify("chatMessage can not be null");
        }


        if (this.sessionType == 0) {
            return Err.verify("sessionType can not be null");
        }

        if (eq(ChatConst.SessionType.group, this.sessionType)) {
            if (this.toIdList == null || this.toIdList.isEmpty() || this.groupId == null) {
                return Err.verify("groupId can not be null");
            }
        }

        if (eq(ChatConst.SessionType.single, this.sessionType)
            && this.toId == null) {
            return Err.verify("toId can not be null");
        }

        if (this.sendTime == null) {
            this.sendTime = LocalDateTime.now();
        }

        return chatMessage.prepare();
    }

    public ChatHistory buildChatHistory(final UserInfo userInfo) {
        final ChatHistory log = new ChatHistory();
        log.setRandomId(this.randomId);
        log.setFromId(userInfo.getIdLong());
        log.setMsgFrom(this.msgFrom);
        log.setSendTime(this.sendTime);
        log.setSessionType(this.sessionType);
        log.setToId(this.toId);

        // chat message
        log.setContentType(chatMessage.getContentType());
        log.setContent(chatMessage.getContent());
        if (!Lang.eq(chatMessage.getContentType(), Text)) {
            log.setFileName(chatMessage.getFileName());
            log.setFileSize(chatMessage.getFileSize());
        }
        return log;
    }

    public ChannelFuture sendSuccess(final Long chatHistoryId) {
        return super.sendSuccess(chatHistoryId);
    }

    public ChannelFuture replay(final ReplayPacket.Data data) {
        return this.ws.send(newReplayChatPacket(data,this).encode());
    }

    @Data
    public static class ChatMessage {

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

        private int fileSize;

        private String fileName;

        public Err prepare() {
            if (eq(contentType, Text)) {
                if (content == null) {
                    throw Err.verify("content is null");
                }
            } else if (contentType == 0) {
                throw Err.verify("contentType is null");
            } else {
                if (fileSize == 0) {
                    throw Err.verify("fileSize is null");
                }

                if (fileName == null) {
                    throw Err.verify("fileName is null");
                }
            }

            return null;
        }
    }
}