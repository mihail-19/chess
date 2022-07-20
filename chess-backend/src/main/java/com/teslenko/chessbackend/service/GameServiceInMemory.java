package com.teslenko.chessbackend.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teslenko.chessbackend.entity.ColorPolicy;
import com.teslenko.chessbackend.entity.Desk;
import com.teslenko.chessbackend.entity.Field;
import com.teslenko.chessbackend.entity.Game;
import com.teslenko.chessbackend.entity.Move;
import com.teslenko.chessbackend.entity.User;
import com.teslenko.chessbackend.exception.ChessException;

@Service
public class GameServiceInMemory implements GameService {
	private static final Logger LOG = LoggerFactory.getLogger(GameServiceInMemory.class);
	private DeskService deskService;
	private UserService userService;
	@Autowired
	public GameServiceInMemory(DeskService deskService, UserService userService) {
		this.deskService = deskService;
		this.userService = userService;
	}

	private List<Game> games = new ArrayList<>();
	private long maxId = 1;
	@Override
	public List<Game> getAll() {
		LOG.info("getting all games");
		return games;
	}

	@Override
	public Game get(long id) {
		LOG.info("getting game with ID={}", id);
		return games.stream().filter(g -> g.getId() == id).findFirst().orElseThrow();
	}

	@Override
	public Game add(Game game) {
		LOG.info("add game {}", game);
		game.setId(maxId++);
		games.add(game);
		return game;
	}

	@Override
	public void remove(long id) {
		LOG.info("remove game with ID={}", id);
		games.removeIf(g -> g.getId() == id);
	}

	@Override
	public Game add(String creatorUsername, ColorPolicy colorPolicy) {
		LOG.info("add game for creator name={}, color policy={}", creatorUsername, colorPolicy);
		Game game = new Game(userService.get(creatorUsername), colorPolicy);
		game.setId(maxId++);
		games.add(game);
		return game;
	}

	@Override
	public Game move(long id, User user, Move move) {
		LOG.info("game with ID={} move {} by {}", id, user, move);
		Game game = games.stream().filter(g -> g.getId() == id).findFirst().orElseThrow();
		game.move(user, move);
		return game;
	}

	@Override
	public Game start(long id) {
		LOG.info("starting game with ID={}", id);
		Game game = get(id);
		Desk desk = deskService.create();
		game.startGame(desk);
		return game;
	}
	
	

	@Override
	public Game add(String creatorUsername, String secondUsername, ColorPolicy colorPolicy) {
		//TODO
		LOG.info("creating a game creator {}, opponent {}, color policy {}", creatorUsername, secondUsername, colorPolicy);
		User creator = userService.get(creatorUsername);
		User opponent = userService.get(secondUsername);
		Game game = new Game(creator, colorPolicy);
		game.addPlayer(opponent);
		game.setId(maxId++);
		games.add(game);
		creator.setGame(game);
		opponent.setGame(game);
		return game;
	}

	@Override
	public Game addPlayer(long gameId, String creator, String newPlayer) {
		Game game = get(gameId);
		//TODO
		return null;
	}

	@Override
	public Game startUserGame(User user) {
		LOG.info("starting game for user {}", user);
		Game game = user.getGame();
		if(game == null) {
			LOG.error("Could not start game: user {} has no game to start", user);
			throw new ChessException("Could not start game: user has no game to start");
		}
		if(!game.getCreator().equals(user)) {
			LOG.error("Could not start game: User {} is not game creator for game {}", user, game);
			throw new ChessException("Could not start game: user is not game creator");
		}
		Desk desk = deskService.create();
		game.startGame(desk);
		return game;
	}

}
