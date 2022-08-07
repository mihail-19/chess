package com.teslenko.chessbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.teslenko.chessbackend.entity.ColorPolicy;
import com.teslenko.chessbackend.entity.Column;
import com.teslenko.chessbackend.entity.Field;
import com.teslenko.chessbackend.entity.Game;
import com.teslenko.chessbackend.entity.Move;
import com.teslenko.chessbackend.entity.User;

public class GameServiceInMemoryTest {
	GameService gameService = new GameServiceInMemory(new StandartDeskService(), 
			Mockito.mock(MessagingService.class));
	
	@Test
	public void testGameAdd() {
		assertEquals(gameService.getAll().size(), 0);
		Game game = new Game(null, null);
		gameService.add(game);
		assertEquals(gameService.getAll().size(), 1);
	}
	
	@Test
	public void testGameGet() {
		Game game = new Game(null, null);
		game = gameService.add(game);
		assertEquals(gameService.getAll().size(), 1);
		assertEquals(gameService.get(game.getId()).getId(), 1);
	}
	
	@Test
	public void testRemove() {
		Game game = new Game(null, null);
		game = gameService.add(game);
		assertEquals(gameService.getAll().size(), 1);
		gameService.remove(game.getId());
		assertEquals(gameService.getAll().size(), 0);
	}
	
	@Test
	public void testMove() {
		User white = new User();
		white.setId(1);
		white.setUsername("player 1");
		User black = new User();
		black.setId(2);
		black.setUsername("player 2");
		Game game = gameService.add(white, black, ColorPolicy.WHITE_CREATOR);
		game.startGame(new StandartDeskService().create());
		System.out.println(game.getMoverUsername());
		gameService.move(game.getId(), white, new Move(new Field(2, Column.e), new Field(4, Column.e)));
		assertTrue(gameService.get(game.getId()).getDesk().getFields().containsKey(new Field(4, Column.e)));
	}
}
