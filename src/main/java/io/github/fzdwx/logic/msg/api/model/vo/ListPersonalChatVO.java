package io.github.fzdwx.logic.msg.api.model.vo;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/17 16:35
 */
@Data
public class ListPersonalChatVO {

    private String id;
    private String content;
    private Integer contentType;
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private String sendTime;
    private Long fromId;
    private Long toId;
    private String fileName;

    /**
     * 在左侧还是在右侧
     */
    private Integer leftOrRight;
}