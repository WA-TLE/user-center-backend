package com.dy.service.impl;

import com.dy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

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
        String userAccount = "abcders";
        String userPassword = "123456789";
        String checkPassword = "123456789";
        Long result = userService.userRegister(userAccount, userPassword, checkPassword);
       //  数据为 空
        Assertions.assertEquals(-1, result);

        //  账户重复
        userAccount = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        //  用户名过短
        userAccount = "dy";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        //  特殊字符....
        userAccount = "d**~~y";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        //  密码不一致
        userAccount = "dyfdsjflas";
        checkPassword = "1234567891";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);


        userAccount = "success2";
        checkPassword = "helloworld";
        userPassword = "helloworld";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
       // Assertions.assertEquals(1, result);


        log.info("新增加的 id 为: {}", result);

    }
}