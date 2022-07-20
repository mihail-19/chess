package com.teslenko.chessbackend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Rook extends Figure{
	public Rook(Field field, Color color, FigureType type) {
		super(field, color, type);
	}
	
	
	public List<Field> availableMoves(Map<Field, Figure> fields){
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
