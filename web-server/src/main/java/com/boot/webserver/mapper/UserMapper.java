package com.boot.webserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.webserver.model.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper extends BaseMapper<User> {

}
