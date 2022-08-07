package com.teslenko.chessbackend.entity.figures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.teslenko.chessbackend.entity.Color;
import com.teslenko.chessbackend.entity.Desk;
import com.teslenko.chessbackend.entity.Field;


public class Knight extends Figure implements Cloneable{
	public Knight(Field field, Color color, FigureType type) {
		super(field, color, type);
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Knight((Field) getField().clone(), getColor(), getType());
	}
	@Override
	public List<Field> availableMoves(Desk desk){
		Map<Field, Figure> fields = desk.getFields();
		List<Field> moves = new ArrayList<>();
		addMoveIfValid(2, -1, fields, moves);
		addMoveIfValid(2, 1, fields, moves);
		
		addMoveIfValid(-2, -1, fields, moves);
		addMoveIfValid(-2, 1, fields, moves);
		
		addMoveIfValid(1, -2, fields, moves);
		addMoveIfValid(1, 2, fields, moves);
		
		addMoveIfValid(-1, -2, fields, moves);
		addMoveIfValid(-1, 2, fields, moves);
		return moves;
	}
	
}
