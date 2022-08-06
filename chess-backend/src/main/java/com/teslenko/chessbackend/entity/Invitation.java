package com.teslenko.chessbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class Invitation {
	private long id;
	@JsonIgnoreProperties(value = {"game", "invitations"})
	private User sender;
	@JsonIgnoreProperties(value = {"game", "invitations"})
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
	@Override
	public String toString() {
		return "Invitation [id=" + id + ", sender=" + sender.getUsername() + ", recepient=" + recepient.getUsername() + "]";
	}
	
}
