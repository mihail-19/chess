package com.teslenko.chessbackend.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Game finish proposition
 * @author Mykhailo Teslenko
 *
 */
@Entity
public class GameFinishProposition {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String senderUsername;
	private boolean isDraw;
	
	@OneToOne(mappedBy = "gameFinishProposition")
	private Game game;
	public GameFinishProposition() {
	}
	public GameFinishProposition(String username, boolean isDraw) {
		super();
		this.senderUsername = username;
		this.isDraw = isDraw;
	}
	public boolean getIsDraw() {
		return isDraw;
	}
	public void setIsDraw(boolean isDraw) {
		this.isDraw = isDraw;
	}
	public String getSenderUsername() {
		return senderUsername;
	}
	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}
	
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "GameFinishProposition [senderUsername=" + senderUsername + ", isDraw=" + isDraw + "]";
	}
	
}
