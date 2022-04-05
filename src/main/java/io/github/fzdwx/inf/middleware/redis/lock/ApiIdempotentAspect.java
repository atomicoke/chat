package io.github.fzdwx.inf.middleware.redis.lock;

import cn.hutool.crypto.SecureUtil;
import io.github.fzdwx.inf.common.exc.ForbiddenException;
import io.github.fzdwx.inf.common.exc.VerifyException;
import io.github.fzdwx.inf.common.util.SpElUtil;
import io.github.fzdwx.inf.common.util.SpringAspectUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2021/12/2 12:08
 */
@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class ApiIdempotentAspect {

    private static final String LOCK_KEY_PREFIX = "apiIdempotent_lock:";

    private final RedissonClient redissonClient;

    /**
     * 接口幂等切面
     */
    @Around("@annotation(io.github.fzdwx.inf.middleware.redis.lock.ApiIdempotent)||" +
            "@within(io.github.fzdwx.inf.middleware.redis.lock.ApiIdempotent)")
    public Object idempotent(final ProceedingJoinPoint point) throws Throwable {
        final ApiIdempotent idempotent = SpringAspectUtil.getAnnotation(point, ApiIdempotent.class);

        final Map<String, Object> argMap = SpElUtil.getArgMap(point);
        final Supplier<String> md5Supplier = () -> SecureUtil.md5(argMap.toString());

        final String lockName = getLockName(argMap, md5Supplier, idempotent.value());

        // init lock
        final RLock lock = this.redissonClient.getLock(LOCK_KEY_PREFIX.concat(lockName));

        // 已经被锁住了
        if (lock.isLocked()) throw new ForbiddenException(idempotent.message());

        if (lock.tryLock(idempotent.acquireTimeout(), idempotent.expireTime(), idempotent.unit())) {
            // do process
            final Object proceed;
            try {
                proceed = point.proceed();
            } catch (final Throwable e) {
                lock.unlock();
                throw e;
            }
            return proceed;
        }

        // 没有加锁成功，不执行，抛出异常，提醒用户
        throw new VerifyException(idempotent.message());
    }

    private static String getLockName(final Map<String, Object> argMap, final Supplier<String> md5, final String spEl) {
        final String methodName = argMap.get(ApiIdempotent.METHOD_NAME).toString();

        if (spEl.equals(ApiIdempotent.METHOD_NAME)) {
            return methodName + md5.get();
        } else
            return SpElUtil.analytical(spEl, argMap, String.class, methodName) + md5.get();
    }
}