package io.github.fzdwx.logic.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.fzdwx.logic.domain.entity.ChatHistory;
import org.apache.ibatis.annotations.Param;

/**
 * @author 98065
 * @description 针对表【chat_log(聊天记录)】的数据库操作Mapper
 * @createDate 2022-04-10 13:16:13
 * @Entity generator.domain.ChatLog
 */
public interface ChatLogMapper extends BaseMapper<ChatHistory> {

    void save(@Param("chatLog") ChatHistory chatLog);
}