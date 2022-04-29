package org.atomicoke.logic.msg.sync.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/23 17:50
 */
@Data
@RequiredArgsConstructor(staticName = "of")
public class NotifySyncResp {

    private final List<String> notifies;

    private final String maxSeq;
}