package com.xrj.demo.user.model;

import javax.persistence.*;

import lombok.Data;

@Table(name = "tbl_user_role")
@Data
public class UserAndRole {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Integer roleId;

}