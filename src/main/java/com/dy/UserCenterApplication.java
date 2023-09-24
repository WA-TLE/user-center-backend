package com.dy;

import com.dy.mapper.UserMapper;
import com.dy.model.domain.User;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.dy.mapper")
public class UserCenterApplication {

    public static void main(String[] args) {

        User user = new User();

        SpringApplication.run(UserCenterApplication.class, args);
    }

}
