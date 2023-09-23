package com.dy.service;

/**
* @author dy
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-09-22 19:52:12
*/
public interface UserService {


    /**
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    Long userRegister(String userAccount, String userPassword, String checkPassword);

}
