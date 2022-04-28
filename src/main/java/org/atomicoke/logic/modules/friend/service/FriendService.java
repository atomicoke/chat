package org.atomicoke.logic.modules.friend.service;

import lombok.AllArgsConstructor;
import org.atomicoke.inf.common.contants.ChatConst;
import org.atomicoke.inf.common.err.Err;
import org.atomicoke.logic.modules.friend.domain.dao.FriendRepo;
import org.atomicoke.logic.modules.friend.domain.dao.FriendRequestRepo;
import org.atomicoke.logic.modules.friend.domain.entity.Friend;
import org.atomicoke.logic.modules.friend.domain.entity.FriendRequest;
import org.atomicoke.logic.msg.ws.packet.sys.SysContactPacket;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhiyuan
 * @since 2022/4/28
 */
@Service
@AllArgsConstructor
public class FriendService {
    private FriendRepo friendDao;

    private FriendRequestRepo friendRequestDao;

    /**
     * 保存好友请求数据
     *
     * @param packet        {@link SysContactPacket}
     * @param requestUserId 请求发起人id
     * @return bool
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveFriendRequest(SysContactPacket packet, Long requestUserId) {
        FriendRequest friendRequest = SysContactPacket.buildFriendRequest(packet, requestUserId);
        switch (packet.getContactType()) {
            case ChatConst.ContactType.addFriend -> {
                boolean flag = friendRequestDao.saveIgnore(friendRequest);
                packet.setRequestId(friendRequest.getId());
                return flag;
            }
            case ChatConst.ContactType.agreeFriend -> {
                boolean flag = friendRequestDao.updateResult(packet.getRequestId(), packet.getSendTime(), ChatConst.FriendAndGroupApplyResult.agree);
                if (flag) {
                    Long applyId = friendRequestDao.getApplyId(packet.getRequestId());
                    List<Friend> friends = Friend.of(applyId, requestUserId, LocalDateTime.now());
                    friendDao.saveBatch(friends);
                }
                return flag;
            }
            case ChatConst.ContactType.rejectFriend -> {
                return friendRequestDao.updateResult(packet.getRequestId(), packet.getSendTime(), ChatConst.FriendAndGroupApplyResult.reject);
            }
            default -> throw Err.verify("未知的系统消息类型" + packet.getContactType());
        }

    }
}
