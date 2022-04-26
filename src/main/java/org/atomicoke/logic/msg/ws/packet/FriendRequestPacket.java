package org.atomicoke.logic.msg.ws.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.logic.msg.ws.WsPacket;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/25 23:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class FriendRequestPacket extends WsPacket {

    @Override
    public String type() {
        return Type.friendRequest;
    }
}