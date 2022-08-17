package com.teslenko.chessbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.teslenko.chessbackend.entity.Column;
import com.teslenko.chessbackend.entity.Field;
import com.teslenko.chessbackend.entity.Game;
import com.teslenko.chessbackend.entity.GameFinishProposition;
import com.teslenko.chessbackend.entity.Move;
import com.teslenko.chessbackend.entity.User;
import com.teslenko.chessbackend.exception.ChessException;
import com.teslenko.chessbackend.exception.ImpossibleMoveException;


/**
 * Game flow test - create users, send invitation, accept it, start game, perform move, offer draw, accept draw, send new invitation.
 * @author Mykhailo Teslenko
 *
 */
@SpringBootTest
@TestPropertySource("/application-test.properties")
@TestMethodOrder(OrderAnnotation.class)
public class GameFlowTest{
	@Autowired
	private UserService userService;
	@Autowired
	private GameService gameService;
	@Autowired
	private InvitationService invitationService;
	
	private static User user1;
	private static User user2;
	@BeforeAll
	public static void prepare() {
		 user1 = new User("user1", "1234");
		 user2 = new User("user2", "4321");
	}
	
	
	@Test
	@Order(1)
	public void sendInvitation() {
		assertEquals(0, userService.getAll().size());
		userService.add(user1);
		userService.add(user2);
		invitationService.sendInvitation(user1.getUsername(), user2.getUsername());
		user1 = userService.get(user1.getUsername());
		user2 = userService.get(user2.getUsername());
		assertEquals(1, user1.getInvitations().size());
		assertEquals(1, user2.getInvitations().size());
		System.out.println(user2.getUsername());
		System.out.println("invs before" + invitationService.getAll());
		
	}
	
	@Test
	@Order(2)
	public void acceptInvitation() {
		System.out.println(invitationService.getAll());
		System.out.println(userService.get(user2.getUsername()).getUsername());
		invitationService.acceptInvitation(user2.getUsername(), user2.getInvitations().get(0).getId());
		user1 = userService.get(user1.getUsername());
		user2 = userService.get(user2.getUsername());
		assertNotNull(user1.getGame());
		assertNotNull(user2.getGame());
		assertEquals(user1.getGame().getId(), user2.getGame().getId());
	}
	
	@Test
	@Order(3)
	public void startGame() {
		Game game = user1.getGame();
		assertFalse(game.getIsStarted());
		gameService.startUserGame(user1);
		user1 = userService.get(user1.getUsername());
		user2 = userService.get(user2.getUsername());
		assertTrue(user1.getGame().getIsStarted());
		assertTrue(user2.getGame().getIsStarted());
	}
	
	@Test
	@Order(4)
	public void moveWhite() {
		Field from = new Field(2, Column.e);
		Field to = new Field(4, Column.e);
		gameService.move(user1.getGame().getId(), user1, new Move(from, to));
		user2 = userService.get(user2.getUsername());
		Game game = user2.getGame();
		assertTrue(game.getDesk().getFields().containsKey(to));
		assertFalse(game.getDesk().getFields().containsKey(from));
	}
	
	@Test
	@Order(5)
	public void moveFailFromIncorrectColor() {
		assertThrows(ImpossibleMoveException.class,() -> gameService.move(user2.getGame().getId(), user1, 
															new Move(new Field(2, Column.e), new Field(3, Column.e))));
	}
	
	@Test
	@Order(6)
	public void acceptDrawFailDrawNotOfferedSender() {
		assertThrows(ChessException.class, () -> gameService.acceptStopUserGame(userService.get(user1.getUsername())));
	}
	
	@Test
	@Order(7)
	public void sendDraw() {
		gameService.offerStopUserGame(user2, new GameFinishProposition(user2.getUsername(), true));
		user1 = userService.get(user1.getUsername());
		assertNotNull(user1.getGame().getGameFinishProposition());
		assertEquals(user1.getGame().getGameFinishProposition().getSenderUsername(), user2.getUsername());
	}
	
	@Test
	@Order(8)
	public void acceptDrawFailBySender() {
		assertThrows(ChessException.class, () -> gameService.acceptStopUserGame(userService.get(user2.getUsername())));
	}
	
	@Test
	@Order(9)
	public void acceptDraw() {
		gameService.acceptStopUserGame(userService.get(user1.getUsername()));
		user2 = userService.get(user2.getUsername());
		assertTrue(user2.getGame().getIsFinished());
		assertTrue(userService.get(user1.getUsername()).getGame().getIsFinished());
	}
	
	@Test
	@Order(10)
	public void testRemoveUser() {
		userService.removeGame(user2.getUsername());
		user2 = userService.get(user2.getUsername());
		Game game = user2.getGame();
		System.out.println(game);
		assertNull(game);
		assertNull(userService.get(user1.getUsername()).getGame().getOpponent());
	}
	
	@Test
	@Order(11)
	public void testGameIsNullAfterOtherInvitation() {
		user1 = userService.get(user1.getUsername());
		Game game = user1.getGame();
		invitationService.sendInvitation(user1.getUsername(), user2.getUsername());
		invitationService.acceptInvitation(user2.getUsername(), userService.get(user2.getUsername()).getInvitations().get(0).getId());
		assertNull(gameService.get(game.getId()));
	}
	
	//Remove all users from DB
		@Test
		@Order(12)
		public void clear() {
			List<User> users = userService.getAll();
			users.forEach(u -> userService.removeUserByName(u.getUsername()));
		}
}
