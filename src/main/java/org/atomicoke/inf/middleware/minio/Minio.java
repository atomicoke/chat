package org.atomicoke.inf.middleware.minio;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.StrUtil;
import io.github.fzdwx.lambada.Time;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import org.atomicoke.inf.common.err.Err;
import org.atomicoke.inf.common.err.impl.MinioException;
import org.atomicoke.inf.common.err.impl.VerifyException;
import org.atomicoke.inf.middleware.id.IdGenerate;
import org.atomicoke.inf.middleware.minio.api.model.MinioUploadRes;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * minio util.
 *
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 16:55
 */
@Component
public class Minio implements InitializingBean {

    static List<String> imageTypes = List.of("jpg", "jpeg", "png", "gif");
    static List<String> videoTypes = List.of("mp4", "avi", "rmvb", "wmv", "mkv", "flv", "mov", "mpg", "mpeg");
    static long imageMaxSize = 1024 * 1024 * 3;
    static long videoMaxSize = 1024 * 1024 * 100;
    private static MinioClient minioClient;
    private static MinioClient minioAccessUrlClient;
    private static String pubBucketName;
    private static String privateBucketName;
    public static Function<String, String> getPrivateUrl = Minio::getAccessUrl;
    private static String endpointStatic;
    private static String outEndpointStatic;
    public static Function<String, String> getPubUrl = Minio::getPubAccessUrl;
    /**
     * 内网的地址
     */
    private final String endpoint;
    private final String outEndpoint;
    private final String accessKey;
    private final String secretKey;
    private final String pubBucket;
    private final String privateBucket;

    public Minio(@Value("${minio.endpoint}") final String endpoint,
                 @Value("${minio.out-endpoint}") final String outEndpoint,
                 @Value("${minio.access-key}") final String accessKey,
                 @Value("${minio.secret-key}") final String secretKey,
                 @Value("${minio.pub-bucket}") final String pubBucket,
                 @Value("${minio.private-bucket}") final String privateBucket
    ) {
        this.endpoint = endpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.pubBucket = pubBucket;
        this.privateBucket = privateBucket;
        this.outEndpoint = outEndpoint;
    }

    public static MinioUploadRes uploadPubImage(InputStream stream, String fileName, String contentType) throws IOException {
        return uploadImage(stream, fileName, pubBucketName, contentType, getPubUrl);
    }

    public static MinioUploadRes uploadPrivateImage(InputStream stream, String fileName, String contentType) throws IOException {
        return uploadImage(stream, fileName, privateBucketName, contentType, getPrivateUrl);
    }

    public static MinioUploadRes uploadImage(InputStream stream, String fileName, String bucketName, String contentType,
                                             Function<String, String> accessUrlFunc) throws IOException {
        if (stream == null) {
            throw Err.verify("image stream is null");
        }
        if (fileName == null) {
            throw Err.verify("image fileName is null");
        }
        checkImage(stream);

        return upload(stream, fileName, bucketName, contentType, accessUrlFunc);
    }

    public static MinioUploadRes uploadPrivate(InputStream stream, String fileName, String contentType) {
        return upload(stream, fileName, privateBucketName, contentType, getPrivateUrl);
    }

    public static MinioUploadRes uploadPublic(InputStream stream, String fileName, String contentType) {
        return upload(stream, fileName, pubBucketName, contentType, getPubUrl);
    }

    public static MinioUploadRes upload(InputStream stream, String fileName, String bucket, String contentType,
                                        Function<String, String> accessUrlFunc) {
        String objectName = Time.unix() + "/" + IdGenerate.nextId() + fileName;

        try {
            return MinioUploadRes.create(
                    minioClient.putObject(PutObjectArgs.builder()
                            .bucket(bucket)
                            .contentType(contentType)
                            .object(objectName)
                            .stream(stream, stream.available(), -1)
                            .build()),
                    accessUrlFunc.apply(objectName));
        } catch (Exception e) {
            throw new MinioException(e);
        }
    }

    public static String getPubAccessUrl(String objectName) {
        if (StrUtil.isBlank(objectName)) {
            return objectName;
        }
        return outEndpointStatic + "/" + pubBucketName + "/" + objectName;
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
        if (StrUtil.isBlank(objectName)) {
            return objectName;
        }
        try {
            return minioAccessUrlClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(privateBucketName)
                    .object(objectName)
                    .method(Method.GET)
                    .expiry(amount, timeUnit)
                    .build());
        } catch (Exception e) {
            throw new MinioException(e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pubBucketName = pubBucket;
        privateBucketName = privateBucket;
        endpointStatic = endpoint;
        outEndpointStatic = outEndpoint;

        minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();

        minioAccessUrlClient = MinioClient.builder()
                .endpoint(outEndpoint)
                .credentials(accessKey, secretKey)
                .build();
    }


    public static void checkImage(final InputStream stream) throws IOException {
        final var available = stream.available();
        if (available > imageMaxSize) {
            throw new VerifyException("图片大小不能超过3M");
        }

        final var type = FileTypeUtil.getType(stream);

        stream.reset();

        if (type == null) {
            throw new VerifyException("文件格式不支持");
        }

        if (!imageTypes.contains(type)) {
            throw new VerifyException("不支持的图片类型:" + type);
        }
    }
}