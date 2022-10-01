package com.boot.webserver.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleAuthenticatorDTO {
  @ApiModelProperty(value = "秘钥", required = true)
  private String secret;

  @ApiModelProperty(value = "验证码code", required = true)
  private String code;
}
