package com.teslenko.chessbackend.dto;

import com.teslenko.chessbackend.entity.User;

public class UserDto {
	private String username;
	private String password;
	
	public User toUser() {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		return user;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
