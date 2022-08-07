package com.teslenko.chessbackend.service;

import com.teslenko.chessbackend.entity.User;

public interface MessagingService {
	/**
	 * Sending message indicate whether changes were performed for user with 
	 * given {@link User}
	 */
	public void sendRefreshBySocket(User user);
}
