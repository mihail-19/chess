package com.teslenko.chessbackend.web;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teslenko.chessbackend.entity.Game;
import com.teslenko.chessbackend.service.GameService;
import com.teslenko.chessbackend.service.InvitationService;

@RestController
@RequestMapping("/actions/invitation")
public class UserActionsInvitationsController {
	private static final Logger LOG = LoggerFactory.getLogger(UserActionsInvitationsController.class);
	@Autowired
	private InvitationService invitationService;
	@Autowired
	private GameService gameService;
	
	@PostMapping("/{id}/deny")
	public void denyEnvitation(Principal principal, @PathVariable long id) {
		LOG.info("deny invitation {} for user {} ", id, principal.getName());
		invitationService.removeInvitation(principal.getName(), id);
	}
	
	@PostMapping("/{id}/accept")
	public Game acceptInvitation(Principal principal, @PathVariable long id) {
		LOG.info("accept invitation {} by user {}", id, principal.getName());
		Game game = invitationService.acceptInvitation(principal.getName(), id);
		return game;
	}
	
	@PostMapping("/send")
	public void sendIvitation(Principal principal, @RequestParam String recepient) {
		LOG.info("create an invitation from {} to {}", principal.getName(), recepient);
		invitationService.sendInvitation(principal.getName(), recepient);
	}

}
