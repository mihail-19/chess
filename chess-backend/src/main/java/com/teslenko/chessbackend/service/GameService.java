package com.teslenko.chessbackend.service;

import java.util.List;

import com.teslenko.chessbackend.entity.ColorPolicy;
import com.teslenko.chessbackend.entity.Field;
import com.teslenko.chessbackend.entity.Game;
import com.teslenko.chessbackend.entity.Move;
import com.teslenko.chessbackend.entity.User;

public interface GameService {
	List<Game> getAll();
	Game get(long id);
	Game add(Game game);
	Game add(String creatorUsername, ColorPolicy colorPolicy);
	Game add(String creatorUsername, String secondUsername, ColorPolicy colorPolicy);
	Game addPlayer(long gameId, String creator, String newPlayer);
	Game start(long id);
	Game startUserGame(User user);
	void remove(long id);
	Game move(long id, User user, Move move);
}