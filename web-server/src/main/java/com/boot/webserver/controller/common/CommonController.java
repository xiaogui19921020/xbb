package com.boot.webserver.controller.common;

import com.boot.webserver.minio.MinioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

  @Autowired private MinioService minioService;

  @PostMapping("/upload")
  public String upload(@RequestParam(name = "file") MultipartFile file) {
    return minioService.upload(file);
  }
}
