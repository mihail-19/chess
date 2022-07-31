package com.teslenko.chessbackend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Bishop extends Figure{
	public Bishop(Field field, Color color, FigureType type) {
		super(field, color, type);
	}
	
	@Override
	public List<Field> availableMoves(Desk desk){
		Map<Field, Figure> fields = desk.getFields();
		List<Field> moves = new ArrayList<>();
		int shiftRow = 0;
		int shiftColumn = 0;
		boolean isAvailable = addMoveIfValid(--shiftRow, --shiftColumn, fields, moves);
		while(isAvailable) {
			isAvailable = addMoveIfValid(--shiftRow, --shiftColumn, fields, moves);
		}
		
		shiftRow = 0;
		shiftColumn = 0;
		isAvailable = addMoveIfValid(--shiftRow, ++shiftColumn, fields, moves);
		while(isAvailable) {
			isAvailable = addMoveIfValid(--shiftRow, ++shiftColumn, fields, moves);
		}
		
		shiftRow = 0;
		shiftColumn = 0;
		isAvailable = addMoveIfValid(++shiftRow, --shiftColumn, fields, moves);
		while(isAvailable) {
			isAvailable = addMoveIfValid(++shiftRow, --shiftColumn, fields, moves);
		}
		
		shiftRow = 0;
		shiftColumn = 0;
		isAvailable = addMoveIfValid(++shiftRow, ++shiftColumn, fields, moves);
		while(isAvailable) {
			isAvailable = addMoveIfValid(++shiftRow, ++shiftColumn, fields, moves);
		}
		
		return moves;
	}
	
}
