package org.atomicoke.logic.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/21 15:58
 */
@Data
@ConfigurationProperties(prefix = "project")
public class ProjectProps {

    /**
     * randomId对应的chatHistoryId在redis中缓存的时间(秒)
     */
    private int randomIdToChatHistoryIdExpire = 30;
}