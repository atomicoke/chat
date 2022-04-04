package io.github.fzdwx.inf.minio;

import cn.hutool.core.io.FileTypeUtil;
import io.github.fzdwx.inf.exc.VerifyException;
import io.github.fzdwx.inf.minio.model.MinioUploadRes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 17:34
 */
@RestController
@RequestMapping("/upload")
public class MinioApi {

    static List<String> imageTypes = List.of("jpg", "jpeg", "png", "gif");
    static List<String> videoTypes = List.of("mp4", "avi", "rmvb", "wmv", "mkv", "flv", "mov", "mpg", "mpeg");
    static long imageMaxSize = 1024 * 1024 * 3;
    static long videoMaxSize = 1024 * 1024 * 100;


    @PostMapping("/image")
    public MinioUploadRes image(MultipartFile file) throws IOException {
        checkImage(file);
        return Minio.upload(file.getInputStream(), file.getOriginalFilename());
    }

    private void checkImage(final MultipartFile file) throws IOException {
        final var available = file.getInputStream().available();
        if (available > imageMaxSize) {
            throw new VerifyException("图片大小不能超过3M");
        }

        final var type = FileTypeUtil.getType(file.getInputStream());
        if (type == null) {
            throw new VerifyException("文件格式不支持");
        }

        if (!imageTypes.contains(type)) {
            throw new VerifyException("不支持的图片类型:" + type);
        }
    }
}