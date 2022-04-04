package io.github.fzdwx.inf.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.github.fzdwx.inf.exc.VerifyException;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

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
    @Nullable
    public static <T> String toJson(final T value) {
        try {
            return getInstance().writeValueAsString(value);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将对象序列化成json字符串,不输出NULL值
     *
     * @param value javaBean
     * @param <T>   T 泛型标记
     * @return jsonString json字符串
     */
    @Nullable
    public static <T> String toJsonNoNull(final T value) {
        try {
            return getInstanceNoNull().writeValueAsString(value);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将对象序列化成json字符串 美化
     *
     * @param value 需要json化的数据
     * @return {@link String} json字符串
     */
    @Nullable
    public static <T> String toJsonPretty(final T value) {
        try {
            return getInstance().writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将对象序列化成 json byte 数组
     *
     * @param object javaBean
     * @return jsonString json字符串
     */
    public static byte[] toJsonAsBytes(final Object object) {
        try {
            return getInstance().writeValueAsBytes(object);
        } catch (final JsonProcessingException e) {
           throw new VerifyException(e);
        }
    }

    /**
     * 解析object到 jsonObject
     * <pre>
     *     使用hu tool的jsonObject
     * </pre>
     *
     * @param object 需要转化为jsonObject的对象
     * @return {@link JSONObject}
     */
    public static JSONObject parseObj(final Object object) {
        if (object instanceof String) {
            return JSONUtil.parseObj(object);
        } else {
            return JSONUtil.parseObj(toJson(object));
        }
    }

    public static JSONArray parseArray(final Object object) {
        if (object instanceof String) {
            return JSONUtil.parseArray(object);
        } else {
            return JSONUtil.parseArray(toJson(object));
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param content   content
     * @param valueType class
     * @param <T>       T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T parse(final String content, final Class<T> valueType) {
        try {
            return getInstance().readValue(content, valueType);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将json反序列化成对象
     *
     * @param content       content
     * @param typeReference 泛型类型
     * @param <T>           T 泛型标记
     * @return Bean
     */
    public static <T> T parse(final String content, final TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(content, typeReference);
        } catch (final IOException e) {
           throw new VerifyException(e);
        }
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
        try {
            return getInstance().readValue(bytes, valueType);
        } catch (final IOException e) {
           throw new VerifyException(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param bytes         bytes
     * @param typeReference 泛型类型
     * @param <T>           T 泛型标记
     * @return Bean
     */
    public static <T> T parse(final byte[] bytes, final TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(bytes, typeReference);
        } catch (final IOException e) {
           throw new VerifyException(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param in        InputStream
     * @param valueType class
     * @param <T>       T 泛型标记
     * @return Bean
     */
    public static <T> T parse(final InputStream in, final Class<T> valueType) {
        try {
            return getInstance().readValue(in, valueType);
        } catch (final IOException e) {
           throw new VerifyException(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param in            InputStream
     * @param typeReference 泛型类型
     * @param <T>           T 泛型标记
     * @return Bean
     */
    public static <T> T parse(final InputStream in, final TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(in, typeReference);
        } catch (final IOException e) {
           throw new VerifyException(e);
        }
    }

    /**
     * 将json反序列化成List对象
     *
     * @param content      content
     * @param valueTypeRef class
     * @param <T>          T 泛型标记
     * @return List
     */
    public static <T> List<T> parseArray(String content, final Class<T> valueTypeRef) {
        try {

            if (!StringUtils.startsWithIgnoreCase(content, StringPool.LEFT_SQ_BRACKET)) {
                content = StringPool.LEFT_SQ_BRACKET + content + StringPool.RIGHT_SQ_BRACKET;
            }

            final List<Map<String, Object>> list = getInstance().readValue(content, new TypeReference<List<Map<String, Object>>>() {
            });
            final List<T> result = new ArrayList<>();
            for (final Map<String, Object> map : list) {
                result.add(toPojo(map, valueTypeRef));
            }
            return result;
        } catch (final IOException e) {
            log.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Nullable
    public static Map<String, Object> toMap(final String content) {
        try {
            return getInstance().readValue(content, Map.class);
        } catch (final IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Nullable
    public static <T> Map<String, T> toMap(final String content, final Class<T> valueTypeRef) {
        try {
            final Map<String, Map<String, Object>> map = getInstance().readValue(content, new TypeReference<Map<String, Map<String, Object>>>() {
            });
            final Map<String, T> result = new HashMap<>(16);
            for (final Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                result.put(entry.getKey(), toPojo(entry.getValue(), valueTypeRef));
            }
            return result;
        } catch (final IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T toPojo(final Map<?, ?> fromValue, final Class<T> toValueType) {
        return getInstance().convertValue(fromValue, toValueType);
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param jsonString jsonString
     * @return jsonString json字符串
     */
    public static JsonNode readTree(final String jsonString) {
        try {
            return getInstance().readTree(jsonString);
        } catch (final IOException e) {
           throw new VerifyException(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param in InputStream
     * @return jsonString json字符串
     */
    public static JsonNode readTree(final InputStream in) {
        try {
            return getInstance().readTree(in);
        } catch (final IOException e) {
           throw new VerifyException(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param content content
     * @return jsonString json字符串
     */
    public static JsonNode readTree(final byte[] content) {
        try {
            return getInstance().readTree(content);
        } catch (final IOException e) {
           throw new VerifyException(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param jsonParser JsonParser
     * @return jsonString json字符串
     */
    public static JsonNode readTree(final JsonParser jsonParser) {
        try {
            return getInstance().readTree(jsonParser);
        } catch (final IOException e) {
           throw new VerifyException(e);
        }
    }

    public static ObjectMapper getInstance() {
        return JacksonHolder.INSTANCE;
    }

    public static ObjectMapper getInstanceNoNull() {
        return JacksonNoNullHolder.INSTANCE;
    }

    private static class JacksonHolder {

        private static final ObjectMapper INSTANCE = new JacksonObjectMapper();
    }

    public static class JacksonObjectMapper extends ObjectMapper {

        private static final long serialVersionUID = 4288193147502386170L;

        private static final Locale CHINA = Locale.CHINA;

        public JacksonObjectMapper() {
            //设置地点为中国
            this.setLocale(CHINA);
            //去掉默认的时间戳格式
            this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            //设置为中国上海时区
            this.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            //序列化时，日期的统一格式
            this.setDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN, Locale.CHINA));
            //序列化处理
            this.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
            this.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
            //遇到空的bean，兼容处理
            this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            //反序列化时，属性不存在的兼容处理
            this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            //单引号处理
            this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            //日期格式化
            this.registerModule(new JavaTimeModule());
            this.findAndRegisterModules();
        }

        @Override
        public ObjectMapper copy() {
            return super.copy();
        }
    }

    public static class JacksonObjectNoNullMapper extends JacksonObjectMapper {

        private static final long serialVersionUID = 4288193147502386170L;

        public JacksonObjectNoNullMapper() {
            this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
    }

    private static class JacksonNoNullHolder {

        private static final ObjectMapper INSTANCE = new JacksonObjectNoNullMapper();
    }

    public static final class JavaTimeModule extends SimpleModule {

        private static final long serialVersionUID = -79657213673258842L;

        public JavaTimeModule() {
            super(PackageVersion.VERSION);
            this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DatePattern.NORM_DATETIME_FORMATTER));
            this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DatePattern.NORM_DATE_FORMATTER));
            this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DatePattern.NORM_TIME_FORMATTER));
            this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DatePattern.NORM_DATETIME_FORMATTER));
            this.addSerializer(LocalDate.class, new LocalDateSerializer(DatePattern.NORM_DATE_FORMATTER));
            this.addSerializer(LocalTime.class, new LocalTimeSerializer(DatePattern.NORM_TIME_FORMATTER));
        }

    }

}