package com.teslenko.chessbackend.entity.figures;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.teslenko.chessbackend.entity.Color;
import com.teslenko.chessbackend.entity.Desk;
import com.teslenko.chessbackend.entity.Field;
import com.teslenko.chessbackend.entity.Move;
import com.teslenko.chessbackend.entity.MoveRecord;
import com.teslenko.chessbackend.exception.ImpossibleMoveException;

/**
 * Chess figure with coordinates on desk, color and type/
 * 
 * @author Mykhailo Teslenko
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Figure implements Cloneable {
	private static final Logger LOG = LoggerFactory.getLogger(Figure.class);
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Embedded
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
	 * 
	 * @param rowId
	 * @param columnId
	 */
	public Figure move(Desk desk, Field field) {
		Map<Field, Figure> fields = desk.getFields();
		Figure figureToTake = null;
		if (field.getRowId() < 1 || field.getRowId() > 8) {
			throw new ImpossibleMoveException("Impossible move for figure: " + this + ", field: " + field);
		}
		List<Field> moves = checkStateMovesRemove(desk, availableMoves(desk));
		if (moves.contains(field)) {
			if (fields.containsKey(field)) {
				figureToTake = fields.get(field);
				if (figureToTake.getColor() != getColor()) {
					desk.takeFigure(fields.get(field));
				} else {
					LOG.error("Impossible to take figure with same color moving figure {}, field {}", this, field);
					throw new ImpossibleMoveException(
							"Impossible to take figure with same color moving figure " + this + ", field " + field);
				}
			}
			fields.remove(this.field);
			this.field = field;
			fields.put(field, this);
		} else {
			throw new ImpossibleMoveException("Impossible move for figure: " + this + ", field: " + field);
		}
		return figureToTake;
	}

	public boolean addMoveIfValid(int rowShift, int columnShift, Map<Field, Figure> fields, List<Field> moves) {
		Field field = getField();
		if (field.isValidShift(rowShift, columnShift)) {
			Field shifted = field.getShiftedField(rowShift, columnShift);
			if (!fields.containsKey(shifted)) {
				moves.add(shifted);
				return true;
			} else if (fields.get(shifted).getColor() != this.getColor()) {
				moves.add(shifted);
			}
		}
		return false;
	}

	@Override
	public abstract Object clone() throws CloneNotSupportedException;

	public abstract List<Field> availableMoves(Desk desk);

	/**
	 * Removes moves which are impossible under check state (i.e. after move check
	 * state stays) Move performed on desk's copy, so real desk is not changed.
	 * 
	 * @param desk
	 * @param moves
	 */
	public List<Field> checkStateMovesRemove(Desk desk, List<Field> moves) {
		if ((color == Color.white && desk.getIsUnderCheckWhite())
				|| (color == Color.black && desk.getIsUnderCheckBlack())) {
			Desk tempDesk = new Desk(desk);
			Iterator<Field> iterator = moves.iterator();
			while (iterator.hasNext()) {
				Field move = iterator.next();
				Map<Field, Figure> fields = tempDesk.getFields();
				Figure figureToTake = null;
				if (fields.containsKey(move)) {
					figureToTake = fields.get(move);
					if (figureToTake.getColor() != getColor()) {
						tempDesk.takeFigure(fields.get(move));
					} 
				}
				fields.remove(this.field);
				fields.put(move, this);
				Field old = this.field;
				this.field = move;
				tempDesk.getMoveRecords().add(new MoveRecord(new Move(this.field, move), color, figureToTake));
				tempDesk.proccessCheck();
				if ((color == Color.white && tempDesk.getIsUnderCheckWhite())
						|| (color == Color.black && tempDesk.getIsUnderCheckBlack())) {
					iterator.remove();
				}
				tempDesk.undoMove();
				this.field = old;
			}
		}
		
		return moves;
	}

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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return field.getColumnId().toString() + field.getRowId() + " " + type + "(" + color.toString().substring(0, 1)
				+ ")";
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
