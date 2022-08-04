package com.teslenko.chessbackend.web;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teslenko.chessbackend.entity.ColorPolicy;
import com.teslenko.chessbackend.entity.Game;
import com.teslenko.chessbackend.entity.GameFinishProposition;
import com.teslenko.chessbackend.entity.Move;
import com.teslenko.chessbackend.entity.User;
import com.teslenko.chessbackend.exception.ChessException;
import com.teslenko.chessbackend.service.GameService;
import com.teslenko.chessbackend.service.UserService;


/**
 * Actions for game. Game is chosen for given user
 * @author Mykhailo Teslenko
 *
 */
@RestController
@RequestMapping("/actions/game")
public class UserActionsGameController {
	private static final Logger LOG = LoggerFactory.getLogger(UserActionsGameController.class);
	private GameService gameService;
	private UserService userService;
	@Autowired
	public UserActionsGameController(GameService gameService, UserService userService) {
		this.gameService = gameService;
		this.userService = userService;
	}
	
	@GetMapping
	public List<Game> getAll(){
		LOG.info("getting all games");
		return gameService.getAll();
	}
	
	@GetMapping("/for-user")
	public Game getGameForUser(Principal principal) {
		LOG.info("getting game for user {}", principal.getName());
		return gameService.getForUser(principal.getName());
	}
	
	@PostMapping("/create")
	public Game createGame(@RequestParam ColorPolicy colorPolicy, Principal principal) {
		LOG.info("creating a game by {} with color policy {}", principal.getName(), colorPolicy);
		String username = principal.getName();
		Game game = gameService.add(username, colorPolicy);
		return game;
	}
	
		
	
	@GetMapping("/start")
	public Game start(Principal principal) {
		LOG.info("starting game by {}", principal.getName());
		User user = userService.get(principal.getName());
		Game game = gameService.startUserGame(user);
		return game;
	}
	
	@PostMapping("/move")
	public Game move(Principal principal, @RequestBody Move move) {
		LOG.info("move {} by {}", move, principal.getName());
		User user = userService.get(principal.getName());
		Game game = user.getGame();
		if(game == null) {
			LOG.error("could not make move: player {} has no game", user);
			throw new ChessException("could not make move: player has no game");
		}
		game = gameService.move(game.getId(), user, move);
		return game;
	}
	
	@PostMapping("/stop-offer")
	public Game stop(Principal principal, @RequestParam Boolean isDraw) {
		LOG.info("stop game by {}", principal.getName());
		User user = userService.get(principal.getName());
		GameFinishProposition offer = new GameFinishProposition(user.getUsername(), isDraw);
		Game game = gameService.offerStopUserGame(user, offer);
		return game;
	}
	@GetMapping("/accept-stop")
	public Game stop(Principal principal) {
		LOG.info("stop game by {}", principal.getName());
		User user = userService.get(principal.getName());
		Game game = gameService.acceptStopUserGame(user);
		return game;
	}
	
}
