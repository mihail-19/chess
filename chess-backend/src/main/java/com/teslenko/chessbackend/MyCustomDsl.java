package com.teslenko.chessbackend;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import com.teslenko.chessbackend.web.filter.CustomAuthenticationFilter;
import com.teslenko.chessbackend.web.filter.JWTManager;

public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
	private JWTManager jwtManager;
	
    public MyCustomDsl(JWTManager jwtManager) {
		this.jwtManager = jwtManager;
	}

	@Override
    public void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilter(new CustomAuthenticationFilter(authenticationManager, jwtManager));
    }

    public static MyCustomDsl customDsl(JWTManager jwtManager) {
        return new MyCustomDsl(jwtManager);
    }
}