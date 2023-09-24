package com.dy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dy.model.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author dy
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2023-09-22 19:52:12
* @Entity generator.domain.User
*/
//@Mapper
public interface UserMapper extends BaseMapper<User> {

}