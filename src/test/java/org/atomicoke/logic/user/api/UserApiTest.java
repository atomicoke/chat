package org.atomicoke.logic.user.api;

import org.atomicoke.inf.common.util.Json;
import org.atomicoke.inf.common.util.Time;
import org.atomicoke.logic.modules.msg.domain.resp.ContactMessageResp;
import org.atomicoke.logic.modules.msg.sync.MessageSyncer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/17 21:58
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserApiTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void test() throws Exception {
        mockMvc.perform(
                get("/user/getAll")
        ).andDo(print());
    }

    @Test
    void test1() {
        List<String> list = IntStream.range(0, 10)
                .mapToObj(i -> {
                    ContactMessageResp resp = new ContactMessageResp();
                    resp.setContactType(11001);
                    resp.setFromId("0");
                    resp.setApplyId("123123123");
                    resp.setHandlerTime(Time.now());
                    resp.setMsgFrom(2);
                    return Json.toJson(resp.toMessage(1111L, i + 1L));
                }).collect(Collectors.toList());
        MessageSyncer.saveToMongo(list);
    }
}