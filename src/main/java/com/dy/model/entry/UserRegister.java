package com.dy.model.entry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: dy
 * @Date: 2023/9/24 18:07
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegister implements Serializable {
    private static final long serialVersionUID = -2654793916867829542L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;

}
