package com.boot.webserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.webserver.mapper.UserMapper;
import com.boot.webserver.model.User;
import com.boot.webserver.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class OtcUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {}
