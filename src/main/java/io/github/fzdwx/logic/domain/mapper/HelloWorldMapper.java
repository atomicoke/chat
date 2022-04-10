package io.github.fzdwx.logic.domain.mapper;

import io.github.fzdwx.logic.domain.entity.HelloWorld;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 98065
* @description 针对表【hello_world(简单演示表)】的数据库操作Mapper
* @createDate 2022-04-10 13:16:13
* @Entity generator.domain.HelloWorld
*/
public interface HelloWorldMapper extends BaseMapper<HelloWorld> {

}