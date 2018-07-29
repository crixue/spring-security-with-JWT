package com.xrj.demo.user.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class UserVO {

    private Long id;

    private String username;

    private String password;
    private String email;
    private String phone; 
    private Date lastPasswordResetDate;
    private List<String> roles;
}
