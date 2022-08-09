package com.teslenko.chessbackend.service;

import java.util.List;

import com.teslenko.chessbackend.entity.ColorPolicy;
import com.teslenko.chessbackend.entity.Game;
import com.teslenko.chessbackend.entity.GameFinishProposition;
import com.teslenko.chessbackend.entity.Move;
import com.teslenko.chessbackend.entity.User;

public interface GameService {
	//TODO secure this methods for admin only (no purpose for player)
	List<Game> getAll();
	Game get(long id);
	
	Game getForUser(String username);
	Game getForUser(User user);
	Game add(Game game);
	Game add(User creator, User opponent, ColorPolicy colorPolicy);
	Game start(long id);
	Game startUserGame(User user);
	Game offerStopUserGame(User user, GameFinishProposition gameFinishCondition);
	Game acceptStopUserGame(User user);
	Game removeUserFromGame(User user);
	void remove(long id);
	Game move(long id, User user, Move move);
}
