package com.boot.webserver.service.minio;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Minio 配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {
  /** 服务地址 */
  private String endpoint;

  /** TCP/IP端口号 */
  private Integer port;

  /** 账户 */
  private String accessKey;

  /** 密码 */
  private String secretKey;

  /** 如果是 true，则用的是 https 而不是 http,默认值是 true" */
  private boolean secure;

  /** 存储桶 */
  private String bucketName;

  /** 图片的最大大小 */
  private long imageSize;

  /** 其他文件的最大大小 */
  private long fileSize;

  /** 初始化 MinIO 客户端 */
  @Bean
  public MinioClient minioClient() {
    return MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
  }
}
