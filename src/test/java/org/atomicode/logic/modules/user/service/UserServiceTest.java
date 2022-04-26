package org.atomicode.logic.modules.user.service;

import cn.hutool.core.util.IdUtil;
import org.assertj.core.api.Assertions;
import org.atomicode.fzdwx.lambada.Coll;
import org.atomicode.inf.common.err.impl.VerifyException;
import org.atomicode.inf.common.web.Web;
import org.atomicode.inf.common.web.model.RoleConstant;
import org.atomicode.inf.common.web.model.UserInfo;
import org.atomicode.logic.modules.user.domain.dao.UserRepo;
import org.atomicode.logic.modules.user.domain.entity.User;
import org.atomicode.logic.modules.user.domain.model.EditUserInfoReq;
import org.atomicode.logic.modules.user.domain.model.SingInReq;
import org.atomicode.logic.modules.user.domain.model.SingUpReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/22 11:19
 */
@ExtendWith({MockitoExtension.class})
class UserServiceTest {

    @Mock
    UserRepo userRepo;

    @InjectMocks
    UserService userService;

    final User user = new User().setId(1L).setMobile(13789983260L).setRoleKey(RoleConstant.ADMIN).setUname("fzdwx").setPasswd("0c2030b06183ddbaa8e2c484e0716d6c").setSalt("giztuc6p").setAvatar("1650244646000/306358031329849344blob").setGender(1).setDelFlag(0);

    /**
     * 在测试之前
     */
    @BeforeEach
    public void beforeTest() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepo);
    }

    @Test
    @DisplayName("test:userService#getAllUser | 当数据库中没有用户时，查询所有用户返回空集合")
    void test_1() {
        when(userRepo.list()).thenReturn(null);

        assertEquals(userService.getAllUser(), Coll.list());
    }

    @Test
    @DisplayName("test:userService#getAllUser | 当数据库中有用户时，查询所有用户返回用户集合")
    void test_2() {
        when(userRepo.list()).thenReturn(Coll.list(user));

        assertEquals(userService.getAllUser().size(), 1);
    }

    @Test
    @DisplayName("test:userService#editUserInfo | 当用户不存在时")
    void test_3() {
        when(userRepo.findOne(anyLong())).thenReturn(null);

        final EditUserInfoReq req = new EditUserInfoReq();
        req.setId(user.getId());

        assertThrows(VerifyException.class, () -> {
            userService.editUserInfo(req);
        });
    }

    @Test
    @DisplayName("test:userService#editUserInfo | 当必传参数为空时")
    void test_4() {
        final EditUserInfoReq req = new EditUserInfoReq();
        req.setId(null);

        assertThrows(VerifyException.class, () -> {
            userService.editUserInfo(req);
        });
    }

    @Test
    @DisplayName("test:userService#editUserInfo | 编辑用户成功")
    void test_5() {
        when(userRepo.findOne(anyLong())).thenReturn(user);
        when(userRepo.updateById(any(User.class))).thenReturn(true);
        final EditUserInfoReq req = new EditUserInfoReq();
        req.setId(user.getId());

        assertTrue(userService.editUserInfo(req));
    }

    @Test
    @DisplayName("test:userService#editUserInfo | 编辑用户失败")
    void test_6() {
        when(userRepo.findOne(anyLong())).thenReturn(user);
        when(userRepo.updateById(any(User.class))).thenReturn(false);
        final EditUserInfoReq req = new EditUserInfoReq();
        req.setId(user.getId());

        assertFalse(userService.editUserInfo(req));
    }

    @Test
    @DisplayName("test:userService#getUserInfo | 获取用户信息-1")
    void test_7() {
        try (final MockedStatic<Web> web = mockStatic(Web.class)) {
            web.when(Web::getUserInfo).thenReturn(null);

            Assertions.assertThat(userService.getUserInfo()).isNull();
        }
    }

    @Test
    @DisplayName("test:userService#getUserInfo | 获取用户信息-2")
    void test_8() {
        try (final MockedStatic<Web> web = mockStatic(Web.class)) {
            web.when(Web::getUserInfo).thenReturn(new UserInfo().setIdLong(123213123L));

            assertEquals(userService.getUserInfo().getIdLong(), 123213123L);
        }
    }

    @Test
    @DisplayName("test:userService#singIn | 用户不存在")
    void test_9() {
        when(userRepo.findOne(user.getUname())).thenReturn(null);
        final SingInReq req = new SingInReq().setUname(user.getUname());

        assertThrows(VerifyException.class, () -> userService.singIn(req));
    }

    @Test
    @DisplayName("test:userService#singIn | 密码错误")
    void test_10() {
        when(userRepo.findOne(user.getUname())).thenReturn(user);
        final SingInReq req = new SingInReq().setUname(user.getUname()).setPasswd("123");

        assertThrows(VerifyException.class, () -> userService.singIn(req));
    }

    @Test
    @DisplayName("test:userService#singIn | 密码正确")
    void test_11() {
        final String mockToken = IdUtil.fastSimpleUUID();
        final SingInReq req = new SingInReq().setUname(user.getUname()).setPasswd("123456");

        try (final MockedStatic<Web> web = mockStatic(Web.class);) {
            when(userRepo.findOne(user.getUname())).thenReturn(user);
            web.when(() -> Web.doLogin(any(User.class))).thenReturn(mockToken);

            assertEquals(userService.singIn(req), mockToken);

            verify(userRepo, times(1)).findOne(user.getUname());
            web.verify(() -> Web.doLogin(any(User.class)), times(1));
        }
    }

    @Test
    @DisplayName("test:userService#singUp | 用户已存在")
    void test_12() {
        final SingUpReq req = new SingUpReq().setUname(user.getUname()).setPasswd("123456");

        when(userRepo.countWithUname(anyString())).thenReturn(1L);

        assertThrows(VerifyException.class, () -> userService.singUp(req));

        verify(userRepo, times(1)).countWithUname(anyString());
    }

    @Test
    @DisplayName("test:userService#singUp | 注册成功")
    void test_13() {
        final SingUpReq req = new SingUpReq().setUname(user.getUname()).setPasswd("123456");
        final String mockToken = IdUtil.fastSimpleUUID();

        try (final MockedStatic<Web> web = mockStatic(Web.class);) {
            when(userRepo.countWithUname(anyString())).thenReturn(0L);
            when(userRepo.save(any(User.class))).thenReturn(true);
            web.when(() -> Web.doLogin(any())).thenReturn(mockToken);

            assertEquals(mockToken, userService.singUp(req));

            verify(userRepo, times(1)).countWithUname(anyString());
            verify(userRepo, times(1)).save(any(User.class));
            web.verify(() -> Web.doLogin(any()), times(1));
        }
    }

    @Test
    @DisplayName("test:userService#singUp | 注册失败")
    void test_14() {
        final SingUpReq req = new SingUpReq().setUname(user.getUname()).setPasswd("123456");

        when(userRepo.countWithUname(anyString())).thenReturn(0L);
        when(userRepo.save(any(User.class))).thenReturn(false);

        assertThrows(VerifyException.class, () -> userService.singUp(req));

        verify(userRepo, times(1)).countWithUname(anyString());
        verify(userRepo, times(1)).save(any(User.class));
    }


    /* @Test
    @DisplayName("")
    void test_2() {
        try (final MockedStatic<Web> webMock = mockStatic(Web.class)) {
            when(userRepo.findOne(anyLong())).thenReturn(null);

            // 为什么不能用thenReturn(null)？ 因为在Web#getUserInfo已经做了判断，如果返回null，则抛出异常
            webMock.when(Web::getUserInfo).thenReturn(new UserInfo().setIdLong(1L));

            assertThrows(VerifyException.class, userService::refreshUserInfo);
        }
    } */
}