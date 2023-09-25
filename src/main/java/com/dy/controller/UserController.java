package com.dy.controller;

import com.dy.model.domain.User;
import com.dy.model.entry.UserLogin;
import com.dy.model.entry.UserRegister;
import com.dy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.dy.constant.UserConstant.ADMIN_ROLE;
import static com.dy.constant.UserConstant.USER_LOGIN_STATE;

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

    /**
     * 根据姓名查询用户
     *
     * @param username
     * @param request
     * @return
     */
    @GetMapping("/search")
    public List<User> queryUsers(String username, HttpServletRequest request) {

        if (!isAdmin(request)) {
            return new ArrayList<>();
        }

        log.info("查询用户: {}", username);

        return userService.queryUserList(username);
    }

    /**
     * 根据 id 删除用户
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public Boolean deleteById(@RequestBody Long id, HttpServletRequest request) {

        if (!isAdmin(request)) {
            return false;
        }
        log.info("根据 id 删除用户: {}", id);
        return userService.deleteById(id);
    }

    /**
     * 校验是否为管理员
     *
     * @param request
     * @return
     */
    private Boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }


}
