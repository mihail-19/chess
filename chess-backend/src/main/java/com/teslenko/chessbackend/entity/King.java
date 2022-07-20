package com.teslenko.chessbackend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class King extends Figure{
	public King(Field field, Color color, FigureType type) {
		super(field, color, type);
	}
	
	
	public List<Field> availableMoves(Map<Field, Figure> fields){
		List<Field> moves = new ArrayList<>();
		addMoveIfValid(1, 0, fields, moves);
		addMoveIfValid(1, 1, fields, moves);
		addMoveIfValid(1, -1, fields, moves);
		addMoveIfValid(0, 1, fields, moves);
		addMoveIfValid(0, -1, fields, moves);
		addMoveIfValid(-1, 0, fields, moves);
		addMoveIfValid(-1, -1, fields, moves);
		addMoveIfValid(-1, 1, fields, moves);
		
		//castling
		return moves;
	}
	
}
