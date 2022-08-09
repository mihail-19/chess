package com.teslenko.chessbackend.web;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teslenko.chessbackend.dto.UserDto;
import com.teslenko.chessbackend.entity.User;
import com.teslenko.chessbackend.exception.BusyNameException;
import com.teslenko.chessbackend.service.UserService;


@RestController
@RequestMapping("users")
public class UserController {
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService;
	
	
	@GetMapping
	public List<User> getAllUsernames(){
		//return userService.getAll().stream().map(u -> u.getUsername()).collect(Collectors.toList());
		LOG.info("getting all users");
		return userService.getAll();
	}
	@GetMapping("/current")
	public User getCurrentUser(Principal principal) {
		LOG.info("getting current user {}", principal.getName());
		User user = userService.get(principal.getName());
		LOG.info("found user {}", user);
		LOG.info("user game {}", user.getGame());
		return user;
	}
	@GetMapping("/{id}")
	public User getById(@PathVariable long id) {
		User user = null;
		try{
			user = userService.get(id);
		} catch(NoSuchElementException e) {
			
		}
		return user;
	}
	
	@GetMapping("/name")
	public User getByName(@RequestParam String name) {
		User user = null;
		try{
			user = userService.get(name);
		} catch(NoSuchElementException nse) {
			
		}
		return user;
	}
	
	@PostMapping("/add")
	public void addUser(@RequestBody UserDto userDto) {
		LOG.info("adding a user {}", userDto);
		User user = userDto.toUser();
		if(userService.containsUsername(user.getUsername())) {
			throw new BusyNameException("username " + user.getUsername() + " is already taken");
		}
		userService.add(user);
	}
	
	
}
