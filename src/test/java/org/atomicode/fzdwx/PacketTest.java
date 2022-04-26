package org.atomicode.fzdwx;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.http.HttpUtil;
import org.atomicode.fzdwx.lambada.lang.StopWatch;
import org.atomicode.logic.msg.ws.WsPacket;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/7 14:02
 */
public class PacketTest {

    @Test
    void test() {
        final var json = """
                        {
                          "randomId": "4444",
                          "type": "chat",
                          "toId": "1511197272837996544",
                          "sessionType": "1",
                          "chatMessage": {
                            "content": "helasdlo",
                            "contentType": 1
                          }
                        }
                """;
        for (int i = 0; i < 100; i++) {
            final var s = StopWatch.get("task-" + i);
            final var decode = WsPacket.decode(json);
            s.stopAndPrint();
        }
    }

    @Test
    void test_image_bytearray() {
        final byte[] bytes = HttpUtil.createGet("http://114.132.249.192:9000/chat/1650189894000/3061283827420200961511616587868495872-65269574.jpg")
                .execute()
                .bodyBytes();
        final BufferedImage bufferedImage = ImgUtil.toImage(bytes);
        System.out.println(Arrays.toString(bytes));
    }
}