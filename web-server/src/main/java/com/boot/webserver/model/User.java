package com.boot.webserver.model;

import com.baomidou.mybatisplus.annotation.*;
import com.boot.webserver.common.typehandler.CryptoTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "user", autoResultMap = true)
public class User implements Serializable {

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  @ApiModelProperty(value = "用户名")
  private String username;

  @ApiModelProperty(value = "密码")
  @TableField(typeHandler = CryptoTypeHandler.class)
  private String password;

  @ApiModelProperty(value = "头像")
  private String avatar;

  @ApiModelProperty(value = "昵称")
  private String nickname;

  @ApiModelProperty(value = "密钥")
  private String secretKey;

  @ApiModelProperty(value = "手机号")
  private String phoneNumber;

  @ApiModelProperty(value = "手机区号")
  private String phoneArea;

  @ApiModelProperty(value = "邮箱地址")
  private String emailAddr;

  @ApiModelProperty(value = "创建时间")
  @TableField(fill = FieldFill.INSERT)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createTime;

  @ApiModelProperty(value = "修改时间")
  @TableField(fill = FieldFill.UPDATE)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updateTime;
}
