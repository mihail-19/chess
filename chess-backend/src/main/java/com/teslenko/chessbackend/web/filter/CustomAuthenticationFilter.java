package com.teslenko.chessbackend.web.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private static final Logger LOG = LoggerFactory.getLogger(CustomAuthenticationFilter.class);
	private AuthenticationManager authenticationManager;
	private JWTManager jwtManager;
	public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JWTManager jwtManager) {
		this.authenticationManager = authenticationManager;
		this.jwtManager = jwtManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		 // Access-Control-Allow-Origin
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", "http://178.151.21.70:3000");
        response.setHeader("Vary", "Origin");

        // Access-Control-Max-Age
        response.setHeader("Access-Control-Max-Age", "3600");

        // Access-Control-Allow-Credentials
        response.setHeader("Access-Control-Allow-Credentials", "false");
		LOG.trace("trying to authenticate username={}, password={}", username, password);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
		return authenticationManager.authenticate(token);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		User user = (User) authResult.getPrincipal();
		String accessToken= jwtManager.generateAccessToken(user, request.getServletPath().toString());
		String refreshToken= jwtManager.generateRefreshToken(user, request.getServletPath().toString());
		//response.setHeader("access_token", accessToken);
		//response.setHeader("refresh_token", refreshToken);
		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", accessToken);
		tokens.put("refresh_token", refreshToken);
		LOG.info("generated tokens {}", tokens);
		new ObjectMapper().writeValue(response.getOutputStream(), tokens);
		
	}
}
