package com.teslenko.chessbackend.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teslenko.chessbackend.entity.Color;
import com.teslenko.chessbackend.entity.ColorPolicy;
import com.teslenko.chessbackend.entity.Desk;
import com.teslenko.chessbackend.entity.Game;
import com.teslenko.chessbackend.entity.GameFinishProposition;
import com.teslenko.chessbackend.entity.Move;
import com.teslenko.chessbackend.entity.User;
import com.teslenko.chessbackend.entity.Winner;
import com.teslenko.chessbackend.exception.ChessException;
import com.teslenko.chessbackend.exception.UnautorizedPlayerException;

//@Service
public class GameServiceInMemory implements GameService {
	private static final Logger LOG = LoggerFactory.getLogger(GameServiceInMemory.class);
	private DeskService deskService;
	private MessagingService messagingService;
	@Autowired
	public GameServiceInMemory(DeskService deskService,  MessagingService messagingService) {
		this.deskService = deskService;
		this.messagingService = messagingService;
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
	public Game getForUser(String username) {
		LOG.info("getting game for user {}", username);
		return games.stream()
				.filter(g -> username.equals(g.getCreator().getUsername()) || username.equals(g.getOpponent().getUsername()))
				.findFirst().orElse(null);
	}

	@Override
	public Game getForUser(User user) {
		return getForUser(user.getUsername());
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
	public Game move(long id, User user, Move move) {
		LOG.info("game with ID={} move {} by {}", id, user, move);
		Game game = games.stream().filter(g -> g.getId() == id).findFirst().orElseThrow();
		game.move(user, move);
		messagingService.sendRefreshBySocket(game.getCreator());
		messagingService.sendRefreshBySocket(game.getOpponent());
		return game;
	}

	@Override
	public Game start(long id) {
		LOG.info("starting game with ID={}", id);
		Game game = get(id);
		Desk desk = deskService.create();
		game.startGame(desk);
		messagingService.sendRefreshBySocket(game.getCreator());
		messagingService.sendRefreshBySocket(game.getOpponent());
		return game;
	}
	
	

	@Override
	public Game add(User creator, User opponent, ColorPolicy colorPolicy) {
		//TODO
		LOG.info("creating a game creator {}, opponent {}, color policy {}", creator.getUsername(), opponent.getUsername(), colorPolicy);
		Game game = new Game(creator, colorPolicy);
		game.addPlayer(opponent);
		game.setId(maxId++);
		games.add(game);
		creator.setGame(game);
		opponent.setGame(game);
		return game;
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
		messagingService.sendRefreshBySocket(game.getCreator());
		messagingService.sendRefreshBySocket(game.getOpponent());
		return game;
	}

	@Override
	public Game offerStopUserGame(User user, GameFinishProposition finishProposition) {
		LOG.info("sending offer to stop game from {}, offer: {}", user, finishProposition);
		Game game = user.getGame();
		Color userColor = game.getUserColor(user);
		if(!user.getUsername().equals(finishProposition.getSenderUsername())){
			LOG.error("error while sending stop game invitation: user name {} and porposition name {} are not equal", user, finishProposition.getSenderUsername());
			throw new UnautorizedPlayerException("error while sending stop game invitation: user name and porposition name are not equal");
		}
		game.setGameFinishProposition(finishProposition);
		if(!finishProposition.getIsDraw()) {
			Winner winner = Winner.valueOf(userColor.other().toString());
			game.finishGame(winner);
		}
		messagingService.sendRefreshBySocket(game.getCreator());
		messagingService.sendRefreshBySocket(game.getOpponent());
		return game;
	}

	@Override
	public Game acceptStopUserGame(User user) {
		LOG.info("accepting stop game by {}", user);
		Game game = user.getGame();
		GameFinishProposition finishProps = game.getGameFinishProposition();
		if(finishProps == null) {
			LOG.error("error accepting stop game: no offer was send in game {}", game);
			throw new ChessException("error accepting stop game: no offer was send in game");
		}
		if(finishProps.getSenderUsername().equals(user.getUsername())) {
			LOG.error("error accepting stop game: could not accept self send props by {} in game {}", user, game);
			throw new ChessException("error accepting stop game: could not accept self send props");
		}
		Winner winner = Winner.draw;
		if(!finishProps.getIsDraw()) {
			if(game.getCreator().equals(user)) {
				if(game.getCreatorColor() == Color.white) {
					winner = Winner.white;
				} else {
					winner = Winner.black;
				}
			} else {
				if(game.getCreatorColor() == Color.white) {
					winner = Winner.black;
				} else {
					winner = Winner.white;
				}
			}
		} 
		game.finishGame(winner);
		messagingService.sendRefreshBySocket(game.getCreator());
		messagingService.sendRefreshBySocket(game.getOpponent());
		return game;
	}

	@Override
	public void removeUserFromGame(User user) {
		Game game = getForUser(user.getUsername());
		if(game != null && game.getIsFinished()) {
			if(game.getCreator().getUsername().equals(user.getUsername())) {
				game.setCreator(null);
			} else {
				game.setOpponent(null);
			}
		} else {
			LOG.error("failed to remove user {} from game: game is not finished, game {}", user, game);
			throw new ChessException("failed to remove user from game: game is not finished");
		}
		if(game.getCreator() == null && game.getOpponent() == null) {
			remove(game.getId());
		}
	}
	
	
}
