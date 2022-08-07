package com.teslenko.chessbackend.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Invitation for users to start game. 
 * @author Mykhailo Teslenko
 *
 */
@Entity
public class Invitation {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String senderUsername;
	
	private String recepientUsername;
	
	@ManyToMany(mappedBy = "invitations")
	@JsonIgnore
	private List<User> users;
	
	public Invitation(String senderUsername, String recepientUsername) {
		this.senderUsername = senderUsername;
		this.recepientUsername = recepientUsername;
	}
	public Invitation() {
	}
	public long getId() {
		return id;
	}

	public String getSenderUsername() {
		return senderUsername;
	}

	public String getRecepientUsername() {
		return recepientUsername;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}

	public void setRecepientUsername(String recepientUsername) {
		this.recepientUsername = recepientUsername;
	}

	@Override
	public String toString() {
		return "Invitation [id=" + id + ", senderUsername=" + senderUsername + ", recepientUsername="
				+ recepientUsername + "]";
	}
	
	
}
