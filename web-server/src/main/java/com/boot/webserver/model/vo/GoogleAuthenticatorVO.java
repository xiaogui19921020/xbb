package com.boot.webserver.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleAuthenticatorVO {
    @ApiModelProperty(value = "秘钥")
    private String secret;
    @ApiModelProperty(value = "url")
    private String url;
}
