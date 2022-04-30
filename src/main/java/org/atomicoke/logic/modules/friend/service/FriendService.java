package org.atomicoke.logic.modules.friend.service;

import io.github.fzdwx.inf.msg.WebSocket;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atomicoke.inf.common.Assert;
import org.atomicoke.inf.common.web.model.UserInfo;
import org.atomicoke.logic.modules.friend.domain.dao.FriendRepo;
import org.atomicoke.logic.modules.friend.domain.dao.FriendRequestRepo;
import org.atomicoke.logic.modules.friend.domain.entity.Friend;
import org.atomicoke.logic.modules.friend.domain.entity.FriendRequest;
import org.atomicoke.logic.modules.friend.domain.model.FriendApplyReq;
import org.atomicoke.logic.modules.friend.domain.model.FriendHandleReq;
import org.atomicoke.logic.msg.domain.resp.ContactMessageResp;
import org.atomicoke.logic.msg.sync.MessageSyncer;
import org.atomicoke.logic.msg.ws.UserWsConn;
import org.atomicoke.logic.msg.ws.WsPacket;
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
        FriendRequest request = req.ofEntity(userInfo.getIdLong());
        Assert.ensureTrue(friendDao.existFriend(userInfo.getId(), req.getToId()), "已存在好友关系!");
        boolean flag = friendRequestDao.saveIgnore(request);
        if (flag) {
            WebSocket conn = UserWsConn.get(req.getToId());
            ContactMessageResp resp = req.ofResp(request.getId(), userInfo);
            if (conn == null) {
                // TODO: 2022/4/23 离线推送
                log.warn("用户[{}]没有连接", req.getToId());
            } else {
                conn.send(WsPacket.newNotifyPacket(resp).encode());
            }
            MessageSyncer.saveToMongo(resp.toMessage(req.getToId(), MessageSyncer.incrSeq(String.valueOf(req.getToId()))));
        }
    }

    /**
     * 申请处理
     *
     * @param userInfo {@link UserInfo}
     * @param req      {@link FriendHandleReq}
     */
    @Transactional(rollbackFor = Exception.class)
    public void handle(UserInfo userInfo, FriendHandleReq req) {
        boolean flag = friendRequestDao.updateResult(req.getRequestId(), req.getHandlerResult());
        if (!flag) {
            return;
        }
        //好友请求的原申请人id为toId
        Long applyId = friendRequestDao.getApplyId(req.getRequestId());
        if (req.getHandlerResult() == 2) {
            List<Friend> friends = Friend.of(applyId, userInfo.getIdLong(), LocalDateTime.now());
            friendDao.saveBatch(friends);
        }
        ContactMessageResp resp = req.ofResp(req.getRequestId(), applyId, userInfo);
        WebSocket conn = UserWsConn.get(applyId);
        if (conn == null) {
            // TODO: 2022/4/23 离线推送
            log.warn("用户[{}]没有连接", applyId);
        } else {
            conn.send(WsPacket.newNotifyPacket(resp).encode());
        }
        MessageSyncer.saveToMongo(resp.toMessage(applyId, MessageSyncer.incrSeq(String.valueOf(applyId))));
    }
}
