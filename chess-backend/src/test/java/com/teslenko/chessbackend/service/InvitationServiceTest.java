package com.teslenko.chessbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.teslenko.chessbackend.entity.Invitation;
import com.teslenko.chessbackend.entity.User;
import com.teslenko.chessbackend.exception.ChessException;
import com.teslenko.chessbackend.exception.NotUserInvitationException;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@TestMethodOrder(OrderAnnotation.class)
public class InvitationServiceTest {
	private static User user1;
	private static User user2;
	@Autowired
	private UserService userService;
	@Autowired
	private InvitationService invitationService;
	
	
	private long wrongUserInvId;
	@BeforeAll
	public static void prepare() {
		 user1 = new User("user1", "1234");
		 user2 = new User("user2", "4321");
	}
	
	@Test
	@Order(1)
	public void createUsers() {
		userService.add(user1);
		userService.add(user2);
	}
	
	@Test
	@Order(2)
	public void testSendFailUnexistantUser() {
		assertThrows(ChessException.class, () -> invitationService.sendInvitation(user1.getUsername(), "aaabc"));
		assertThrows(ChessException.class, () -> invitationService.sendInvitation("dsferg4", user2.getUsername()));
		
	}
	
	@Test
	@Order(3)
	public void testSendSuccess() {
		invitationService.sendInvitation(user1.getUsername(), user2.getUsername());
		user1 = userService.get(user1.getUsername());
		user2 = userService.get(user2.getUsername());
		assertEquals(1, user1.getInvitations().size());
		assertEquals(1, user2.getInvitations().size());
		assertEquals(user1.getInvitations().get(0).getId(), user2.getInvitations().get(0).getId());
	}
	
	@Test
	@Order(4)
	public void testSendFailInvitationAlreadySent() {
		assertThrows(ChessException.class, () -> invitationService.sendInvitation(user1.getUsername(), user2.getUsername()));
		assertThrows(ChessException.class, () -> invitationService.sendInvitation(user2.getUsername(), user1.getUsername()));
	}
	
	@Test
	@Order(5)
	public void testSendFailTooManyInvitations() {
		User user3 = userService.add(new User("user3", "pwdd"));
		User user4 = userService.add(new User("user4", "pwdd14"));
		invitationService.sendInvitation(user3.getUsername(), user2.getUsername());
		assertEquals(2, userService.get(user2.getUsername()).getInvitations().size());
		assertThrows(ChessException.class, () -> invitationService.sendInvitation(user4.getUsername(), user2.getUsername()));
	}
	
	@Test
	@Order(6)
	public void testRemoveFailNotUserInvitation() {
		user2 = userService.get(user2.getUsername());
		assertThrows(NotUserInvitationException.class, () -> invitationService.removeInvitation(user2.getUsername(), 12345));
		wrongUserInvId = -1;
		for(Invitation inv : user2.getInvitations()) {
			if(!inv.getSenderUsername().equals(user1.getUsername())) {
				wrongUserInvId = inv.getId();
				break;
			}
		}
		assertThrows(NotUserInvitationException.class, () -> invitationService.removeInvitation(user1.getUsername(), wrongUserInvId));
	}
	
	@Test
	@Order(7)
	public void testRemoveInvsSuccess() {
		user2 = userService.get(user2.getUsername());
		long id = -1;
		for(Invitation inv : user2.getInvitations()) {
			if(!inv.getSenderUsername().equals(user1.getUsername())) {
				id = inv.getId();
				break;
			}
		}
		invitationService.removeInvitation(user2.getUsername(), id);
		assertEquals(1, userService.get(user2.getUsername()).getInvitations().size());
	}
	
	//Remove all users from DB
	@Test
	@Order(8)
	public void clear() {
		List<User> users = userService.getAll();
		users.forEach(u -> userService.removeUserByName(u.getUsername()));
	}
}
