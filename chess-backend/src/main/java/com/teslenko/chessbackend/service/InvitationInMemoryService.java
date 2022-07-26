package com.teslenko.chessbackend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teslenko.chessbackend.entity.ColorPolicy;
import com.teslenko.chessbackend.entity.Game;
import com.teslenko.chessbackend.entity.Invitation;
import com.teslenko.chessbackend.entity.User;
import com.teslenko.chessbackend.exception.ChessException;
import com.teslenko.chessbackend.exception.NotUserInvitationException;

@Service
public class InvitationInMemoryService implements InvitationService{
	private static final Logger LOG = LoggerFactory.getLogger(InvitationInMemoryService.class);
	private UserService userService;
	private GameService gameService;
	private List<Invitation> invitations = new ArrayList<>();
	private long maxId = 1;
	@Autowired
	public InvitationInMemoryService(UserService userService, GameService gameService) {
		this.userService = userService;
		this.gameService = gameService;
	}

	@Override
	public void sendInvitation(String senderName, String recepientName) {
		LOG.info("sending invitation from {} to {} game {}", senderName, recepientName);
		User sender = userService.get(senderName);
		User recepient = userService.get(recepientName);
		Invitation invitation = new Invitation();
		invitation.setSender(sender);
		invitation.setRecepient(recepient);
		invitation.setId(maxId++);
		sender.getInvitations().add(invitation);
		recepient.getInvitations().add(invitation);
		invitations.add(invitation);
		userService.update(sender);
		userService.update(recepient);
		userService.sendRefreshBySocket(sender);
		userService.sendRefreshBySocket(recepient);
	}

	@Override
	public void removeInvitation(String username, long id) {
		LOG.info("removing invitation by user {} with ID {}", username, id);
		Optional<Invitation> invOpt = invitations.stream().filter(i -> i.getId() == id).findFirst();
		if(invOpt.isPresent()) {
			Invitation invitation = invOpt.get();
			User sender = invitation.getSender();
			User recepient = invitation.getRecepient();
			if(!sender.getUsername().equals(username) && !recepient.getUsername().equals(username)) {
				LOG.error("invitation with ID={} not belongs to user with name={}", id, username);
				throw new NotUserInvitationException("invitation not belong to user" + username);
			}
			sender.getInvitations().removeIf(i -> i.getId() == id);
			recepient.getInvitations().removeIf(i -> i.getId() == id);
			userService.update(sender);
			userService.update(recepient);
			invitations.removeIf(i -> i.getId() == id);
			userService.sendRefreshBySocket(sender);
			userService.sendRefreshBySocket(recepient);
			LOG.info("invitation removed by user {} with ID {}", username, id);
		} else {
			LOG.error("trying to remove unexistant invitation for id={}, no action", id);
			return;
		}
		
	}

	@Override
	public Game acceptInvitation(String recepient, long id) {
		LOG.info("accepting invitation ID={} by {}", id, recepient);
		Game game = null;
		Optional<Invitation> invOpt = invitations.stream().filter(i -> i.getId() == id).findFirst();
		if(invOpt.isPresent()) {
			Invitation invitation = invOpt.get();
			User sender = invitation.getSender();
			User rec = invitation.getRecepient();
			if(!rec.getUsername().equals(recepient)) {
				LOG.error("invitation with ID={} not belongs to user with name={}", id, recepient);
				throw new NotUserInvitationException("invitation not belong to user" + recepient);
			}
			//operate existing games of users
			if(sender.getGame() != null && sender.getGame().getIsFinished() == false) {
				LOG.error("error accepting invitation ID={}: sender is in other game {}", sender);
				throw new ChessException("can't create accept invitation - sender is in other game now");
			}
			
			if(rec.getGame() != null) {
				if(!rec.getGame().getIsFinished()) {
					LOG.error("error accepting invitation ID={}: recepient is in other game {}", recepient);
					throw new ChessException("can't create accept invitation - recepient is in other game now");
				} else {
					rec.setGame(null);
				}
			}
			game = gameService.add(sender.getUsername(), rec.getUsername(), ColorPolicy.WHITE_CREATOR);
			sender.setGame(game);
			rec.setGame(game);
			userService.update(sender);
			userService.update(rec);
			removeInvitation(recepient, id);
			userService.sendRefreshBySocket(sender);
			userService.sendRefreshBySocket(rec);
		} else {
			throw new NotUserInvitationException("trying to accept unexistant envitation");
		}
		return game;
	}

	@Override
	public Invitation getById(long id) {
		return invitations.stream().filter(i -> i.getId() == id).findFirst().orElseThrow();
	}

	@Override
	public List<Invitation> getForUsername(String username) {
		return invitations.stream()
				.filter(inv -> inv.getRecepient().getUsername().equals(username) || inv.getSender().getUsername().equals(username))
				.collect(Collectors.toList());
	}
	
}
