package com.teslenko.chessbackend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Queen extends Figure{
	public Queen(Field field, Color color, FigureType type) {
		super(field, color, type);
	}
	
	
	public List<Field> availableMoves(Map<Field, Figure> fields){
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
		
		shiftRow = 0;
		isAvailable = addMoveIfValid(++shiftRow, 0, fields, moves);
		while(isAvailable) {
			isAvailable = addMoveIfValid(++shiftRow, 0, fields, moves);
		}
		shiftRow = 0;
		isAvailable = addMoveIfValid(--shiftRow, 0, fields, moves);
		while(isAvailable) {
			isAvailable = addMoveIfValid(--shiftRow, 0, fields, moves);
		}
		
		shiftColumn = 0;
		isAvailable = addMoveIfValid(0, ++shiftColumn, fields, moves);
		while(isAvailable) {
			isAvailable = addMoveIfValid(0, ++shiftColumn, fields, moves);
		}
		shiftColumn = 0;
		isAvailable = addMoveIfValid(0, --shiftColumn, fields, moves);
		while(isAvailable) {
			isAvailable = addMoveIfValid(0, --shiftColumn, fields, moves);
		}
		return moves;
	}
	
}
