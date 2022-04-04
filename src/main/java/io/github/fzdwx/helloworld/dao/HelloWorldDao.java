package io.github.fzdwx.helloworld.dao;

import io.github.fzdwx.helloworld.dao.base.HelloWorldBaseDao;
import io.github.fzdwx.helloworld.entity.HelloWorldEntity;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 13:33
 */
@Repository
public class HelloWorldDao extends HelloWorldBaseDao {

    public HelloWorldEntity getOne() {
        return mapper.findById(1510852611564998656L);
    }
}