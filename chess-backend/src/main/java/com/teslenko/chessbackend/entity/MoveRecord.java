package com.teslenko.chessbackend.entity;

public class MoveRecord implements Cloneable {
	private Move move;
	private Color color;
	private Figure takenFigure;
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
	@Override
	public String toString() {
		return "MoveRecord [move=" + move + ", color=" + color + ", takenFigure=" + takenFigure + "]";
	}
	
}
