package com.teslenko.chessbackend.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teslenko.chessbackend.exception.ImpossibleMoveException;
import com.teslenko.chessbackend.exception.UnautorizedPlayerException;

@Entity
public class Game {
	private static final Logger LOG = LoggerFactory.getLogger(Game.class);
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Embedded
	private Desk desk;

	@JsonIgnoreProperties("game")
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumns(
			@JoinColumn(name="creator_id", referencedColumnName = "id")
	)
	private User creator;
	@JsonIgnoreProperties("game")
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumns(
			@JoinColumn(name="opponent_id", referencedColumnName = "id")
	)
	private User opponent; 
	private Color creatorColor;
	private Color moveColor = Color.white;
	private ColorPolicy colorPolicy;
	private String moverUsername;
	private boolean isStarted;
	private boolean isFinished;
	private Winner winner;
	
	@OneToOne
	private GameFinishProposition gameFinishProposition;
	public Game(User creator, ColorPolicy colorPolicy) {
		this.creator = creator;
		this.colorPolicy = colorPolicy;
	}
	public Game(User creator, ColorPolicy colorPolicy, Desk desk) {
		this.creator = creator;
		this.colorPolicy = colorPolicy;
		this.desk = desk;
	}
	public Game() {
		
	}
	public Color getUserColor(User user) {
		if(creator.getUsername().equals(user.getUsername())) {
			return creatorColor;
		} else if (opponent.getUsername().equals(user.getUsername())) {
			return creatorColor == Color.white ? Color.black : Color.white;
		} else {
			LOG.error("error getting user color: user {} is not player in game {}", user, this);
			throw new UnautorizedPlayerException("error getting user color: user is not player in game");
		}
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
	public void startGame() {
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
	
	/**
	 * Finishes the game with given winner
	 * @param winner
	 */
	public void finishGame(Winner winner) {
		isFinished = true;
		this.winner = winner;
	}
	
	/**
	 * Perform figure move by player. If move color is incorrect, throws ImpossibleMoveException.
	 * Move color changes after move to opposite.
	 * @param username
	 * @param from
	 * @param to
	 */
	public void move(User user, Move move) {
		if(!isStarted || isFinished) {
			throw new ImpossibleMoveException("error while move: can't move if game is not running");
		}
		Field from = move.getFrom();
		Field to = move.getTo();
		if(!creator.equals(user) && !opponent.equals(user)) {
			throw new UnautorizedPlayerException("User " + user + " is not attached to the game " + this);
		}
		if(moverUsername.equals(user.getUsername())) {
			desk.moveFigure(moveColor, from, to);
			tryFinishGame();
			switchMover();
		} else {
				throw new ImpossibleMoveException("trying to move black figure by white player");
		}
	}
	
	//Trying to finish game if checkmate or pat. Set winner from checkmate state of the desk.
	private void tryFinishGame() {
		Checkmate checkmate = desk.getCheckmate();
		if(checkmate != Checkmate.NONE) {
			Winner winner = Winner.draw;
			if(checkmate == Checkmate.WHITE_CHECKMATE ) {
				winner = Winner.black;
			} else if(checkmate == Checkmate.BLACK_CHECKMATE) {
				winner = Winner.white;
			}
			finishGame(winner);
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
	
	
	public Winner getWinner() {
		return winner;
	}
	public void setWinner(Winner winner) {
		this.winner = winner;
	}
	
	public GameFinishProposition getGameFinishProposition() {
		return gameFinishProposition;
	}
	public void setGameFinishProposition(GameFinishProposition gameFinishProposition) {
		this.gameFinishProposition = gameFinishProposition;
	}
	@Override
	public String toString() {
		String creatorUsername = creator == null ? " - " : creator.getUsername();
		String opponentUsername = opponent == null ? " - " : opponent.getUsername();
		return "Game [id=" + id + ", desk=" + desk + ", creator=" + creatorUsername + ", opponent=" + opponentUsername
				+ ", creatorColor=" + creatorColor + ", moveColor=" + moveColor + ", isStarted=" + isStarted
				+ ", isFinished=" + isFinished + ", colorPolicy=" + colorPolicy +  ", mover's name=" + moverUsername + "]";
	}
	
	
}
