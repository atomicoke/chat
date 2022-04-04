package io.github.fzdwx.logic.helloworld.api;

import cn.hutool.core.util.RandomUtil;
import io.github.fzdwx.inf.redis.Redis;
import io.github.fzdwx.inf.web.Web;
import io.github.fzdwx.logic.helloworld.dao.HelloWorldDao;
import io.github.fzdwx.logic.domain.entity.HelloWorldEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/3 16:07
 */
@RestController
@RequiredArgsConstructor
public class HelloWorldApi {

    private final HelloWorldDao helloWorldDao;

    @GetMapping("hello")
    public String hello() {
        Redis.set("hello","world");
        System.out.println(Web.getUserInfo());
        return "start success";
    }

    @GetMapping("get")
    public HelloWorldEntity get() {
        return helloWorldDao.getOne();
    }

    @GetMapping("save")
    public Object save() {
        final var entity = new HelloWorldEntity();
        entity.setSayHello(RandomUtil.randomString(8));
        entity.setYourName(RandomUtil.randomString(8));
        return helloWorldDao.save(entity);
    }

    @GetMapping("delete/{id}")
    public Object delete(@PathVariable final String id) {
        return this.helloWorldDao.logicDeleteById(id);
    }
}