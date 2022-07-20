package com.teslenko.chessbackend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.teslenko.chessbackend.exception.ImpossibleMoveException;

public class Knight extends Figure{
	public Knight(Field field, Color color, FigureType type) {
		super(field, color, type);
	}
	
	
	public List<Field> availableMoves(Map<Field, Figure> fields){
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
