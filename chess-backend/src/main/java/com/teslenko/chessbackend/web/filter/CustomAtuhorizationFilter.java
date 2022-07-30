package com.teslenko.chessbackend.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


public class CustomAtuhorizationFilter extends OncePerRequestFilter{
	private static final Logger LOG = LoggerFactory.getLogger(CustomAtuhorizationFilter.class);
	private JWTManager jwtManager;
	
	public CustomAtuhorizationFilter(JWTManager jwtManager) {
		this.jwtManager = jwtManager;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		LOG.info("request authorization {}", request.getServletPath());
		if(request.getServletPath().equals("/login") || request.getServletPath().equals("/users/add") || request.getServletPath().startsWith("/stomp")) {
			LOG.info("don't need to authorize request {}", request.getServletPath());
			filterChain.doFilter(request, response);
		} else {
			String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			if(authHeader != null && authHeader.startsWith("Bearer ")) {
				try {
					String token = authHeader.substring("Bearer ".length());
					UsernamePasswordAuthenticationToken authToken = jwtManager.authorizeToken(token);
					SecurityContextHolder.getContext().setAuthentication(authToken);
					LOG.info("authorization success for {}", authToken.getName());
					filterChain.doFilter(request, response);
				}catch(Exception e) {
					LOG.error("Error logging in : {} " + e.getMessage());
					response.setHeader("error", e.getMessage());
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
				}
						
			} else {
				LOG.error("Error loggin in: no auth header {} for request {}", authHeader, request.getServletPath());
				filterChain.doFilter(request, response);
			}
		}
	}

}
