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
	List<User> searchForUsername(String username);
	void update(User player);
	User removeGame(String username);
	void removeUserByName(String username);
}
