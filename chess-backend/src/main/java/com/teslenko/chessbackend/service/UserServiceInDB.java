package com.teslenko.chessbackend.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.teslenko.chessbackend.Roles;
import com.teslenko.chessbackend.db.UserCrudRepository;
import com.teslenko.chessbackend.entity.User;
import com.teslenko.chessbackend.exception.BusyNameException;
import com.teslenko.chessbackend.exception.ChessException;

@Service
public class UserServiceInDB implements UserDetailsService, UserService {
	private static final Logger LOG = LoggerFactory.getLogger(UserServiceInDB.class);
	private PasswordEncoder passwordEncoder;
	private UserCrudRepository userCrudRepository;
	private GameService gameService;
	private UserValidationService userValidationService;
	@Autowired
	public UserServiceInDB(PasswordEncoder passwordEncoder, UserCrudRepository userCrudRepository, GameService gameService, UserValidationService userValidationService) {
		this.passwordEncoder = passwordEncoder;
		this.userCrudRepository = userCrudRepository;
		this.gameService = gameService;
		this.userValidationService = userValidationService;
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
		if(!userValidationService.isValidUser(user)) {
			LOG.error("error adding user: invalid username or password {}", user);
			throw new ChessException("invalid username or password of user " + user);
		}
		if (containsUsername(user.getUsername())) {
			LOG.error("error while adding user: username {} already exists ", user.getUsername());
			throw new BusyNameException("trying to add user with existing username " + user.getUsername());
		}
		setupUser(user);
		user = userCrudRepository.save(user);
		LOG.info("user added: {}", user);
		return user;
	}
	/*
	 * Setup for new user. Password encoding, roles adding etc
	 */
	private void setupUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setInvitations(new ArrayList<>());
		user.getRoles().add(Roles.ROLE_USER.toString().toString());
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
		Iterable<User> usersIt = userCrudRepository.findAll();
		Iterator<User> it = usersIt.iterator();
		while(it.hasNext()) {
			users.add(it.next());
		}
		return users;
	}

	@Override
	@Transactional
	public User get(String username) {
		LOG.info("getting ser with username {}", username);
		User user = userCrudRepository.findByUsername(username).orElseThrow();
		//To fetch invitations
		user.getInvitations();
		LOG.info("found user {}", user);
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
	@Transactional
	public User removeGame(String username) {
		User user = get(username);
		gameService.removeUserFromGame(user);
		user.setGame(null);
		userCrudRepository.save(user);
		return user;
	}

	@Override
	public void removeUserByName(String username) {
		Optional<User> user = userCrudRepository.findByUsername(username);
		if(user.isPresent()) {
			userCrudRepository.deleteById(user.get().getId());
		}
	}

	@Override
	public List<User> searchForUsername(String username) {
		List<User> users = getAll();
		return users.stream()
				.filter(u -> {
					return u.getUsername().toLowerCase().contains(username.toLowerCase());
				})
				.collect(Collectors.toList());
	}
	
	
	
	
	
}
