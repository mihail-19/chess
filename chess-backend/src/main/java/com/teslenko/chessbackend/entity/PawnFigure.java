package com.teslenko.chessbackend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.IntConsumer;


public class PawnFigure extends Figure implements Cloneable {
	public PawnFigure(Field field, Color color, FigureType type) {
		super(field, color, type);
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new PawnFigure((Field) getField().clone(), getColor(), getType());
	}
	@Override
	public Figure move(Desk desk, Field field) {
		Figure figureToTake = super.move(desk, field);
		if((getColor() == Color.white && getField().getRowId() == 8) || (getColor() == Color.black && getField().getRowId() == 1)) {
			setType(FigureType.queen);
		}
		return figureToTake;
	}
	
	@Override
	public List<Field> availableMoves(Desk desk) {
		Map<Field, Figure> fields = desk.getFields();
		List<Field> moves = new ArrayList<>();
		if(getColor() == Color.white) {
			boolean isFreeNextField = addStraightMove(1, 0, fields, moves);
			if(isFreeNextField) {
				addStraightMove(2, 0, fields, moves);
			}
			addMoveDiagonal(1, -1, fields, moves);
			addMoveDiagonal(1, 1, fields, moves);
		} else {
			boolean isFreeNextField = addStraightMove(-1, 0, fields, moves);
			if(isFreeNextField) {
				addStraightMove(-2, 0, fields, moves);
			}
			addMoveDiagonal(-1, -1, fields, moves);
			addMoveDiagonal(-1, 1, fields, moves);
		}
		return moves;
	}
	
	private void addMoveDiagonal(int rowShift, int columnShift, Map<Field, Figure> fields, List<Field> moves) {
		Field field = getField();
		if (field.isValidShift(rowShift, columnShift)) {
			Field shifted = field.getShiftedField(rowShift, columnShift);
			if (fields.containsKey(shifted) && fields.get(shifted).getColor() != this.getColor()) {
				moves.add(shifted);
			}
		}
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
