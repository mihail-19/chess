package com.teslenko.chessbackend.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class CustomAtuhorizationFilter extends OncePerRequestFilter{
	private static final Logger LOG = LoggerFactory.getLogger(CustomAtuhorizationFilter.class);
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(request.getServletPath().equals("/login") || request.getServletPath().equals("/users/add")) {
			filterChain.doFilter(request, response);
		} else {
			String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			if(authHeader != null && authHeader.startsWith("Bearer ")) {
				try {
					String token = authHeader.substring("Bearer ".length());
					Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
					JWTVerifier verifier = JWT.require(algorithm).build();
					DecodedJWT decodedJWT = verifier.verify(token);
					String username = decodedJWT.getSubject();
					String [] roles = decodedJWT.getClaim("roles").asArray(String.class);
					Collection<SimpleGrantedAuthority> auths = new ArrayList<>();
					Arrays.stream(roles).forEach(r -> auths.add(new SimpleGrantedAuthority(r)));
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, auths);
					SecurityContextHolder.getContext().setAuthentication(authToken);
					filterChain.doFilter(request, response);
				}catch(Exception e) {
					LOG.error("Error logging in : {} " + e.getMessage());
					response.setHeader("error", e.getMessage());
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
				}
						
			} else {
				filterChain.doFilter(request, response);
			}
		}
	}

}
