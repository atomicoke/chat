package org.atomicode.inf.middleware.id;

import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.spring.SpringUtil;
import me.ahoo.cosid.IdGenerator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/13 22:36
 */
@Component
public class IdGenerate implements InitializingBean {

    private static IdGenerator idGenerator;

    @Override
    public void afterPropertiesSet() throws Exception {
        idGenerator = SpringUtil.getBean(IdGenerator.class);
    }

    public static long nextId() {
        return idGenerator.generate();
    }

    public static String nextIdStr() {
        return idGenerator.generateAsString();
    }

    public static String fastUuid() {
        return IdUtil.fastSimpleUUID();
    }
}