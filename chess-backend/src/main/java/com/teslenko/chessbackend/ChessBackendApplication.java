package com.teslenko.chessbackend;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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
import com.teslenko.chessbackend.web.filter.JWTManager;

@SpringBootApplication
@EnableWebSecurity
@EnableWebSocket
public class ChessBackendApplication {
	
	@Value("${chess.cors.url}")
	private String corsURL;
	@Autowired
	private JWTManager jwtManager;
	
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
				 .allowedOrigins(corsURL);
			}
		};
	}
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	     
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
					.and().addFilterBefore(new CustomAtuhorizationFilter(jwtManager), UsernamePasswordAuthenticationFilter.class);
				})
	     	.apply(MyCustomDsl.customDsl(jwtManager));
	     
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin(corsURL);
		configuration.setAllowedHeaders(Collections.singletonList("*"));
		configuration.setAllowedMethods(Collections.singletonList("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
