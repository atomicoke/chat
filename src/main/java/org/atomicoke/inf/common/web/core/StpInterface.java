package org.atomicoke.inf.common.web.core;

import io.github.fzdwx.lambada.Coll;
import org.atomicoke.inf.common.web.Web;

import java.util.List;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/5 9:40
 */
public class StpInterface implements cn.dev33.satoken.stp.StpInterface {

    @Override
    public List<String> getPermissionList(final Object loginId, final String loginType) {
        return Coll.list();
    }

    @Override
    public List<String> getRoleList(final Object loginId, final String loginType) {
        return Coll.list(Web.getUserInfo().getRoleKey());
    }
}