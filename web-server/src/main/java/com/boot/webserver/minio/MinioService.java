package com.boot.webserver.minio;

import cn.hutool.core.util.IdUtil;
import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/** Minio 工具类 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MinioService {

  private final MinioConfig minioConfig;

  private final MinioClient minioClient;

  /** 文件上传 */
  public String upload(MultipartFile file) {
    String bucketName = minioConfig.getBucketName();
    try {
      if (!this.bucketExists(bucketName)) {
        this.makeBucket(bucketName);
      }
      String fileName = file.getOriginalFilename();
      assert fileName != null;
      String objectName =
          IdUtil.randomUUID().replace("-", "") + fileName.substring(fileName.lastIndexOf("."));
      this.putObject(bucketName, file, objectName, file.getContentType());
      return minioConfig.getEndpoint() + "/" + bucketName + "/" + objectName;
    } catch (Exception e) {
      log.error("文字上传失败", e);
      return null;
    }
  }

  /**
   * 检查存储桶是否存在
   *
   * @param bucketName 存储桶名称
   */
  @SneakyThrows
  public boolean bucketExists(String bucketName) {
    return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
  }

  /**
   * 创建存储桶
   *
   * @param bucketName 存储桶名称
   */
  @SneakyThrows
  public boolean makeBucket(String bucketName) {
    boolean flag = bucketExists(bucketName);
    if (!flag) {
      minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
      return true;
    } else {
      return false;
    }
  }

  /** 列出所有存储桶 */
  @SneakyThrows
  public List<Bucket> listBuckets() {
    return minioClient.listBuckets();
  }

  /**
   * 列出存储桶中的所有对象
   *
   * @param bucketName 存储桶名称
   */
  @SneakyThrows
  public Iterable<Result<Item>> listObjects(String bucketName) {
    boolean flag = bucketExists(bucketName);
    if (flag) {
      return minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
    }
    return null;
  }

  /**
   * 文件上传
   *
   * @param bucketName 桶名称
   * @param multipartFile 上传的文件
   */
  @SneakyThrows
  public void putObject(
      String bucketName, MultipartFile multipartFile, String filename, String fileType) {
    InputStream inputStream = new ByteArrayInputStream(multipartFile.getBytes());
    minioClient.putObject(
        PutObjectArgs.builder().bucket(bucketName).object(filename).stream(
                inputStream, -1, minioConfig.getFileSize())
            .contentType(fileType)
            .build());
  }

  /**
   * 获取对象的元数据
   *
   * @param bucketName 存储桶名称
   * @param objectName 存储桶里的对象名称
   */
  @SneakyThrows
  public StatObjectResponse statObject(String bucketName, String objectName) {
    boolean flag = bucketExists(bucketName);
    if (flag) {
      return minioClient.statObject(
          StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }
    return null;
  }

}
