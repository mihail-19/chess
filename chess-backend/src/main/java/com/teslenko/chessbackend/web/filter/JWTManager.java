package com.teslenko.chessbackend.web.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JWTManager {
	private static final Logger LOG = LoggerFactory.getLogger(JWTManager.class);
	private Algorithm algorithm;
	public JWTManager(@Value("${chess.jwt.secret}") String secret) {
		LOG.trace("chess secret {}", secret);
		algorithm = Algorithm.HMAC256(secret.getBytes());
	}
	public String generateAccessToken(User user, String requestUrl) {
		String accessToken= JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 10*24*60*60*1000))
				.withIssuer(requestUrl)
				.withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(algorithm);
		return accessToken;
	}
	public String generateRefreshToken(User user, String requestUrl) {
		String refreshToken= JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 20*24*60*60*1000))
				.withIssuer(requestUrl)
				.withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(algorithm);
		return refreshToken;
	}
	
	public UsernamePasswordAuthenticationToken authorizeToken(String jwtToken) {
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT decodedJWT = verifier.verify(jwtToken);
		String username = decodedJWT.getSubject();
		String [] roles = decodedJWT.getClaim("roles").asArray(String.class);
		Collection<SimpleGrantedAuthority> auths = new ArrayList<>();
		Arrays.stream(roles).forEach(r -> auths.add(new SimpleGrantedAuthority(r)));
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, auths);
		return authToken;
	}
}
