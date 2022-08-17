package com.teslenko.chessbackend.service;

import org.springframework.stereotype.Service;

import com.teslenko.chessbackend.entity.User;

@Service
public class UserValidationServiceImpl implements UserValidationService{
	
	private static final int MIN_NAME_LENGTH = 1;
	private static final int MAX_NAME_LENGTH = 20;
	private static final int MIN_PWD_LENGTH = 4;
	private static final int MAX_PWD_LENGTH = 30;
	
	@Override
	public boolean isValidUser(User user) {
		if(user == null) {
			return false;
		}
		if(!isValidUsername(user.getUsername())) {
			return false;
		}
		if(!isValidPwd(user.getPassword())) {
			return false;
		}
		return true;
	}
	
	private boolean isValidPwd(String pwd) {
		if(pwd == null) {
			return false;
		}
		if(pwd.length() > MAX_PWD_LENGTH || pwd.length() < MIN_PWD_LENGTH) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isValidUsername(String username) {
		if(username == null) {
			return false;
		}
		if(username.length() > MAX_NAME_LENGTH || username.length() < MIN_NAME_LENGTH) {
			return false;
		}
		return true;
	}

}
