package io.github.fzdwx.logic.msg.sync.model;

import io.github.fzdwx.logic.msg.domain.resp.ChatMessageResp;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/23 17:50
 */
@Data
@RequiredArgsConstructor(staticName = "of")
public class MessageSyncResp {

    private final List<ChatMessageResp> messages;

    private final String maxSeq;
}