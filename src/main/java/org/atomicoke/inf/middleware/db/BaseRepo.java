package org.atomicoke.inf.middleware.db;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.github.fzdwx.lambada.internal.Tuple2;
import org.atomicoke.inf.common.err.Err;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/10 12:44
 */
public class BaseRepo<M extends BaseMapper<E>, E> extends ServiceImpl<M, E> implements IService<E> {


    /**
     * count id
     */
    public long count(Long id) {
        return SqlHelper.retCount(baseMapper.selectCount(query().eq("id", id)));
    }

    /**
     * count id >= 1
     */
    public boolean exist(Long id) {
        return SqlHelper.retCount(baseMapper.selectCount(query().eq("id", id))) >= 1;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean removeBy(final SFunction<E, ?> col, Object value) {
        return this.lu().eq(col, value).remove();
    }

    public final E getBy(final SFunction<E, ?> col, Object value) {
        return this.lambdaQuery().eq(col, value).one();
    }

    public final boolean existBy(final SFunction<E, ?> col, Object value) {
        return exist(newLambdaQuery().eq(col, value));
    }

    public final List<E> listBy(final SFunction<E, ?> col, Object value) {
        return list(lambdaQuery().eq(col, value));
    }

    @SafeVarargs
    public final List<E> listBy(Tuple2<SFunction<E, ?>, Object>... eqs) {
        if (eqs.length <= 0) {
            throw Err.verify("list conditon is empty");
        }

        final LambdaQueryWrapper<E> lq = newLambdaQuery();
        for (final Tuple2<SFunction<E, ?>, Object> t2 : eqs) {
            lq.eq(t2.v1, t2.v2);
        }

        return list(lq);
    }

    protected QueryWrapper<E> newQuery() {
        return Wrappers.query();
    }

    protected LambdaQueryWrapper<E> newLambdaQuery() {
        return Wrappers.lambdaQuery();
    }

    /**
     * count query >= 1
     */
    protected boolean exist(Wrapper<E> query) {
        return SqlHelper.retCount(baseMapper.selectCount(query)) >= 1;
    }

    protected LambdaUpdateChainWrapper<E> lu() {
        return ChainWrappers.lambdaUpdateChain(baseMapper);
    }
}