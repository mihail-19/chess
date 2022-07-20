package com.teslenko.chessbackend.service;

import java.util.List;

import com.teslenko.chessbackend.entity.Game;
import com.teslenko.chessbackend.entity.User;

public interface UserService {
	User add(User player);
	User get(long id);
	User get(String username);
	boolean containsUsername(String username);
	List<User> getAll();
	void update(User player);
	void sendRefreshBySocket(User user);
}
