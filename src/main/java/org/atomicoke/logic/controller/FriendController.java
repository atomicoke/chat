package org.atomicoke.logic.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atomicoke.inf.common.web.model.Rest;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.modules.friend.domain.model.FriendApplyReq;
import org.atomicoke.logic.modules.friend.domain.model.FriendHandleReq;
import org.atomicoke.logic.modules.friend.domain.model.req.SyncFriendReq;
import org.atomicoke.logic.modules.friend.domain.model.vo.FriendInfoVO;
import org.atomicoke.logic.modules.friend.service.FriendService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 同步好友信息(如果传入updateTime,则只同步指定更新时间后的好友信息)
     *
     * @param userInfo 用户信息
     * @param req      req
     * @return {@link Rest }<{@link List }<{@link FriendInfoVO }>>
     */
    @PostMapping("sync")
    public Rest<List<FriendInfoVO>> sync(UserInfo userInfo, @RequestBody SyncFriendReq req) {
        req.setUserId(userInfo.getIdLong());

        return Rest.of(friendService.sync(req));
    }

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
    @DeleteMapping("delete/{friendId}")
    public Rest<?> delete(UserInfo userInfo, @PathVariable Long friendId) {
        return Rest.of(() -> friendService.delete(userInfo, friendId));
    }

    /**
     * 获取好友信息
     *
     * @param friendId 好友id
     * @return {@link Rest }<{@link String }>
     */
    @GetMapping("info/{friendId}")
    public Rest<FriendInfoVO> info(UserInfo userInfo, @PathVariable final Long friendId) {
        return Rest.of(friendService.info(userInfo.getIdLong(), friendId));
    }
}