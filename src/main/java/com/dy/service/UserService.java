package com.dy.service;

import com.dy.model.domain.User;
import com.dy.model.entry.UserLogin;
import com.dy.model.entry.UserRegister;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dy
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2023-09-22 19:52:12
 */
public interface UserService {


    /**
     * 用户注册
     *
     * @param userRegister@return
     */
    Long userRegister(UserRegister userRegister);

    /**
     * 用户登录
     *
     * @param userLogin
     * @param request
     * @return
     */
    User userLogin(UserLogin userLogin, HttpServletRequest request);
}
