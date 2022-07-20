package com.teslenko.chessbackend.entity;

public class Field {
	private int rowId;
	private Column columnId;
	
	public Field(int rowId, Column columnId) {
		this.rowId = rowId;
		this.columnId = columnId;
	}
	
	public Field() {
	}
	
	public Field getShiftedField(int rowShift, int columnShift) {
		int shiftedRow = rowId + rowShift;
		int shiftedColumn = columnId.ordinal() + columnShift;
		if(shiftedRow < 1 || shiftedRow > 8) {
			throw new IllegalArgumentException("could not shift to this row: " + shiftedRow);
		}
		if(shiftedColumn < 0 || shiftedColumn >= Column.values().length) {
			throw new IllegalArgumentException("could not shift to this column: " + shiftedColumn);
		}
		return new Field(shiftedRow, Column.values()[shiftedColumn]);
	}
	
	/**
	 * Indicates whether given shift is valid and is not out of desk.
	 * @param rowShift
	 * @param columnShift
	 * @return
	 */
	public boolean isValidShift(int rowShift, int columnShift) {
		int shiftedRow = rowId + rowShift;
		int shiftedColumn;
		try {
			shiftedColumn = columnId.ordinal() + columnShift;
		}catch(Exception e) {
			return false;
		}
		if(shiftedRow < 1 || shiftedRow > 8) {
			return false;
		}
		if(shiftedColumn < 0 || shiftedColumn >= Column.values().length) {
			return false;
		}
		return true;
	}
	
	public int getRowId() {
		return rowId;
	}
	public void setRowId(int rowId) {
		this.rowId = rowId;
	}
	public Column getColumnId() {
		return columnId;
	}
	public void setColumnId(Column columnId) {
		this.columnId = columnId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columnId == null) ? 0 : columnId.hashCode());
		result = prime * result + rowId;
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
		Field other = (Field) obj;
		if (columnId != other.columnId)
			return false;
		if (rowId != other.rowId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Field [rowId=" + rowId + ", columnId=" + columnId + "]";
	}
	
}
