package org.atomicoke.logic.modules.friend.service;

import io.github.fzdwx.inf.msg.WebSocket;
import io.github.fzdwx.lambada.Lang;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.Assert;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.modules.friend.domain.dao.FriendRepo;
import org.atomicoke.logic.modules.friend.domain.dao.FriendRequestRepo;
import org.atomicoke.logic.modules.friend.domain.entity.Friend;
import org.atomicoke.logic.modules.friend.domain.entity.FriendRequest;
import org.atomicoke.logic.modules.friend.domain.model.FriendApplyReq;
import org.atomicoke.logic.modules.friend.domain.model.FriendHandleReq;
import org.atomicoke.logic.modules.friend.domain.model.req.SyncFriendReq;
import org.atomicoke.logic.modules.friend.domain.model.vo.FriendInfoVO;
import org.atomicoke.logic.modules.msg.UserWsConn;
import org.atomicoke.logic.modules.msg.WsPacket;
import org.atomicoke.logic.modules.msg.domain.resp.ContactMessageResp;
import org.atomicoke.logic.modules.msg.sync.MessageSyncer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author oneIdler
 * @since 2022/4/28
 */
@Slf4j
@Service
@AllArgsConstructor
public class FriendService {

    private FriendRepo friendDao;

    private FriendRequestRepo friendRequestDao;

    /**
     * 好友申请
     *
     * @param userInfo {@link UserInfo}
     * @param req      {@link FriendApplyReq}
     */
    @Transactional(rollbackFor = Exception.class)
    public void apply(UserInfo userInfo, FriendApplyReq req) {
        Assert.ensureFalse(friendDao.existFriend(userInfo.getId(), req.getToId()), "已存在好友关系!");

        FriendRequest request = req.ofEntity(userInfo.getIdLong());

        if (!friendRequestDao.saveIgnore(request)) {
            return;
        }

        push(req.getToId(), req.ofResp(request.getId(), userInfo));
    }

    /**
     * 申请处理
     *
     * @param userInfo {@link UserInfo}
     * @param req      {@link FriendHandleReq}
     */
    @Transactional(rollbackFor = Exception.class)
    public void handle(UserInfo userInfo, FriendHandleReq req) {
        // TODO 这里要判断apply_id 不能等于 userInfo.id
        if (!friendRequestDao.updateResult(req.getRequestId(), req.getHandlerResult())) {
            return;
        }

        // 好友请求的原申请人id为toId
        Long applyId = friendRequestDao.getApplyId(req.getRequestId());
        if (Lang.eq(req.getHandlerResult().intValue(), ChatConst.FriendAndGroupApplyResult.agree)) {
            List<Friend> friends = Friend.of(applyId, userInfo.getIdLong(), LocalDateTime.now());
            // TODO: 2022/4/30 如果已经曾经存在好友关系，删除好友时如何处理？ 是删除两份还是单独删除某一方？
            friendDao.saveBatch(friends);
        }

        push(applyId, req.ofResp(req.getRequestId(), applyId, userInfo));
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
     * @param applyId 申请人id
     * @param resp    resp
     */
    private void push(final Long applyId, final ContactMessageResp resp) {
        WebSocket conn = UserWsConn.get(applyId);
        if (conn == null) {
            // TODO: 2022/4/23 离线推送
            log.warn("用户[{}]没有连接", applyId);
        } else {
            conn.send(WsPacket.newNotifyPacket(resp).encode());
        }
        MessageSyncer.saveToMongo(resp.toMessage(applyId, MessageSyncer.incrSeq(applyId)));
    }
}