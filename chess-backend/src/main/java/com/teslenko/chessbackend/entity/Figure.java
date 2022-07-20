package com.teslenko.chessbackend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.teslenko.chessbackend.exception.ImpossibleMoveException;

/**
 * Chess figure with coordinates on desk, color and type/
 * @author Mykhailo Teslenko
 *
 */
public abstract class Figure {
	private Field field;
	private Color color;
	private FigureType type;
	private boolean isAlive = true;
	
	public Figure(Field field, Color color, FigureType type) {
		this.field = field;
		this.color = color;
		this.type = type;
	}
	
	public Figure() {
	}
	
	/**
	 * Moves figure to the given field if possible or throws an exception.
	 * @param rowId
	 * @param columnId
	 */
	public void move(Desk desk, Field field) {
		Map<Field, Figure> fields = desk.getFields();
		if(field.getRowId() < 1 || field.getRowId() > 8) {
			throw new ImpossibleMoveException("Impossible move for figure: " + this + ", field: " + field);
		}
		List<Field> moves = availableMoves(fields);
		if(moves.contains(field)) {
			if(fields.containsKey(field)) {
				Figure figureToTake = fields.get(field);
				if(figureToTake.getColor() != getColor()) {
					desk.takeFigure(fields.get(field));
				} else {
					throw new ImpossibleMoveException("Impossible to take figure with same color moving figure " + this +  ", field " + field);
				}
			} 
			fields.remove(this.field);
			this.field = field;
			fields.put(field, this);
		} else {
			throw new ImpossibleMoveException("Impossible move for figure: " + this + ", field: " + field);
		}
	}
	
	public boolean addMoveIfValid(int rowShift, int columnShift, Map<Field, Figure> fields, List<Field> moves) {
		Field field = getField();
		if(field.isValidShift(rowShift, columnShift)) {
			Field shifted = field.getShiftedField(rowShift, columnShift);
			if(!fields.containsKey(shifted) || fields.get(shifted).getColor() != this.getColor()) {
				moves.add(shifted);
				return true;
			}
		}
		return false;
	}
	
	public abstract List<Field> availableMoves(Map<Field, Figure> fields);
	
	
	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public FigureType getType() {
		return type;
	}
	public void setType(FigureType type) {
		this.type = type;
	}

	public boolean getIsAlive() {
		return isAlive;
	}

	public void setIsAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	@Override
	public String toString() {
		return field.getColumnId().toString() + field.getRowId() + " " + type +  "(" + color.toString().substring(0,1) + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + (isAlive ? 1231 : 1237);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Figure other = (Figure) obj;
		if (color != other.color)
			return false;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		if (isAlive != other.isAlive)
			return false;
		if (type != other.type)
			return false;
		return true;
	}


	
}
