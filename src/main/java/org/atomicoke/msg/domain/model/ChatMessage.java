package org.atomicoke.msg.domain.model;

import io.github.fzdwx.lambada.Lang;
import lombok.Data;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.err.Err;
import org.atomicoke.inf.middleware.minio.Minio;

@Data
public class ChatMessage {

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

    /**
     * 发送时间
     */
    private Long sendTime;

    public Err prepare() {
        if (Lang.eq(contentType, ChatConst.ContentType.Text)) {
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

    public void fixUrl() {
        if (!Lang.eq(contentType, ChatConst.ContentType.Text)) {
            this.content = Minio.getPubAccessUrl(this.content);
        }
    }
}