package org.atomicode.inf.middleware.minio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/11 22:27
 */
@SpringBootTest
class MinioTest {

    @Autowired
    Minio minio;

    @Test
    void test111() {
        final var accessUrl = Minio.getAccessUrl("1649232453000/1511616587868495872-65269574.jpg");
        System.out.println("http://114.132.249.192:9000/chat/1649232453000/1511616587868495872-65269574.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=root%2F20220411%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20220411T142601Z&X-Amz-Expires=3600&X-Amz-SignedHeaders=host&X-Amz-Signature=f0ad5217060a71a6a4a60566eeee496db510a5ef4ab9a9985c34b0abe81f05ea");
        System.out.println(accessUrl);
    }
}