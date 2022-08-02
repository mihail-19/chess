package com.teslenko.chessbackend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Rook extends Figure implements Cloneable{
	public Rook(Field field, Color color, FigureType type) {
		super(field, color, type);
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Rook((Field) getField().clone(), getColor(), getType());
	}
	@Override
	public Figure move(Desk desk, Field field) {
		Field old = this.getField();
		Figure figureToTake = super.move(desk, field);
		if(desk.getIsCastlingAvailableWhiteLeft() && getColor() == Color.white && old.getColumnId() == Column.a) {
			desk.setIsCastlingAvailableWhiteLeft(false);
		}
		if(desk.getIsCastlingAvailableWhiteRight() && getColor() == Color.white && old.getColumnId() == Column.h) {
			desk.setIsCastlingAvailableWhiteRight(false);
		}
		if(desk.getIsCastlingAvailableWhiteLeft() && getColor() == Color.black && old.getColumnId() == Column.a) {
			desk.setIsCastlingAvailableBlackLeft(false);
		}
		if(desk.getIsCastlingAvailableBlackRight() && getColor() == Color.black && old.getColumnId() == Column.h) {
			desk.setIsCastlingAvailableBlackRight(false);
		}
		return figureToTake;
	}
	
	@Override
	public List<Field> availableMoves(Desk desk){
		Map<Field, Figure> fields = desk.getFields();
		List<Field> moves = new ArrayList<>();
		
		int rowShift = 0;
		boolean isAvailable = addMoveIfValid(++rowShift, 0, fields, moves);
		while(isAvailable) {
			isAvailable = addMoveIfValid(++rowShift, 0, fields, moves);
		}
		rowShift = 0;
		isAvailable = addMoveIfValid(--rowShift, 0, fields, moves);
		while(isAvailable) {
			isAvailable = addMoveIfValid(--rowShift, 0, fields, moves);
		}
		
		int columnShift = 0;
		isAvailable = addMoveIfValid(0, ++columnShift, fields, moves);
		while(isAvailable) {
			isAvailable = addMoveIfValid(0, ++columnShift, fields, moves);
		}
		columnShift = 0;
		isAvailable = addMoveIfValid(0, --columnShift, fields, moves);
		while(isAvailable) {
			isAvailable = addMoveIfValid(0, --columnShift, fields, moves);
		}
		return moves;
	}
	
}
