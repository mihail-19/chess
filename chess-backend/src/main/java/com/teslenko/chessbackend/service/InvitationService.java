package com.teslenko.chessbackend.service;

import java.util.List;

import com.teslenko.chessbackend.entity.Game;
import com.teslenko.chessbackend.entity.Invitation;

public interface InvitationService {
	void sendInvitation(String senderName, String recepientName);
	void removeInvitation(String username, long id);
	Game acceptInvitation(String recepient, long id);
	Invitation getById(long id);
	List<Invitation> getForUsername(String username);
}
