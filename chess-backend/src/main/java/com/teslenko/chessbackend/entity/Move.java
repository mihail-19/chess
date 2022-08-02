package com.teslenko.chessbackend.entity;

public class Move implements Cloneable {
	private Field from;
	private Field to;
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Move((Field) from.clone(), (Field) to.clone());
	}
	public Move(Field from, Field to) {
		this.from = from;
		this.to = to;
	}
	public Move() {
		
	}
	public Field getFrom() {
		return from;
	}
	public void setFrom(Field from) {
		this.from = from;
	}
	public Field getTo() {
		return to;
	}
	public void setTo(Field to) {
		this.to = to;
	}
	@Override
	public String toString() {
		return "Move [from=" + from + ", to=" + to + "]";
	}
	
}
