package com.teslenko.chessbackend.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class FieldTest {

	@Test
	public void shiftTestOk() {
		Field field = new Field(2, Column.e);
		Field shifted = field.getShiftedField(2, 0);
		assertEquals(shifted.getRowId(), 4);
		assertEquals(shifted.getColumnId(), Column.e);
		Field shifted2 = field.getShiftedField(5, 1);
		assertEquals(shifted2.getRowId(), 7);
		assertEquals(shifted2.getColumnId(), Column.f);
	}
	
	@Test
	public void shiftTestFail() {
		Field field = new Field(2, Column.e);
		assertThrows(IllegalArgumentException.class, () -> field.getShiftedField(-2, 0));
		
	}
}
