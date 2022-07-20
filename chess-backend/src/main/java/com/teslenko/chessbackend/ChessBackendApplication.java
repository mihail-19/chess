package com.teslenko.chessbackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.teslenko.chessbackend.web.filter.CustomAtuhorizationFilter;
import com.teslenko.chessbackend.web.filter.CustomAuthenticationFilter;

@SpringBootApplication
@EnableWebSecurity
public class ChessBackendApplication {
	private static final Logger LOG = LoggerFactory.getLogger(ChessBackendApplication.class);
	private UserDetailsService userDetailsService;

	public static void main(String[] args) {
		SpringApplication.run(ChessBackendApplication.class, args);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		 AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
//	     authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//	     AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
	     
	     http
	     	.cors().disable()
	     	.csrf().disable()
	     	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	     	.and()
	     	.authorizeHttpRequests((auth) -> {
					auth
					.antMatchers("/login", "/users/add").permitAll()
					.antMatchers("/games/**", "/users/**").hasAnyAuthority("ROLE_USER")
					.anyRequest().authenticated()
					.and().addFilterBefore(new CustomAtuhorizationFilter(), UsernamePasswordAuthenticationFilter.class);
				})
	     	.apply(MyCustomDsl.customDsl());
	     
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
