package com.teslenko.chessbackend.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Move implements Cloneable {
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "rowId", column = @javax.persistence.Column(name = "from_row_id")),
		@AttributeOverride(name="columnId", column = @javax.persistence.Column(name = "from_column_id"))
	})
	private Field from;
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "rowId", column = @javax.persistence.Column(name = "to_row_id")),
		@AttributeOverride(name="columnId", column = @javax.persistence.Column(name = "to_column_id"))
	})
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
