package org.atomicoke.logic.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 朋友
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/17 22:32
 */
@RestController
@RequestMapping("friend")
public class FriendController {

    @PostMapping("request")
    public void request() {
        // TODO: 2022/4/17 添加朋友
    }
}