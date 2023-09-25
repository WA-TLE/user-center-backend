package com.dy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dy.mapper.UserMapper;
import com.dy.model.domain.User;
import com.dy.model.entry.UserLogin;
import com.dy.model.entry.UserRegister;
import com.dy.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dy.constant.UserConstant.USER_LOGIN_STATE;


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
     * 盐值, 加密数据
     */
    private static final String SALT = "ding_yu";


    /**
     * @param userRegister@return
     */
    public Long userRegister(UserRegister userRegister) {

        String userAccount = userRegister.getUserAccount();
        String userPassword = userRegister.getUserPassword();
        String checkPassword = userRegister.getCheckPassword();

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

    public User userLogin(UserLogin userLongin, HttpServletRequest request) {

        String userAccount = userLongin.getUserAccount();
        String userPassword = userLongin.getUserPassword();

        //  1.1 校验数据非空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        //  1.2 账户长度不小于 4 位
        if (userAccount.length() < 4) {
            return null;
        }

        //  1.3 密码不小于 8 位
        if (userPassword.length() < 8) {
            return null;
        }

        //  1.4 账户不包含特殊字符
        //  匹配标点字符, 符号字符, 一个或多个空白字符
        String validPattern = "\\pP|\\pS|\\s+";

        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", encryptPassword);
//        queryWrapper.eq("id", 8);

        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("user login failed, userAccount Cannot match userPassword");
            return null;
        }

        //  查询出结果后, 要对用户信息进行脱敏, 不能直接将用户的密码返回给前端
        //  这里设置的值, 是我查出来的用户 (它里面包含的信息就挺多的)
        User secureUsers = getSecureUsers(user);


        //  记录用户登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE, secureUsers);


        return secureUsers;
    }

    /**
     * 根据用户名查询用户
     *
     * @param username@return
     * @return
     */
    public List<User> queryUserList(String username) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_name", username);


        List<User> userList = userMapper.selectList(queryWrapper);
        List<User> secureUserList = new ArrayList<>();

        for (User user : userList) {
            user = getSecureUsers(user);
            secureUserList.add(user);
        }
        return secureUserList;
    }

    /**
     * 根据 id 删除用户
     *
     * @param id
     * @return
     */
    public Boolean deleteById(Long id) {

        int i = userMapper.deleteById(id);

        return i == 1;
    }

    public User getSecureUsers(User originUser) {
        User secureUser = new User();
        secureUser.setId(originUser.getId());
        secureUser.setUserName(originUser.getUserName());
        secureUser.setUserAccount(originUser.getUserAccount());
        secureUser.setAvatarUrl(originUser.getAvatarUrl());
        secureUser.setGender(originUser.getGender());
        secureUser.setEmail(originUser.getEmail());
        secureUser.setPhone(originUser.getPhone());
        secureUser.setUserRole(originUser.getUserRole());    //  把用户的权限也返回给前端
        secureUser.setUserStatus(originUser.getUserStatus());
        secureUser.setCreateTime(originUser.getCreateTime());

        return secureUser;
    }


}
