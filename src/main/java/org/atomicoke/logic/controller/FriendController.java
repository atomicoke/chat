package org.atomicoke.logic.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atomicoke.inf.common.web.model.Rest;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.modules.friend.domain.model.FriendApplyReq;
import org.atomicoke.logic.modules.friend.domain.model.FriendHandleReq;
import org.atomicoke.logic.modules.friend.domain.model.vo.FriendInfoVO;
import org.atomicoke.logic.modules.friend.service.FriendService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 好友
 *
 * @author oneIdler
 * @date 2022/4/17 22:32
 */
@RestController
@RequestMapping("friend")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    /**
     * 好友申请
     *
     * @param userInfo {@link UserInfo}
     * @param req      {@link FriendApplyReq}
     * @return rest
     */
    @PostMapping("apply")
    public Rest<?> apply(UserInfo userInfo, @Valid @RequestBody FriendApplyReq req) {
        return Rest.of(() -> friendService.apply(userInfo, req));
    }

    /**
     * 好友申请处理
     *
     * @param userInfo {@link UserInfo}
     * @param req      {@link FriendApplyReq}
     * @return rest
     */
    @PostMapping("handle")
    public Rest<?> handle(UserInfo userInfo, @Valid @RequestBody FriendHandleReq req) {
        return Rest.of(() -> friendService.handle(userInfo, req));
    }

    /**
     * 删除好友
     *
     * @param userInfo {@link UserInfo}
     * @return rest
     */
    @PostMapping("delete")
    public Rest<?> delete(UserInfo userInfo) {
        //todo 删除好友
        return Rest.success();
    }

    /**
     * 获取好友信息
     *
     * @param friendId 好友id
     * @return {@link Rest }<{@link String }>
     */
    @GetMapping("info/{friendId}")
    public Rest<FriendInfoVO> info(UserInfo userInfo, @PathVariable final Long friendId) {
        return Rest.of(friendService.info(userInfo.getIdLong(),friendId));
    }
}