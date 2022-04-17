package io.github.fzdwx.logic.msg.api.model.vo;

import io.github.fzdwx.inf.middleware.minio.Minio;
import io.github.fzdwx.logic.domain.entity.User;
import lombok.Data;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/17 16:43
 */
@Data
public class SenderInfo {

    private String id;
    private String uname;
    private String avatar;

    public static SenderInfo from(final User senderUser) {
        final var senderInfo = new SenderInfo();
        senderInfo.setId(String.valueOf(senderUser.getId()));
        senderInfo.setUname(senderUser.getUname());
        senderInfo.setAvatar(Minio.getPubAccessUrl(senderUser.getAvatar()));
        return senderInfo;
    }
}