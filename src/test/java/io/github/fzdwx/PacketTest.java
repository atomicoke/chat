package io.github.fzdwx;

import io.github.fzdwx.lambada.lang.StopWatch;
import io.github.fzdwx.logic.msg.ws.WsPacket;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/7 14:02
 */
public class PacketTest {

    @Test
    void test() {
        final var json = """
                {"type":"chat","fromId":1511197272837996544,"fromUname":"可能常何","fromAvatar":null,"toId":3,"content":"你好啊我操","contentType":1,"sessionType":3,"msgFrom":1,"sendTime":"2014-02-03 06:10:14"}""";
        for (int i = 0; i < 100; i++) {
            final var s = StopWatch.get("task-" + i);
            final var decode = WsPacket.decode(json);
            s.stopAndPrint();
        }
    }
}