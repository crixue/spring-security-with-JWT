package com.xrj.demo.auth.service;

import com.xrj.demo.user.model.User;

public interface AuthService {

	String refresh(String oldToken);

	//User register(User userToAdd);

	//String login(String username, String password);

	User registerByPhone(User userToAdd);

	String loginByPhone(String phone, String password);

}
