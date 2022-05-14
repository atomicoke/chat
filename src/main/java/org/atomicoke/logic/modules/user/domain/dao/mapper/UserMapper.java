package org.atomicoke.logic.modules.user.domain.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.atomicoke.logic.modules.user.domain.entity.User;
import org.atomicoke.logic.modules.user.domain.model.req.SearchUserReq;
import org.atomicoke.logic.modules.user.domain.model.vo.BasicInfoVO;

import java.util.List;

/**
 * @author 98065
 * @description 针对表【user(简单演示表)】的数据库操作Mapper
 * @createDate 2022-04-10 13:16:13
 * @Entity generator.domain.User
 */
public interface UserMapper extends BaseMapper<User> {

    BasicInfoVO basicInfo(@Param("uname") String uname);

    List<BasicInfoVO> search(@Param("req") SearchUserReq req);
}