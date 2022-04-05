package io.github.fzdwx.inf.common.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Spring aop 工具类
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2021/12/2 12:21
 */
public class SpringAspectUtil {

    /**
     * 获取aop切点上指定的注解
     *
     * @return {@link T}
     */
    public static <T extends Annotation> T getAnnotation(final ProceedingJoinPoint point, final Class<T> annotationClass) {
        return getMethodSignature(point).getMethod().getAnnotation(annotationClass);
    }

    /**
     * 获取aop切点的方法签名
     *
     * @return {@link MethodSignature}
     */
    public static MethodSignature getMethodSignature(final ProceedingJoinPoint point) {
        return (MethodSignature) point.getSignature();
    }

    /**
     * 获取aop切点要执行的方法
     *
     * @return {@link Method}
     */
    public static Method getMethod(final ProceedingJoinPoint point) {
        return getMethodSignature(point).getMethod();
    }
}