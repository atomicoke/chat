package org.atomicoke.logic.msg.sync.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.atomicoke.logic.msg.domain.model.Message;

import java.util.List;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/23 17:50
 */
@Data
@RequiredArgsConstructor(staticName = "of")
public class MessageSyncResp {

    private final List<Message> messages;

    private final String maxSeq;
}