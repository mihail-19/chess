package com.teslenko.chessbackend.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;

import com.teslenko.chessbackend.entity.figures.Figure;

@Entity
public class MoveRecord implements Cloneable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Embedded
	private Move move;
	private Color color;
	@OneToOne
	@JoinTable(name = "move_record_figure",
			joinColumns = @JoinColumn(name="move_record_id"),
			inverseJoinColumns = @JoinColumn(name="figure_id"))
	private Figure takenFigure;
	
	
	public MoveRecord(){
		
	}
	public MoveRecord(Move move, Color color, Figure takenFigure) {
		this.move = move;
		this.color = color;
		this.takenFigure = takenFigure;
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Figure figure = takenFigure == null ? null : (Figure) takenFigure.clone();
		return new MoveRecord((Move) move.clone(), color, figure);
	}
	public Move getMove() {
		return move;
	}
	public Color getColor() {
		return color;
	}
	public Figure getTakenFigure() {
		return takenFigure;
	}
	public void setMove(Move move) {
		this.move = move;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public void setTakenFigure(Figure takenFigure) {
		this.takenFigure = takenFigure;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "MoveRecord [move=" + move + ", color=" + color + ", takenFigure=" + takenFigure + "]";
	}
	
}
