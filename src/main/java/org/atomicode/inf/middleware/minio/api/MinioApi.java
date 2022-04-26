package org.atomicode.inf.middleware.minio.api;

import org.atomicode.fzdwx.lambada.Io;
import org.atomicode.inf.common.web.model.Rest;
import org.atomicode.inf.middleware.minio.Minio;
import org.atomicode.inf.middleware.minio.api.model.MinioUploadRes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 17:34
 */
@RestController
@RequestMapping("/upload")
public class MinioApi {

    /**
     * 上传图片
     */
    @PostMapping("/image")
    public Rest<MinioUploadRes> image(MultipartFile file) throws IOException {
        return Rest.of(Minio.uploadPrivateImage(Io.wrap(file.getBytes()), file.getOriginalFilename(), file.getContentType()));
    }

    /**
     * 上传图片
     */
    @PostMapping("/pub/image")
    public Rest<MinioUploadRes> pubImage(MultipartFile file) throws IOException {
        return Rest.of(Minio.uploadPubImage(Io.wrap(file.getBytes()), file.getOriginalFilename(), file.getContentType()));
    }
}