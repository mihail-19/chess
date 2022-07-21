package com.teslenko.chessbackend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.teslenko.chessbackend.exception.ImpossibleMoveException;

public class PawnFigure extends Figure {
	public PawnFigure(Field field, Color color, FigureType type) {
		super(field, color, type);
	}
	
	@Override
	public void move(Desk desk, Field field) {
		super.move(desk, field);
		if((getColor() == Color.white && getField().getRowId() == 8) || (getColor() == Color.black && getField().getRowId() == 1)) {
			setType(FigureType.queen);
		}
	}
	
	@Override
	public List<Field> availableMoves(Map<Field, Figure> fields) {
		List<Field> moves = new ArrayList<>();
		if(getColor() == Color.white) {
			boolean isFreeNextField = addStraightMove(1, 0, fields, moves);
			if(isFreeNextField) {
				addStraightMove(2, 0, fields, moves);
			}
			addMoveIfValid(1, -1, fields, moves);
			addMoveIfValid(1, 1, fields, moves);
		} else {
			boolean isFreeNextField = addStraightMove(-1, 0, fields, moves);
			if(isFreeNextField) {
				addStraightMove(-2, 0, fields, moves);
			}
			addMoveIfValid(-1, -1, fields, moves);
			addMoveIfValid(-1, 1, fields, moves);
		}
		
		return moves;
	}
	
	private boolean addStraightMove(int rowShift, int columnShift, Map<Field, Figure> fields, List<Field> moves) {
		Field field = getField();
		if(field.isValidShift(rowShift, columnShift)) {
			Field shifted = field.getShiftedField(rowShift, columnShift);
			if(!fields.containsKey(shifted)) {
				moves.add(shifted);
				return true;
			}
		}
		return false;
	}
}