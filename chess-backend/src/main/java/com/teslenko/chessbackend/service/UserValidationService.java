package com.teslenko.chessbackend.service;

import com.teslenko.chessbackend.entity.User;

public interface UserValidationService {
	boolean isValidUser(User user);
	boolean isValidUsername(String username);
}
