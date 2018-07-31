package com.xrj.demo.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xrj.demo.common.model.RestResp;
import com.xrj.demo.user.mapper.UserMapper;
import com.xrj.demo.user.model.User;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserMapper userMapper;
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all-users")
    public RestResp<List<User>> getUsers() {
		List<User> users = userMapper.selectAll();
        users.stream().forEach(one -> one.setPassword(null));
        return RestResp.ok(users);
    }
	
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@PostMapping("/exists-phone")
	public RestResp<Boolean> checkIfExistsPhone(@RequestParam("phone")String phone) {
		if(userMapper.queryByPhone(phone) == null) {
			return RestResp.ok(false);
		}
		return RestResp.ok(true);
	}

}
