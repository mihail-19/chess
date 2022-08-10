package com.teslenko.chessbackend.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teslenko.chessbackend.db.InvitationCrudRepository;
import com.teslenko.chessbackend.entity.ColorPolicy;
import com.teslenko.chessbackend.entity.Game;
import com.teslenko.chessbackend.entity.Invitation;
import com.teslenko.chessbackend.entity.User;
import com.teslenko.chessbackend.exception.ChessException;
import com.teslenko.chessbackend.exception.NotUserInvitationException;

@Service
public class InvitationInDBService implements InvitationService{
	private static final Logger LOG = LoggerFactory.getLogger(InvitationInDBService.class);
	private UserService userService;
	private GameService gameService;
	private InvitationCrudRepository invitationCrudRepository;
	private MessagingService messagingService;
	private static final int MAX_INVITATIONS = 10;
	@Autowired
	public InvitationInDBService(UserService userService, GameService gameService, InvitationCrudRepository invitationCrudRepository, MessagingService messagingService) {
		this.userService = userService;
		this.gameService = gameService;
		this.invitationCrudRepository = invitationCrudRepository;
		this.messagingService = messagingService;
	}

	@Override
	@Transactional
	public void sendInvitation(String senderName, String recepientName) {
		LOG.info("sending invitation from {} to {} game {}", senderName, recepientName);
		User sender = userService.get(senderName);
		User recepient = userService.get(recepientName);
		if(sender.getInvitations().size() > MAX_INVITATIONS) {
			LOG.error("failed to send invitation: sender {} limit exhausted", sender);
			throw new ChessException("Sender invitation limit exhausted");
		}
		if(recepient.getInvitations().size() > MAX_INVITATIONS) {
			LOG.error("failed to send invitation: recepient {} limit exhausted", recepient);
			throw new ChessException("Recepient invitation limit exhausted");
		}
		Invitation invitation = new Invitation(senderName, recepientName);
		Invitation savedInv = invitationCrudRepository.save(invitation);
		sender.getInvitations().add(savedInv);
		recepient.getInvitations().add(savedInv);
		userService.update(sender);
		userService.update(recepient);
		messagingService.sendRefreshBySocket(sender);
		messagingService.sendRefreshBySocket(recepient);
	}
	
	
	@Override
	public void removeInvitation(String username, long id) {
		LOG.info("removing invitation by user {} with ID {}", username, id);
		Invitation inv = invitationCrudRepository.findById(id).orElseThrow();
		User sender = userService.get(inv.getSenderUsername());
		User recepient = userService.get(inv.getRecepientUsername());
		if(!sender.getUsername().equals(username) && recepient.getUsername().equals(username)){
			LOG.error("invitation with ID={} not belongs to user with name={}", id, username);
			throw new NotUserInvitationException("invitation not belong to user" + username);
		}
		removeInvitationFromUsers(sender, recepient, id);
		userService.update(sender);
		userService.update(recepient);
		LOG.info("invitation removed by user {} with ID {}", username, id);
		messagingService.sendRefreshBySocket(sender);
		messagingService.sendRefreshBySocket(recepient);
		
		
	}
	
	@Transactional
	@Override
	public Game acceptInvitation(String recepientUsername, long id) {
		LOG.info("accepting invitation ID={} by {}", id, recepientUsername);
		Game game = null;
		Invitation inv = invitationCrudRepository.findById(id).orElseThrow();
		User sender = userService.get(inv.getSenderUsername());
		User recepient = userService.get(inv.getRecepientUsername());
		if(!recepient.getUsername().equals(recepientUsername)){
			LOG.error("invitation {} not belongs to user with name={}", inv, recepientUsername);
			throw new NotUserInvitationException("invitation not belong to user" + recepientUsername);
		}
		Game senderGame = sender.getGame();
		if(senderGame != null) {
			if(senderGame.getIsFinished()) {
				gameService.removeUserFromGame(sender);
				sender.setGame(null);
				//gameService.remove(senderGame.getId());
			} else {
				LOG.error("error accepting invitation ID={}: sender is in other game {}", sender);
				throw new ChessException("can't create accept invitation - sender is in other game now");
			}
		}
		Game recGame = recepient.getGame();
		if(recGame != null) {
			if(!recGame.getIsFinished()) {
				LOG.error("error accepting invitation ID={}: recepient is in other game {}", recepient);
				throw new ChessException("can't create accept invitation - recepient is in other game now");
			} else {
				gameService.removeUserFromGame(recepient);
				recepient.setGame(null);
				//gameService.remove(recGame.getId());
			}
			
		}
		game = gameService.add(sender, recepient, ColorPolicy.WHITE_CREATOR);
		sender.setGame(game);
		recepient.setGame(game);
		removeInvitationFromUsers(sender, recepient, id);
		userService.update(sender);
		userService.update(recepient);
		messagingService.sendRefreshBySocket(sender);
		messagingService.sendRefreshBySocket(recepient);
		return game;
	}

	@Override
	public Invitation getById(long id) {
		return invitationCrudRepository.findById(id).orElseThrow();
	}
	
	/**
	 * Founds all invitations for given user, sender and recepient role both.
	 * 
	 * @implNote Method could work slow in this version, it fetches all {@link Invitation} from DB
	 */
	@Override
	public List<Invitation> getForUsername(String username) {
		Iterable<Invitation> invs = invitationCrudRepository.findAll();
		List<Invitation> userInvitations = new ArrayList<>();
		for(Invitation inv : invs) {
			if(inv.getSenderUsername().equals(username) || inv.getRecepientUsername().equals(username)) {
				userInvitations.add(inv);
			}
		}
		return userInvitations;
	}
	
	
	/*
	 * Removes invitation, deleting it also from given users.
	 */
	private void removeInvitationFromUsers(User sender, User recepient, long id) {
		sender.getInvitations().removeIf(in -> in.getId() == id);
		recepient.getInvitations().removeIf(in -> in.getId() == id);
		invitationCrudRepository.deleteById(id);
	}
}
