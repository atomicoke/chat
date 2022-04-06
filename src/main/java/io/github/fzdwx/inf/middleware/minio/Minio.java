package io.github.fzdwx.inf.middleware.minio;

import cn.hutool.core.util.IdUtil;
import io.github.fzdwx.inf.common.exc.MinioException;
import io.github.fzdwx.inf.middleware.minio.model.MinioUploadRes;
import io.github.fzdwx.lambada.lang.UnixTime;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 16:55
 */
@Component
public class Minio implements InitializingBean {

    private static MinioClient minioClient;
    private static String bucketName;
    private static String endpointStatic;
    private static String outEndpointStatic;
    /**
     * 内网的地址
     */
    private final String endpoint;
    private final String outEndpoint;
    private final String accessKey;
    private final String secretKey;
    private final String bucket;

    public Minio(@Value("${minio.endpoint}") final String endpoint,
                 @Value("${minio.out-endpoint}") final String outEndpoint,
                 @Value("${minio.access-key}") final String accessKey,
                 @Value("${minio.secret-key}") final String secretKey,
                 @Value("${minio.bucket}") final String bucket) {
        this.endpoint = endpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucket = bucket;
        this.outEndpoint = outEndpoint;
    }

    public static MinioUploadRes upload(InputStream stream, String fileName) {
        String objectName = UnixTime.unixTime() + "/" + IdUtil.getSnowflakeNextId() + "-" + fileName;

        try {
            return MinioUploadRes.create(minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(stream, stream.available(), -1)
                    .build()), getAccessUrl(objectName));
        } catch (Exception e) {
            throw new MinioException(e);
        }
    }

    public static String getAccessUrl(String objectName) {
        return getAccessUrl(objectName, 1, TimeUnit.HOURS);
    }

    /**
     * 获取 object 对应的 访问url
     *
     * @param objectName 对象名称
     * @param amount     有效时间
     * @param timeUnit   时间单位
     * @return {@link String }
     */
    public static String getAccessUrl(String objectName, int amount, TimeUnit timeUnit) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .method(Method.GET)
                    .expiry(amount, timeUnit)
                    .build())
                    .replace(endpointStatic,outEndpointStatic);
        } catch (Exception e) {
            throw new MinioException(e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        bucketName = bucket;
        endpointStatic = endpoint;
        outEndpointStatic = outEndpoint;
        minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}