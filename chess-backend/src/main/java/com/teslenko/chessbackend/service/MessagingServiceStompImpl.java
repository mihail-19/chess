package com.teslenko.chessbackend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.teslenko.chessbackend.entity.User;

/**
 * Sends a message to STOMP endpoint with {@link SimpMessagingTemplate}.
 * @author Mykhailo Teslenko
 *
 */
@Service
public class MessagingServiceStompImpl implements MessagingService{
	private static final Logger LOG = LoggerFactory.getLogger(MessagingServiceStompImpl.class);
	private SimpMessagingTemplate messagingTemplate;
	private static final String WEB_SOCKET_ENDPOINT = "/usrs/";
	
	@Autowired
	public MessagingServiceStompImpl(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}
	@Override
	public void sendRefreshBySocket(User user) {
		
		String url = WEB_SOCKET_ENDPOINT + user.getUsername();
		LOG.info("sending refresh msg {}", url);
		messagingTemplate.convertAndSend(WEB_SOCKET_ENDPOINT + user.getUsername(), user);
	}
}
