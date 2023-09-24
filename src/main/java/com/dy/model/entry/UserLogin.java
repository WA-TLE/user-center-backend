package com.dy.model.entry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @Author: dy
 * @Date: 2023/9/24 16:59
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLogin implements Serializable {
    private static final long serialVersionUID = 7279585647762607607L;
    private String userAccount;
    private String userPassword;
    //  这里不应该封装到 entry 里面的
//    private HttpServletRequest request;

}
