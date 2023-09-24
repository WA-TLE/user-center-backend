package com.dy.service.impl;

import com.dy.model.entry.UserRegister;
import com.dy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author: dy
 * @Date: 2023/9/23 13:16
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class UserServiceImplTest {

    @Resource
    UserService userService;

    @Test
    public void userRegister() {

        //  todo 昨天这里的测试有问题, 待重测
        String userAccount = "admin";
        String userPassword = "123456789";
        String checkPassword = "123456789";
        UserRegister userRegister = new UserRegister(userAccount, userPassword, checkPassword);

        userRegister.setUserAccount("");
        Long result = userService.userRegister(userRegister);
        //  数据为 空
        Assertions.assertEquals(-1, result);
        userRegister.setUserAccount(userAccount);


        //  账户重复
        userRegister.setUserAccount("123456");
        result = userService.userRegister(userRegister);
        Assertions.assertEquals(-1, result);
        userRegister.setUserAccount(userAccount);

        //  用户名过短
        userRegister.setUserAccount("dy");
        result = userService.userRegister(userRegister);
        Assertions.assertEquals(-1, result);
        userRegister.setUserAccount(userAccount);


        //  特殊字符....
        userRegister.setUserAccount("d**~~y");
        result = userService.userRegister(userRegister);
        Assertions.assertEquals(-1, result);
        userRegister.setUserAccount(userAccount);

        //  密码不一致
        userRegister.setUserPassword("hello World");
        userRegister.setCheckPassword("Hello world!");
        result = userService.userRegister(userRegister);
        Assertions.assertEquals(-1, result);

        userRegister.setUserPassword("hello World");
        userRegister.setCheckPassword("hello World");



        result = userService.userRegister(userRegister);
       // Assertions.assertEquals(1, result);


        log.info("新增加的 id 为: {}", result);

    }
}