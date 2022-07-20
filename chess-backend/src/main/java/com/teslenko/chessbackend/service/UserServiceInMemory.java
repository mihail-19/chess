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

import com.teslenko.chessbackend.entity.User;
import com.teslenko.chessbackend.exception.BusyNameException;

@Service
public class UserServiceInMemory implements UserDetailsService, UserService {
	private static final Logger LOG = LoggerFactory.getLogger(UserServiceInMemory.class);
	private PasswordEncoder passwordEncoder;
	private List<User> users = new ArrayList<>();
	private long maxId = 1;
	private SimpMessagingTemplate messagingTemplate;
	private static final String WEB_SOCKET_ENDPOINT = "/socket/";
	private static final String WEB_SOCKET_REFRESH_MESSAGE = "refresh";
	@Autowired
	public UserServiceInMemory(PasswordEncoder passwordEncoder, SimpMessagingTemplate messagingTemplate) {
		this.passwordEncoder = passwordEncoder;
		this.messagingTemplate = messagingTemplate;
		
		//for testing only
		User user1 = new User();
		user1.setUsername("user1");
		user1.setPassword("1234");
		add(user1);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst()
				.orElseThrow(() -> new UsernameNotFoundException("not found user " + username));
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
		user.setId(maxId++);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setInvitations(new ArrayList<>());
		users.add(user);
		LOG.info("user added: {}", user);
		return user;
	}

	@Override
	public User get(long id) {
		LOG.info("getting user with ID {}", id);
		return users.stream().filter(p -> p.getId() == id).findFirst().orElseThrow();
	}

	@Override
	public void update(User player) {
		LOG.info("updating user {}", player);
		users.removeIf(p -> p.getId() == player.getId());
		users.add(player);
	}

	@Override
	public List<User> getAll() {
		LOG.info("getting all users");
		return users;
	}

	@Override
	public User get(String username) {
		LOG.info("getting ser with username {}", username);
		return users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElseThrow();
	}

	@Override
	public boolean containsUsername(String username) {
		LOG.info("getting if contains username {}", username);
		return users.stream().filter(u -> u.getUsername().equals(username)).findAny().isPresent();
	}
	
	/**
	 * Sending message indicate whether changes were performed for user with 
	 * given username
	 */
	@Override
	public void sendRefreshBySocket(User user) {
		LOG.info("sending user refresh message by web socket for user {}", user);
		messagingTemplate.convertAndSend(WEB_SOCKET_ENDPOINT + user.getUsername(), WEB_SOCKET_REFRESH_MESSAGE);
	}
	
	
	
}
