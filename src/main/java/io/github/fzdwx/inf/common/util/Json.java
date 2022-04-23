package io.github.fzdwx.inf.common.util;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import lombok.extern.slf4j.Slf4j;

/**
 * json 工具类 for Jackson
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2021/12/1 13:02
 */
@Slf4j
public final class Json {

    private Json() {
    }

    /**
     * 将对象序列化成json字符串
     *
     * @param value javaBean
     * @param <T>   T 泛型标记
     * @return jsonString json字符串
     */
    public static <T> String toJson(final T value) {
        return JSON.toJSONString(value);
    }

    /**
     * 将对象序列化成 json byte 数组
     *
     * @param object javaBean
     * @return jsonString json字符串
     */
    public static byte[] toJsonAsBytes(final Object object) {
        return JSON.toJSONBytes(object);
    }

    /**
     * 解析json字符串到JSONObject
     *
     * @param json 需要转化为jsonObject的对象
     * @return {@link JSONObject}
     */
    public static com.alibaba.fastjson2.JSONObject parseObj(final String json) {
        return JSON.parseObject(json);
    }

    public static JSONArray parseArray(final String json) {
        return JSON.parseArray(json);
    }

    /**
     * 将json反序列化成对象
     *
     * @param content   content
     * @param valueType class
     * @param <T>       T 泛型标记
     * @return Bean
     */
    public static <T> T parse(final String content, final Class<T> valueType) {
        return JSON.parseObject(content, valueType);
    }

    /**
     * 将json byte 数组反序列化成对象
     *
     * @param bytes     json bytes
     * @param valueType class
     * @param <T>       T 泛型标记
     * @return Bean
     */
    public static <T> T parse(final byte[] bytes, final Class<T> valueType) {
        return JSON.parseObject(bytes, valueType);
    }

}