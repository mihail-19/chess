package com.teslenko.chessbackend.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.teslenko.chessbackend.exception.ImpossibleMoveException;
import com.teslenko.chessbackend.exception.NoSuchFigureException;
import com.teslenko.chessbackend.service.StandartDeskService;

public class KingTest {
	Desk desk = new StandartDeskService().create();

	@Test
	public void testFalseMoveUpLeft() {
		Field field = new Field(1, Column.e);
		Field moveField = new Field(2, Column.e);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.white, field, moveField));
	}
	@Test
	public void testFalseMoveTwoFields() {
		
		Field field = new Field(1, Column.e);
		Field moveField = new Field(3, Column.e);
		desk.getFields().remove(moveField);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.white, field, moveField));
	}
	@Test
	public void moveForward() {
		Field field = new Field(1, Column.e);
		Field moveField = new Field(2, Column.e);
		desk.getFields().remove(moveField);
		desk.moveFigure(Color.white, field, moveField);
		assertEquals(FigureType.king, desk.getFields().get(moveField).getType());
	}
	
	@Test
	public void moveLeft() {
		Field field = new Field(8, Column.e);
		Field moveField = new Field(8, Column.d);
		desk.getFields().remove(moveField);
		desk.moveFigure(Color.black, field, moveField);
		assertEquals(FigureType.king, desk.getFields().get(moveField).getType());
	}
	
	@Test
	public void castlingWhiteLeft() {
		Field field = new Field(1, Column.e);
		Field moveField = new Field(1, Column.c);
		desk.getFields().remove(field.getShiftedField(0, -1));
		desk.getFields().remove(field.getShiftedField(0, -2));
		desk.getFields().remove(field.getShiftedField(0, -3));
		System.out.println(desk.getFields());
		desk.moveFigure(Color.white, field, moveField);
		assertEquals(FigureType.king, desk.getFields().get(moveField).getType());
		assertEquals(FigureType.rook, desk.getFields().get(moveField.getShiftedField(0, 1)).getType());
		assertFalse(desk.getIsCastlingAvailableWhiteLeft());
		assertFalse(desk.getIsCastlingAvailableWhiteRight());
	}
	
}
