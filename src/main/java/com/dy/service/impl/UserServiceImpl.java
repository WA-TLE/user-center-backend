package com.dy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dy.mapper.UserMapper;
import com.dy.model.domain.User;
import com.dy.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author dy
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-09-22 19:52:12
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;


    /**
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1.校验

        //  1.1 校验数据非空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1L;
        }

        //  1.2 账户长度不小于 4 位
        if (userAccount.length() < 4) {
            return -1L;
        }

        //  1.3 密码不小于 8 位
        if (userPassword.length() < 8) {
            return -1L;
        }

       //  1.4 账户不包含特殊字符
        //  匹配标点字符, 符号字符, 一个或多个空白字符
        String validPattern = "\\pP|\\pS|\\s+";
        /*
            Pattern.compile(validPattern): 将之前写的 validPattern 编译成一种特殊的查找规则, 让计算机可以理解它
            .matcher(userAccount): 要在 userAccount 中使用之前定义的查找规则
            matcher.find() 配备到对应规则后就返回 true
         */
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1L;
        }

        //  1.5 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1L;
        }

        //  1.6 账户名不能重复
        //      要先从数据库中查找是否存在对应的名称
        //         MybatisPlus 的使用
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //  给查找对象添加查找规则
        queryWrapper.eq("user_account", userAccount);
        Long count = userMapper.selectCount(queryWrapper);

        if (count > 0) {
            return -1L;
        }

        //  2. 加密
        //  2.1  使用 md5 进行加密
        //  2.2  加盐, 把水搞混
        final String SALT = "ding_yu";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //  3. 插入数据
        User user = User.builder()
                .userAccount(userAccount)
                .userPassword(encryptPassword)
                .build();

        int insert = userMapper.insert(user);
        log.info("数据库改变的条数: {}", insert);

        if (insert != 1) {
            return -1L;
        }


        return user.getId();
    }

}
