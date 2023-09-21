package com.dy;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.dy.mapper.UserMapper;
import com.dy.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class UserCenterApplicationTests {


        @Resource
        private UserMapper userMapper;

        @Test
        public void testSelect() {
            System.out.println(("----- selectAll method test ------"));
            List<User> userList = userMapper.selectList(null);
            Assert.isTrue(5 == userList.size(), "查询出的结果数量不对");
            userList.forEach(System.out::println);
        }


}
