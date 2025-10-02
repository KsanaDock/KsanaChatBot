package com.ksanaDock.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.ksanaDock.service.FileUploadService;
import com.ksanaDock.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.Arrays;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    @Autowired
    private OSS ossClient;

    @Override
    public String uploadAvatar(String userId, MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String objectName = "avatars/" + UUID.randomUUID().toString() + 
            originalFilename.substring(originalFilename.lastIndexOf("."));
        
        try {
            ossClient.putObject(bucketName, objectName, file.getInputStream());
            return String.format("https://%s.%s/%s", bucketName, endpoint, objectName);
        } catch (OSSException oe) {
            LogUtil.logBusinessError("FileUpload", "uploadAvatar", "OSS上传失败", oe);
            throw new RuntimeException("文件上传失败");
        }
    }

    @Override
    public void deleteFile(String objectName) {
        if (objectName != null && !objectName.isEmpty()) {
            try {
                // 从完整URL中提取objectName
                if (objectName.startsWith("http")) {
                    String[] parts = objectName.split("/");
                    objectName = String.join("/", Arrays.copyOfRange(parts, 3, parts.length));
                }
                ossClient.deleteObject(bucketName, objectName);
                LogUtil.logBusinessOperation("FileUpload", "deleteFile", "删除文件成功: " + objectName);
            } catch (Exception e) {
                LogUtil.logBusinessError("FileUpload", "deleteFile", "删除文件失败", e);
            }
        }
    }

    @Override
    public String generateFullOssUrl(String objectName) {
        if (objectName != null && !objectName.isEmpty()) {
            try {
                // 如果已经是完整URL则直接返回
                if (objectName.startsWith("http")) {
                    return objectName;
                }
                // 否则构建完整的OSS URL
                return String.format("https://%s.%s/%s", bucketName, endpoint, objectName);
            }
            catch (Exception e) {
                LogUtil.logBusinessError("FileUpload", "generateFullOssUrl", "生成OSS URL失败", e);
            }
        }
        return null;
    }
}