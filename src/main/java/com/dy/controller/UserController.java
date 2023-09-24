package com.dy.controller;

import com.dy.model.domain.User;
import com.dy.model.entry.UserLogin;
import com.dy.model.entry.UserRegister;
import com.dy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: dy
 * @Date: 2023/9/24 18:03
 * @Description:
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    UserService userService;

    /**
     * 用户注册
     *
     * @param userRegister
     * @return
     */
    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegister userRegister) {

        log.info("用户注册: {}", userRegister);
        //  必要的校验
        if (userRegister == null) {
            return null;
        }
        String userAccount = userRegister.getUserAccount();
        String userPassword = userRegister.getUserPassword();
        String checkPassword = userRegister.getCheckPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userRegister(userRegister);
    }

    /**
     * 用户登录
     *
     * @param userLogin
     * @return
     */
    @PostMapping("/login")
    public User userLogin(@RequestBody UserLogin userLogin, HttpServletRequest request) {

        log.info("用户登陆: {}", userLogin);
        //  必要的校验
        if (userLogin == null) {
            return null;
        }
        String userAccount = userLogin.getUserAccount();
        String userPassword = userLogin.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userLogin, request);
    }


}
