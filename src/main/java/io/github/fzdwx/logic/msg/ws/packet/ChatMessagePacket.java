package io.github.fzdwx.logic.msg.ws.packet;

import io.github.fzdwx.inf.common.exc.Err;
import io.github.fzdwx.inf.common.web.model.UserInfo;
import io.github.fzdwx.inf.middleware.minio.Minio;
import io.github.fzdwx.lambada.Seq;
import io.github.fzdwx.logic.contants.ChatConst;
import io.github.fzdwx.logic.domain.entity.ChatLogEntity;
import io.github.fzdwx.logic.msg.ws.WsPacket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static io.github.fzdwx.lambada.Lang.eq;
import static io.github.fzdwx.logic.contants.ChatConst.ContentType.Text;
import static io.github.fzdwx.logic.contants.ChatConst.ContentType.audio;
import static io.github.fzdwx.logic.contants.ChatConst.ContentType.file;
import static io.github.fzdwx.logic.contants.ChatConst.ContentType.image;
import static io.github.fzdwx.logic.contants.ChatConst.ContentType.video;

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
     * @see io.github.fzdwx.logic.contants.ChatConst.SessionType
     */
    private int sessionType;

    /**
     * 发送时间
     */
    private Date sendTime;

    private List<ChatMessage> chatMessages;

    @Override
    public String type() {
        return type;
    }

    public Err prepare() {
        if (this.chatMessages == null || this.chatMessages.isEmpty()) {
            return Err.verify("chatMessages can not be null");
        }

        for (final ChatMessage chatMessage : chatMessages) {
            final Err err = chatMessage.prepare();
            if (err != null) {
                return err;
            }
        }

        return null;
    }

    public Collection<ChatLogEntity> toChatLogs(final UserInfo userInfo) {
        return Seq.of(this.chatMessages).map(c -> mapping(userInfo, c)).toList();
    }

    public int size() {
        return this.chatMessages.size();
    }

    private ChatLogEntity mapping(final UserInfo userInfo, final ChatMessage chatMessage) {
        return new ChatLogEntity()
                .setContent(chatMessage.getContent())
                .setContentType(chatMessage.getContentType())
                .setFromId(userInfo.getIdLong())
                .setMsgFrom(ChatConst.MsgFrom.USER)
                .setSendTime(this.sendTime)
                .setSessionType(this.sessionType)
                .setToId(Long.valueOf(this.toId))
                ;
    }

    @Data
    public static class ChatMessage {

        /**
         * 消息体
         */
        private String content;

        /**
         * 消息类型 e.g: text, image, audio, video, file
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

            this.content = Minio.upload(inputStream, fileName).getObjectName();
        }
    }
}