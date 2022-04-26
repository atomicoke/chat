package org.atomicode.logic.modules.chathistory.domain.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.atomicode.logic.modules.chathistory.domain.entity.ChatHistory;

/**
 * @author 98065
 * @description 针对表【chat_log(聊天记录)】的数据库操作Mapper
 * @createDate 2022-04-10 13:16:13
 * @Entity generator.domain.ChatLog
 */
public interface ChatHistoryMapper extends BaseMapper<ChatHistory> {

    int saveIgnore(@Param("chatHistory") ChatHistory chatHistory);
}