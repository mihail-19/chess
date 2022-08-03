package com.teslenko.chessbackend.service;

import java.util.List;

import com.teslenko.chessbackend.entity.ColorPolicy;
import com.teslenko.chessbackend.entity.Game;
import com.teslenko.chessbackend.entity.GameFinishProposition;
import com.teslenko.chessbackend.entity.Move;
import com.teslenko.chessbackend.entity.User;

public interface GameService {
	List<Game> getAll();
	Game get(long id);
	Game getForUser(String username);
	Game add(Game game);
	Game add(String creatorUsername, ColorPolicy colorPolicy);
	Game add(User creator, User opponent, ColorPolicy colorPolicy);
	Game addPlayer(long gameId, String creator, String newPlayer);
	Game start(long id);
	Game startUserGame(User user);
	Game offerStopUserGame(User user, GameFinishProposition gameFinishCondition);
	Game acceptStopUserGame(User user);
	void remove(long id);
	Game move(long id, User user, Move move);
}
