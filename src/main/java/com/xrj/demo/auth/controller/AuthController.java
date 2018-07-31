package com.xrj.demo.auth.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xrj.demo.auth.service.AuthService;
import com.xrj.demo.common.model.RestResp;
import com.xrj.demo.user.model.User;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @PostMapping("/create-auth")
    public RestResp<?> createAuthenticationToken(
            @RequestBody User user) throws AuthenticationException{
        final String token = authService.loginByPhone(user.getPhone(), user.getPassword());

        // Return the token
        return RestResp.ok(token);
    }

    @GetMapping("/refresh")
    public RestResp<?> refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws AuthenticationException{
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if(refreshedToken == null) {
            return RestResp.error(null);
        } else {
            return RestResp.ok(refreshedToken);
        }
    }

    @PostMapping("/register-by-phone")
    public User registerByPhone(@RequestBody User addedUser) throws AuthenticationException{
        return authService.registerByPhone(addedUser);
    }
    
//    @PostMapping("upgrade-to-VIP")
//    public RestResp<?> upgradeToVIP(@RequestBody User addedUser) throws AuthenticationException{
//    	
//    }
    
}
