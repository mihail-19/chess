package com.teslenko.chessbackend;

import java.util.Arrays;
import java.util.Collections;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import com.teslenko.chessbackend.web.filter.CustomAtuhorizationFilter;
import com.teslenko.chessbackend.web.filter.CustomAuthenticationFilter;

@SpringBootApplication
@EnableWebSecurity
@EnableWebSocket
public class ChessBackendApplication {
	private static final Logger LOG = LoggerFactory.getLogger(ChessBackendApplication.class);
	private UserDetailsService userDetailsService;

	public static void main(String[] args) {
		SpringApplication.run(ChessBackendApplication.class, args);
	}
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				 registry.addMapping("/**")
				 .allowCredentials(true)
				 .allowedOrigins("http://:3000");
			}
		};
	}
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		 AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
//	     authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//	     AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
	     
	     http
	     	.cors().configurationSource(corsConfigurationSource()).and()
	     	.csrf().disable()
	     	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	     	.and()
	     	.authorizeHttpRequests((auth) -> {
					auth
					.antMatchers("/login", "/users/add", "/stomp-endpoint/**").permitAll()
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
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		//configuration.applyPermitDefaultValues();
		configuration.addAllowedOrigin("http://:3000");
		configuration.setAllowedHeaders(Collections.singletonList("*"));
		configuration.setAllowedMethods(Collections.singletonList("*"));
		configuration.setAllowCredentials(true);
//		configuration.setAllowedOrigins(Arrays.asList("*"));
//		configuration.setAllowedMethods(Arrays.asList("GET","POST"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
