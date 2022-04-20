package io.github.fzdwx.logic.msg.ws.packet;

import io.github.fzdwx.inf.common.contants.ChatConst;
import io.github.fzdwx.inf.common.err.Err;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import io.github.fzdwx.inf.middleware.minio.Minio;
import io.github.fzdwx.lambada.Lang;
import io.github.fzdwx.logic.domain.entity.ChatHistory;
import io.github.fzdwx.logic.msg.ws.WsPacket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;

import static io.github.fzdwx.inf.common.contants.ChatConst.ContentType.Text;
import static io.github.fzdwx.inf.common.contants.ChatConst.ContentType.audio;
import static io.github.fzdwx.inf.common.contants.ChatConst.ContentType.file;
import static io.github.fzdwx.inf.common.contants.ChatConst.ContentType.image;
import static io.github.fzdwx.inf.common.contants.ChatConst.ContentType.video;
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
     * 接收者 id
     */
    private String toId;

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
    private Date sendTime;

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

        if (this.toId == null || this.toId.isEmpty()) {
            return Err.verify("toId can not be null");
        }

        if (this.sessionType == 0) {
            return Err.verify("sessionType can not be null");
        }

        if (this.sendTime == null) {
            this.sendTime = new Date();
        }

        return chatMessage.prepare();
    }

    public ChatHistory buildChatLog(final UserInfo userInfo) {
        return mapping(userInfo, this.chatMessage);
    }

    private ChatHistory mapping(final UserInfo userInfo, final ChatMessage chatMessage) {
        final ChatHistory log = new ChatHistory();
        log.setContent(chatMessage.getContent());
        log.setContentType(chatMessage.getContentType());
        log.setFromId(userInfo.getIdLong());
        log.setMsgFrom(this.msgFrom);
        log.setSendTime(this.sendTime);
        log.setSessionType(this.sessionType);
        log.setToId(Long.valueOf(this.toId));

        if (!Lang.eq(chatMessage.getContentType(), Text)) {
            log.setFileName(chatMessage.getFileName());
            log.setFileSize(chatMessage.getAttrByte().length);
        }
        return log;
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

        /**
         * 附件如果是文件
         */
        private byte[] attrByte;

        private String fileName;

        public Err prepare() {
            if (eq(contentType, Text)) {
                if (content == null) {
                    throw Err.verify("content is null");
                }
            } else if (contentType == 0) {
                throw Err.verify("contentType is null");
            } else {
                if (attrByte == null || attrByte.length == 0) {
                    throw Err.verify("attrByte is null");
                }

                if (fileName == null) {
                    throw Err.verify("fileName is null");
                }

                try {
                    doUpload();
                } catch (Exception e) {
                    log.error("upload file error", e);
                    return Err.verify("upload file error : " + e.getMessage());
                }
            }

            return null;
        }

        private void doUpload() throws IOException {
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(attrByte);

            switch (contentType) {
                case image -> {
                    Minio.checkImage(inputStream);
                }
                case audio -> {
                    throw Err.verify("audio not support");
                    // TODO: 2022/4/9 上传音频
                }
                case video -> {
                    // TODO: 2022/4/9 上传视频
                    throw Err.verify("video is not support");
                }
                case file -> {
                    // TODO: 2022/4/9 上传文件
                    throw Err.verify("file is not support");
                }
            }

            // todo 文件格式
            this.content = Minio.uploadPrivate(inputStream, fileName, MediaType.APPLICATION_OCTET_STREAM_VALUE).getObjectName();
        }
    }
}