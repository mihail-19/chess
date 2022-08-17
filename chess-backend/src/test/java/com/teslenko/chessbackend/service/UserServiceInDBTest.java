package com.teslenko.chessbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.teslenko.chessbackend.entity.User;
import com.teslenko.chessbackend.exception.BusyNameException;
import com.teslenko.chessbackend.exception.ChessException;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@TestMethodOrder(OrderAnnotation.class)
public class UserServiceInDBTest {
	private static final String username = "usr1";
	@Autowired
	private UserService userService;
	
	@Test
	@Order(1)
	public void testUserAdd() {
		User user = new User();
		user.setUsername(username);
		user.setPassword("12");
		assertThrows(ChessException.class, () -> userService.add(user));
		user.setPassword("1234");
		userService.add(user);
		User userFromDB = userService.get(username);
		assertNotNull(userFromDB);
		assertEquals(userFromDB.getUsername(), userFromDB.getUsername());
	}
	
	@Test
	@Order(2)
	public void testGetAll() {
		User usr = userService.get(username);
		assertNotNull(usr);
		List<User> users = userService.getAll();
		assertEquals(1, users.size());
		assertEquals(users.get(0).getUsername(), username);
	}
	
	@Test
	@Order(3)
	public void testSearchOneUser() {
		List<User> users = userService.searchForUsername("usr1");
		assertEquals(1, users.size());
		assertTrue(users.get(0).getUsername().equals(username));
		users = userService.searchForUsername("us");
		assertEquals(1, users.size());
		assertTrue(users.get(0).getUsername().equals(username));
		users = userService.searchForUsername("sr");
		assertEquals(1, users.size());
		assertTrue(users.get(0).getUsername().equals(username));
	}
	
	@Test
	@Order(4)
	public void testSearchNotMatches() {
		List<User> users = userService.searchForUsername("ser");
		assertEquals(0, users.size());
	}
	
	@Test
	@Order(5)
	public void testSearchMultiple() {
		User second = new User("usr2", "abcd");
		userService.add(second);
		User notMatches = new User("vasia", "cdef");
		userService.add(notMatches);
		List<User> users = userService.searchForUsername("sr");
		assertEquals(2, users.size());
	}
	
	@Test
	@Order(6)
	public void testAddUserWithExistingName() {
		User user = new User(username, "4321");
		assertThrows(BusyNameException.class, () -> userService.add(user));
	}
	
	@Test
	@Order(7)
	public void removeUser() {
		assertTrue(userService.containsUsername(username));
		userService.removeUserByName(username);
		assertFalse(userService.containsUsername(username));
	}
	
	
	//Remove all users from DB
	@Test
	@Order(8)
	public void clear() {
		List<User> users = userService.getAll();
		users.forEach(u -> userService.removeUserByName(u.getUsername()));
	}
}
