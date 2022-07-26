package com.teslenko.chessbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.teslenko.chessbackend.exception.ImpossibleMoveException;
import com.teslenko.chessbackend.exception.UnautorizedPlayerException;

public class Game {
	private long id;
	private Desk desk;

	@JsonIgnoreProperties("game")
	private User creator;
	@JsonIgnoreProperties("game")
	private User opponent; 
	private Color creatorColor;
	private Color moveColor = Color.white;
	private boolean isStarted;
	private boolean isFinished;
	private ColorPolicy colorPolicy;
	private String moverUsername;
	
	public Game(User creator, ColorPolicy colorPolicy) {
		this.creator = creator;
		this.colorPolicy = colorPolicy;
	}
	public Game() {
		
	}
	
	public void addPlayer(User player) {
		this.opponent = player;
	}
	
	public void startGame(Desk desk) {
		this.desk = desk;
		if(colorPolicy == ColorPolicy.WHITE_CREATOR) {
			creatorColor = Color.white;
			moverUsername = creator.getUsername();
		} else if (colorPolicy == ColorPolicy.BLACK_CREATOR){
			creatorColor = Color.black;
			moverUsername = opponent.getUsername();
		} else if(colorPolicy == ColorPolicy.RANDOM) {
			boolean isWhite = Math.random() > 0.5;
			if(isWhite) {
				creatorColor = Color.white;
				moverUsername = creator.getUsername();
			} else {
				creatorColor = Color.black;
				moverUsername = opponent.getUsername();
			}
		}
		isStarted = true;
	}
	
	public void finishGame() {
		isFinished = true;
	}
	
	/**
	 * Perform figure move by player. If move color is incorrect, throws ImpossibleMoveException.
	 * Move color changes after move to opposite.
	 * @param username
	 * @param from
	 * @param to
	 */
	public void move(User user, Move move) {
		Field from = move.getFrom();
		Field to = move.getTo();
		if(!creator.equals(user) && !opponent.equals(user)) {
			throw new UnautorizedPlayerException("User " + user + " is not attached to the game " + this);
		}
		if(moverUsername.equals(user.getUsername())) {
			desk.moveFigure(moveColor, from, to);
			switchMover();
		} else {
				throw new ImpossibleMoveException("trying to move black figure by white player");
		}
	}
	
	/*
	 * Switches current mover to other and switches move color to opposite
	 */
	private void switchMover() {
		if(moverUsername.equals(creator.getUsername())) {
			moverUsername = opponent.getUsername();
		} else {
			moverUsername = creator.getUsername();
		}
		if(moveColor == Color.white) {
			moveColor = Color.black;
		} else {
			moveColor = Color.white;
		}
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Desk getDesk() {
		return desk;
	}

	public void setDesk(Desk desk) {
		this.desk = desk;
	}


	public Color getMoveColor() {
		return moveColor;
	}

	public void setMoveColor(Color moveColor) {
		this.moveColor = moveColor;
	}
	
	
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	public User getOpponent() {
		return opponent;
	}
	public void setOpponent(User opponent) {
		this.opponent = opponent;
	}
	public Color getCreatorColor() {
		return creatorColor;
	}
	public void setCreatorColor(Color creatorColor) {
		this.creatorColor = creatorColor;
	}
	public boolean getIsStarted() {
		return isStarted;
	}
	public void setIsStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}
	public boolean getIsFinished() {
		return isFinished;
	}
	public void setIsFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	public ColorPolicy getColorPolicy() {
		return colorPolicy;
	}
	public void setColorPolicy(ColorPolicy colorPolicy) {
		this.colorPolicy = colorPolicy;
	}
	
	public String getMoverUsername() {
		return moverUsername;
	}
	public void setMoverUsername(String moverUsername) {
		this.moverUsername = moverUsername;
	}
	@Override
	public String toString() {
		return "Game [id=" + id + ", desk=" + desk + ", creator=" + creator.getUsername() + ", opponent=" + opponent.getUsername()
				+ ", creatorColor=" + creatorColor + ", moveColor=" + moveColor + ", isStarted=" + isStarted
				+ ", isFinished=" + isFinished + ", colorPolicy=" + colorPolicy +  ", mover's name=" + moverUsername + "]";
	}
	
	
}
