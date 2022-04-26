package org.atomicode.inf.middleware.minio.api.model;

import io.minio.ObjectWriteResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * upload response
 *
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/4 17:25
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MinioUploadRes {

    private String accessUrl;
    private String objectName;

    public static MinioUploadRes create(ObjectWriteResponse response, String accessUrl) {
        return new MinioUploadRes(accessUrl, response.object());
    }
}