package io.github.fzdwx.helloworld;

import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 12:20
 */
public class HelloWorldTest {

    public static final String url = "jdbc:mysql://114.132.249.192:3306/chat?useUnicode=true&characterEncoding=utf-8&useSSL=false";


    @Test
    public void generate() throws Exception {
        FileGenerator.build(Empty.class);
    }

    @Tables(
            // 设置数据库连接信息
            url = url, username = "root", password = "qkq23123A2.ww%",
            // 设置entity类生成src目录, 相对于 user.dir
            srcDir = "src/main/java",
            // 设置entity类的package值
            basePack = "io.github.fzdwx.logic.user",
            // 设置dao接口和实现的src目录, 相对于 user.dir
            daoDir = "src/main/java",
            // 设置哪些表要生成Entity文件
            tables = {@Table(value = {
                    "chat_log",
                    "friend",
                    "friend_request",
                    "group_chat",
                    "group_chat_member",
                    "group_chat_request"
                    })}
    )
    static class Empty {
    }
}