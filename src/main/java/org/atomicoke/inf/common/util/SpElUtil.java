package org.atomicoke.inf.common.util;

import io.github.fzdwx.lambada.Lang;
import jodd.util.StringPool;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SpEl 工具类
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2021/12/02 14:15:13
 */
public class SpElUtil {

    private static final Map<String, Expression> CACHE = new ConcurrentHashMap<>();

    private static final ExpressionParser PARSER = new SpelExpressionParser();

    /**
     * 分析spel表达式
     *
     * @param spEl         spel
     * @param argMap       参数映射 {@link #getArgMap(ProceedingJoinPoint)}
     * @param clazz        结果值的类型
     * @param defaultValue 如果spEl解析出来为null的值
     * @return {@link T}
     */
    public static <T> T analytical(final String spEl, final Map<String, Object> argMap, final Class<T> clazz, final T defaultValue) {
        if (!spEl.contains(StringPool.HASH)) return defaultValue;
        Expression expression = CACHE.get(spEl);
        final StandardEvaluationContext context = new StandardEvaluationContext(argMap);
        for (final Map.Entry<String, Object> entry : argMap.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        if (expression == null) {
            expression = PARSER.parseExpression(spEl);
            CACHE.putIfAbsent(spEl, expression);
        }
        return Lang.defVal(expression.getValue(context, clazz), defaultValue);
    }

    /**
     * 从{@link ProceedingJoinPoint#getArgs()}中得到参数与其字段的kv映射
     *
     * @param point 切点
     * @return {@link HashMap}<{@link String}, {@link Object}>
     */
    public static Map<String, Object> getArgMap(final ProceedingJoinPoint point) {
        final Object[] args = point.getArgs();

        final Map<String, Object> map = new HashMap<>(args.length + 1);
        final Method method = SpringAspectUtil.getMethod(point);

        final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        final String[] params = discoverer.getParameterNames(method);

        if (params != null) {
            for (int len = 0; len < params.length; len++) {
                map.put(params[len], args[len]);
            }
        }

        map.put("methodName", method.getName());

        return map;
    }
}