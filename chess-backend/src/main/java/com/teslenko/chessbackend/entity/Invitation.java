package com.teslenko.chessbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Invitation {
	private long id;
	@JsonIgnore
	private User sender;
	@JsonIgnore
	private User recepient;
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}
	public User getRecepient() {
		return recepient;
	}
	public void setRecepient(User recepient) {
		this.recepient = recepient;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
}
