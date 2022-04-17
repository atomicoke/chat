package io.github.fzdwx.logic.msg.api.model;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.fzdwx.logic.contants.ChatConst;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/17 16:10
 */
@Data
public class ListPersonalChatReq {

    private Long fromId;
    private Long toId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * @see io.github.fzdwx.logic.contants.ChatConst.SessionType
     */
    private Integer sessionType;

    /**
     * @see io.github.fzdwx.logic.contants.ChatConst.MsgFrom
     */
    private Integer msgFrom;

    /**
     * 消息id（以此条消息开始搜索后面的内容）
     */
    private Long id;

    /**
     * @see io.github.fzdwx.logic.contants.ChatConst.ContentType
     */
    private Integer contentType;

    /**
     * 翻页时使用,nextId
     */
    private Long nextId;

    /**
     * @see io.github.fzdwx.logic.contants.ChatConst.TurnPageType
     */
    private Integer turnPageType;

    /**
     * 发送时间筛选 开始时间
     */
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date startTime;

    /**
     * 发送时间筛选 结束时间
     */
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date endTime;

    /**
     * 请求多少条数据,最多50条
     */
    private Integer limit = 50;

    public static ListPersonalChatReq createUser(final String fromId, final String toId, final Long id) {
        ListPersonalChatReq listPersonalChatReq = new ListPersonalChatReq();
        listPersonalChatReq.setFromId(Long.valueOf(fromId));
        listPersonalChatReq.setToId(Long.valueOf(toId));
        listPersonalChatReq.setId(id);
        listPersonalChatReq.setSessionType(ChatConst.SessionType.personal);
        listPersonalChatReq.setMsgFrom(ChatConst.MsgFrom.USER);
        return listPersonalChatReq;
    }
}