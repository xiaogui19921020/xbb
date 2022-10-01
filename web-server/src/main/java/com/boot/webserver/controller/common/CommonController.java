package com.boot.webserver.controller.common;

import com.boot.webserver.model.dto.GoogleAuthenticatorDTO;
import com.boot.webserver.model.vo.GoogleAuthenticatorVO;
import com.boot.webserver.service.minio.MinioService;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.util.Utils;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

  @Autowired private MinioService minioService;
  @Autowired private CodeVerifier codeVerifier;
  @Autowired private SecretGenerator secretGenerator;
  @Autowired private QrDataFactory qrDataFactory;
  @Autowired private QrGenerator qrGenerator;

  @PostMapping("/upload")
  public String upload(@RequestParam(name = "file") MultipartFile file) {
    return minioService.upload(file);
  }

  @GetMapping("/getGoogleAuthInfo")
  @ApiOperation(value = "获取google验证")
  @SneakyThrows
  public GoogleAuthenticatorVO getGoogleAuthInfoNew() {
    String secret = secretGenerator.generate();
    QrData data =
        qrDataFactory.newBuilder().label("username").secret(secret).issuer("appname").build();
    String url =
        Utils.getDataUriForImage(qrGenerator.generate(data), qrGenerator.getImageMimeType());
    return GoogleAuthenticatorVO.builder().secret(secret).url(url).build();
  }

  @PostMapping("/verifyGoogleAuth")
  @ApiOperation(value = "验证google验证器")
  public Boolean verifyGoogleAuth(@Validated @RequestBody GoogleAuthenticatorDTO dto) {
    return codeVerifier.isValidCode(dto.getSecret(), dto.getCode());
  }
}
