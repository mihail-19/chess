package com.teslenko.chessbackend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class King extends Figure implements Cloneable{
	public King(Field field, Color color, FigureType type) {
		super(field, color, type);
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new King((Field) getField().clone(), getColor(), getType());
	}
	@Override
	public Figure move(Desk desk, Field field) {
		int shift = field.getColumnId().ordinal() - getField().getColumnId().ordinal();
		Figure figureToTake = super.move(desk, field);
	
		//rook move is castling - in case shift between king moves is bigger than 1
		//Unreachable code if move is not possible
		Field rookField;
		Field newField;
		if(Math.abs(shift) > 1) {
			if(shift < 0) {
				rookField = new Field(this.getField().getRowId(), Column.a);
				newField = new Field(this.getField().getRowId(), Column.d);
			} else {
				rookField = new Field(this.getField().getRowId(), Column.h);
				newField = new Field(this.getField().getRowId(), Column.f);
			}
			Figure rook = desk.getFields().get(rookField);
			desk.getFields().remove(rookField);
			rook.setField(newField);
			desk.getFields().put(newField, rook);
		}
		
		//If any move for the king, castling is restricted for mover's color
		if(getColor() == Color.white) {
			desk.setIsCastlingAvailableWhiteLeft(false);
			desk.setIsCastlingAvailableWhiteRight(false);
		}
		if(getColor() == Color.black) {
			desk.setIsCastlingAvailableBlackLeft(false);
			desk.setIsCastlingAvailableBlackRight(false);
		}
		return figureToTake;
	}
	
	public List<Field> availableMoves(Desk desk){
		Map<Field, Figure> fields = desk.getFields();
		List<Field> moves = new ArrayList<>();
		addMoveIfValid(1, 0, fields, moves);
		addMoveIfValid(1, 1, fields, moves);
		addMoveIfValid(1, -1, fields, moves);
		addMoveIfValid(0, 1, fields, moves);
		addMoveIfValid(0, -1, fields, moves);
		addMoveIfValid(-1, 0, fields, moves);
		addMoveIfValid(-1, -1, fields, moves);
		addMoveIfValid(-1, 1, fields, moves);
		
		if(isAvailableCastlingLeft(desk)) {
			moves.add(getField().getShiftedField(0, -2));
		} 
		if (isAvailableCastlingRight(desk)) {
			moves.add(getField().getShiftedField(0, 2));
		}
		return moves;
	}
	
	public boolean isAvailableCastlingLeft(Desk desk) {
		//If castling is not more available (already performed or figures were moved) for the given side.
		//Not available under the check
		if(getColor() == Color.white && (desk.getIsUnderCheckWhite() || !desk.getIsCastlingAvailableWhiteLeft()) 
				|| getColor() == Color.black && (desk.getIsUnderCheckBlack() || !desk.getIsCastlingAvailableBlackLeft())) {
			return false;
		}
		//check if there is no figures on path
		Map<Field, Figure> fields = desk.getFields();
		if(!hasFiguresOnRow(Column.a, fields)) {
			return hasRookOnField(Column.a, fields);
		} 
		return false;
	}
	public boolean isAvailableCastlingRight(Desk desk) {
		//If castling is not more available (already performed or figures were moved) for the given side.
		//Not available under the check
		if(getColor() == Color.white && (desk.getIsUnderCheckWhite() || !desk.getIsCastlingAvailableWhiteRight()) 
				|| getColor() == Color.black && (desk.getIsUnderCheckBlack() || !desk.getIsCastlingAvailableBlackRight())) {
			return false;
		}
		//check if there is no figures on path
		Map<Field, Figure> fields = desk.getFields();
		if(!hasFiguresOnRow(Column.h, fields)) {
			return hasRookOnField(Column.h, fields);
		} 
		return false;
	}
	
	private boolean hasRookOnField(Column column, Map<Field, Figure> fields) {
		Field rookField = new Field(getField().getRowId(), column);
		if(fields.containsKey(rookField)) {
			Figure rook = fields.get(rookField);
			return rook.getType().equals(FigureType.rook) && rook.getColor().equals(this.getColor());
		} else {
			return false;
		}
	}
	
	/**
	 * Returns true if there is a figure on the row from this figure position to given position (exclusively).
	 * Meant to use on castling checks.
	 * @param to
	 * @param fields
	 * @return
	 */
	public boolean hasFiguresOnRow(Column to, Map<Field, Figure> fields) {
		int shift = to.ordinal() < getField().getColumnId().ordinal() ? -1 : 1;
		Field next = this.getField().getShiftedField(0, shift);
		while(next.getColumnId().ordinal() > to.ordinal()) {
			if(fields.containsKey(next)) {
				return true;
			}
			next = next.getShiftedField(0, shift);
		}
		return false;
	}
	
}
