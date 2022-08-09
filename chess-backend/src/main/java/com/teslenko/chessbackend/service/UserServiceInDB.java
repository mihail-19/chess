package com.teslenko.chessbackend.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.teslenko.chessbackend.Roles;
import com.teslenko.chessbackend.db.UserCrudRepository;
import com.teslenko.chessbackend.entity.Game;
import com.teslenko.chessbackend.entity.User;
import com.teslenko.chessbackend.exception.BusyNameException;

@Service
public class UserServiceInDB implements UserDetailsService, UserService {
	private static final Logger LOG = LoggerFactory.getLogger(UserServiceInDB.class);
	private PasswordEncoder passwordEncoder;
	private UserCrudRepository userCrudRepository;
	private GameService gameService;
	@Autowired
	public UserServiceInDB(PasswordEncoder passwordEncoder, UserCrudRepository userCrudRepository, GameService gameService) {
		this.passwordEncoder = passwordEncoder;
		this.userCrudRepository = userCrudRepository;
		this.gameService = gameService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userCrudRepository.findByUsername(username).orElseThrow();
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(r -> authorities.add(new SimpleGrantedAuthority(r)));
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				authorities);
	}

	@Override
	public User add(User user) {
		LOG.info("adding user {}", user);
		if (containsUsername(user.getUsername())) {
			LOG.error("error while adding user: username {} already exists ", user.getUsername());
			throw new BusyNameException("trying to add user with existing username " + user.getUsername());
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setInvitations(new ArrayList<>());
		user.getRoles().add(Roles.ROLE_USER.toString().toString());
		user = userCrudRepository.save(user);
		
		LOG.info("user added: {}", user);
		return user;
	}

	@Override
	public User get(long id) {
		LOG.info("getting user with ID {}", id);
		User user = userCrudRepository.findById(id).orElseThrow();
		//Game game = gameService.getForUser(user.getUsername());
		//user.setGame(game);
		return user;
	}

	@Override
	public void update(User user) {
		LOG.info("updating user {}", user);
		userCrudRepository.save(user);
	}

	@Override
	public List<User> getAll() {
		LOG.info("getting all users");
		List<User> users = new ArrayList<>();
		userCrudRepository.findAll().forEach(users::add);
		users.forEach(u -> u.setGame(gameService.getForUser(u.getUsername())));
		return users;
	}

	@Override
	public User get(String username) {
		LOG.info("getting ser with username {}", username);
		User user = userCrudRepository.findByUsername(username).orElseThrow();
		//LOG.info("found user {}", user);
		//Game game = gameService.getForUser(user.getUsername());
		//user.setGame(game);
		return user;
	}

	@Override
	public boolean containsUsername(String username) {
		LOG.info("getting if contains username {}", username);
		return userCrudRepository.findByUsername(username).isPresent();
	}
	

	@Override
	public User removeGame(String username) {
		User user = get(username);
		gameService.removeUserFromGame(user);
		user.setGame(null);
		return user;
	}
	
	
	
}
