package org.atomicoke.logic.modules.friend.service;

import io.github.fzdwx.lambada.Lang;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.Assert;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.util.Time;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.modules.friend.domain.dao.FriendApplyRepo;
import org.atomicoke.logic.modules.friend.domain.dao.FriendRepo;
import org.atomicoke.logic.modules.friend.domain.entity.Friend;
import org.atomicoke.logic.modules.friend.domain.entity.FriendApply;
import org.atomicoke.logic.modules.friend.domain.model.FriendApplyReq;
import org.atomicoke.logic.modules.friend.domain.model.FriendHandleReq;
import org.atomicoke.logic.modules.friend.domain.model.req.SyncFriendReq;
import org.atomicoke.logic.modules.friend.domain.model.vo.FriendInfoVO;
import org.atomicoke.logic.modules.msg.UserWsConn;
import org.atomicoke.logic.modules.msg.WsPacket;
import org.atomicoke.logic.modules.msg.domain.model.Message;
import org.atomicoke.logic.modules.msg.domain.resp.ContactMessageResp;
import org.atomicoke.logic.modules.msg.sync.MessageSyncer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socket.WebSocket;

import java.util.List;
import java.util.Objects;

/**
 * @author oneIdler
 * @since 2022/4/28
 */
@Slf4j
@Service
@AllArgsConstructor
public class FriendService {

    private FriendRepo friendDao;

    private FriendApplyRepo friendApplyDao;

    /**
     * 好友申请
     *
     * @param userInfo {@link UserInfo}
     * @param req      {@link FriendApplyReq}
     */
    @Transactional(rollbackFor = Exception.class)
    public void apply(UserInfo userInfo, FriendApplyReq req) {
        Assert.ensureFalse(friendDao.existFriend(userInfo.getIdLong(), req.getToId()), "已存在好友关系!");
        Assert.ensureFalse(Objects.equals(userInfo.getIdLong(), req.getToId()), "无法添加自己为好友!");
        boolean exist = friendApplyDao.existByResult(userInfo.getIdLong(), req.getToId());
        Assert.ensureFalse(exist, "请勿重复申请!");
        FriendApply apply = req.ofEntity(userInfo.getIdLong());
        if (!friendApplyDao.save(apply)) {
            return;
        }

        push(req.getToId(), req.ofResp(apply.getId(), userInfo));
    }

    /**
     * 申请处理
     *
     * @param userInfo {@link UserInfo}
     * @param req      {@link FriendHandleReq}
     */
    @Transactional(rollbackFor = Exception.class)
    public void handle(UserInfo userInfo, FriendHandleReq req) {
        if (!friendApplyDao.updateResult(req.getApplyId(), req.getHandlerResult())) {
            return;
        }

        // 好友请求的原申请人id为toId
        Long applyUserId = friendApplyDao.getApplyUserId(req.getApplyId());
        if (Lang.eq(req.getHandlerResult().intValue(), ChatConst.FriendAndGroupApplyResult.agree)) {
            List<Friend> friends = Friend.of(applyUserId, userInfo.getIdLong(), Time.now());
            friendDao.insertOrUpdate(friends);
        }

        push(applyUserId, req.ofResp(req.getApplyId(), applyUserId, userInfo));
    }

    /**
     * 删除好友
     *
     * @param userInfo {@link UserInfo}
     * @param friendId 好友id
     */
    public void delete(UserInfo userInfo, Long friendId) {
        friendDao.removeFriend(userInfo.getIdLong(), friendId);
    }

    /**
     * 获取好友信息
     *
     * @param ownerId  所有者id
     * @param friendId 朋友id
     * @return {@link FriendInfoVO }
     */
    public FriendInfoVO info(final Long ownerId, final Long friendId) {
        final var friendInfoVO = friendDao.info(ownerId, friendId);
        Assert.notNull(friendInfoVO, "好友不存在!");

        friendInfoVO.fixAvatar();
        return friendInfoVO;
    }

    public List<FriendInfoVO> sync(final SyncFriendReq req) {
        final var list = this.friendDao.sync(req);
        if (Lang.isEmpty(list)) {
            return list;
        }

        list.forEach(FriendInfoVO::fixAvatar);
        return list;
    }

    /**
     * 推送消息
     *
     * @param userId 目标用户id
     * @param resp   resp
     */
    private void push(final Long userId, final ContactMessageResp resp) {
        WebSocket conn = UserWsConn.get(userId);
        Message message = resp.toMessage(userId, MessageSyncer.incrSeq(userId));
        if (conn == null) {
            // TODO: 2022/4/23 离线推送
            log.warn("用户[{}]没有连接", userId);
        } else {
            conn.send(WsPacket.newNotifyPacket(message, ChatConst.Notify.contact).encode());
        }
        MessageSyncer.saveToMongo(message);
    }
}