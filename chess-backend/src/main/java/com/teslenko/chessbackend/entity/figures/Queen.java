package com.teslenko.chessbackend.entity.figures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.teslenko.chessbackend.entity.Color;
import com.teslenko.chessbackend.entity.Desk;
import com.teslenko.chessbackend.entity.Field;


public class Queen extends Figure implements Cloneable{
	public Queen(Field field, Color color, FigureType type) {
		super(field, color, type);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Queen((Field) getField().clone(), getColor(), getType());
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
