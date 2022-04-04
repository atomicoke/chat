package io.github.fzdwx.helloworld;

import io.github.fzdwx.db.config.MybatisConfiguration;
import io.github.fzdwx.helloworld.entity.HelloWorldEntity;
import io.github.fzdwx.helloworld.mapper.HelloWorldMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 12:20
 */
@SpringBootTest
@ContextConfiguration(classes = {MybatisConfiguration.class, DataSourceAutoConfiguration.class})
public class HelloWorldTest {

    @Autowired
    private  HelloWorldMapper mapper;

    @Test
    void test1() {
        /**
         * 为了演示方便，先删除数据
         */
        mapper.delete(mapper.query()
                .where.id().eq(1L).end());
        /**
         * 插入数据
         */
        HelloWorldEntity entity = new HelloWorldEntity();
        entity.setId(1L);
        entity.setSayHello("hello world");
        entity.setYourName("fluent mybatis");
        entity.setIsDeleted(false);
        mapper.insert(entity);
        /**
         * 查询 id = 1 的数据
         */
        HelloWorldEntity result1 = mapper.findOne(mapper.query()
                .where.id().eq(1L).end());
        /**
         * 控制台直接打印出查询结果
         */
        System.out.println("1. HelloWorldEntity:" + result1.toString());
        /**
         * 更新id = 1的记录
         */
        mapper.updateBy(mapper.updater()
                .set.sayHello().is("say hello, say hello!")
                .set.yourName().is("fluent mybatis is powerful!").end()
                .where.id().eq(1L).end()
        );
        /**
         * 查询 id = 1 的数据
         */
        HelloWorldEntity result2 = mapper.findOne(mapper.query()
                .where.sayHello().like("hello")
                .and.isDeleted().eq(false).end()
                .limit(1)
        );
        /**
         * 控制台直接打印出查询结果
         */
        System.out.println("2. HelloWorldEntity:" + result2.toString());

    }
}