package com.boot.webserver.controller;

import com.alibaba.fastjson2.JSONObject;
import com.boot.webserver.service.AsyncService;
import com.boot.webserver.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
  @Autowired private AsyncService asyncService;
  @Autowired private IUserService userService;

  @Value("${server.port}")
  private int port;

  @GetMapping("/info")
  public Object info() {
    JSONObject json = new JSONObject();
    return json;
  }

  @GetMapping("/list")
  public Object login() {
    return userService.list();
  }
}
