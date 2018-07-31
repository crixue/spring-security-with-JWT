package com.xrj.demo.user.model;

import javax.persistence.*;

import lombok.Data;

@Table(name = "tbl_role")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String role;

    private String desc;

}