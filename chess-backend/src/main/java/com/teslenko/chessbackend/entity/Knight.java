package com.teslenko.chessbackend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Knight extends Figure{
	public Knight(Field field, Color color, FigureType type) {
		super(field, color, type);
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
