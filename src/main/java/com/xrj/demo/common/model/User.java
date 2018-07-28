package com.xrj.demo.common.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class User {

//    @Id
    private String id;

    private String username;

    private String password;
    private String email;
    private String phone; 
    private Date lastPasswordResetDate;
    private List<String> roles;
}
