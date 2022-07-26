package com.teslenko.chessbackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketListener {
	private static final Logger LOG = LoggerFactory.getLogger(WebSocketListener.class);
	
	@EventListener
	public void handleConnect(SessionConnectedEvent event) {
		LOG.info("Socket connected event {}", event);
		
	}
	
	@EventListener
	public void handleDisconnect(SessionDisconnectEvent event) {
		LOG.info("Socket disconnect event {}", event);
	}
}
