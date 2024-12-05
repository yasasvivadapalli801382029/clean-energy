package com.example.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.APIMessageResponse;
import com.example.dto.UserLoginRequest;
import com.example.service.JWTService;
import com.example.service.UserLoginService;

@RestController
public class UserLoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginController.class);
    
    @Autowired
    private UserLoginService userLoginService;


    /**
     * Authenticate the user and return a JWT token.
     */
    @PostMapping(value="/login")
    public ResponseEntity<APIMessageResponse> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
    	
    	return userLoginService.loginUser(userLoginRequest);
        
    }
    
    @PostMapping("/register")
    public ResponseEntity<APIMessageResponse> signUpUser(@RequestBody UserLoginRequest request) {
    	
    	return userLoginService.signUpUser(request);
    }
 

    /**
     * Welcome message endpoint.
     */
    @GetMapping("/welcome")
    public String welcomeSpringBoot() {
        return "Welcome to Clean Energy Application";
    }
    
    @GetMapping("/hello")
    public String welcome2() {
    	return "welcome2";
    }
}
