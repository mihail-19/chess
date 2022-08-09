package com.teslenko.chessbackend.entity.figures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;

import com.teslenko.chessbackend.entity.Color;
import com.teslenko.chessbackend.entity.Desk;
import com.teslenko.chessbackend.entity.Field;

@Entity
public class Bishop extends Figure implements Cloneable{
	public Bishop(Field field, Color color, FigureType type) {
		super(field, color, type);
	}
	public Bishop() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Bishop((Field) getField().clone(), getColor(), getType());
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
