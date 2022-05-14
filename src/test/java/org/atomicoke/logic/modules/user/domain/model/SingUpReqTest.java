package org.atomicoke.logic.modules.user.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/5/9 11:20
 */
class SingUpReqTest {

    @Test
    void test_preCheck_has_hz() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SingUpReq().setUname("我草111111111")
                    .preCheck();
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new SingUpReq().setUname("***********")
                    .preCheck();
        });

        new SingUpReq().setUname("aaaaaaaaaa")
                .preCheck();

        new SingUpReq().setUname("1111111111111")
                .preCheck();
    }
}