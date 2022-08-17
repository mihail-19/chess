package com.teslenko.chessbackend.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.teslenko.chessbackend.entity.User;

public class UserValidationServiceTest {
	UserValidationService validator = new UserValidationServiceImpl();
	
	@Test
	public void testEmptyName() {
		String name = null;
		assertFalse(validator.isValidUsername(name));
		name = "";
		assertFalse(validator.isValidUsername(name));
	}
	
	@Test
	public void testTooBigUsername() {
		String name = "ewfweeeeeeeeeeeeeeewffffffffffffffffweeeeeeeeeeeeeeeeeeeeeeeeeeeeeeewaaaaaaaaa";
		assertFalse(validator.isValidUsername(name));
	}
	
	@Test
	public void testOKName() {
		String name = "Joh`n Noveber";
		assertTrue(validator.isValidUsername(name));
	}
	
	@Test
	public void incorrectPwd() {
		User user = new User("Nick", null);
		assertFalse(validator.isValidUser(user));
		user = new User("John", "1");
		assertFalse(validator.isValidUser(user));
	}
	
	@Test
	public void validUser() {
		User user = new User("Євген", "152fd^^");
		assertTrue(validator.isValidUser(user));
	}
}
