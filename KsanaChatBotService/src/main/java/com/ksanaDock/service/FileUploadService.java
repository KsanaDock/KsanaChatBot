package com.ksanaDock.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileUploadService {
    /**
     * 上传头像并返回OSS URL
     * @param userId 用户ID
     * @param file 头像文件
     * @return 头像URL
     * @throws IOException 如果文件操作失败
     */
    String uploadAvatar(String userId, MultipartFile file) throws IOException;

    /**
     * 删除旧头像
     * @param oldAvatarUrl 旧头像URL
     */
    void deleteFile(String oldAvatarUrl);

    /**
     * 生成全路径的ossURL
     * @param objectName 对象名称
     * @return 全路径的ossURL
     */
    String generateFullOssUrl(String objectName);
}