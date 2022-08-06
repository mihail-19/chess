package com.teslenko.chessbackend.entity;

/**
 * Game finish proposition
 * @author Mykhailo Teslenko
 *
 */
public class GameFinishProposition {
	private String senderUsername;
	private boolean isDraw;
	
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
	@Override
	public String toString() {
		return "GameFinishProposition [senderUsername=" + senderUsername + ", isDraw=" + isDraw + "]";
	}
	
}
