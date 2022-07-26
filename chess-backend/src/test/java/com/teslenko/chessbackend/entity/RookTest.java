package com.teslenko.chessbackend.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.teslenko.chessbackend.exception.ImpossibleMoveException;

public class RookTest {
	Desk desk = new DeskFactory().getDesk();

	@Test
	public void testFalseMoveUp() {
		Field field = new Field(1, Column.a);
		Field moveField = new Field(3, Column.a);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.white, field, moveField));
	}
	@Test
	public void testFalseMoveRight() {
		Field field = new Field(1, Column.a);
		Field moveField = new Field(1, Column.b);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.white, field, moveField));
	}
	@Test
	public void testFalseMoveDown() {
		Field field = new Field(8, Column.h);
		Field moveField = new Field(7, Column.h);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.black, field, moveField));
	}
	@Test
	public void testFalseLeft() {
		Field field = new Field(8, Column.h);
		Field moveField = new Field(8, Column.g);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.black, field, moveField));
	}
	
	@Test
	public void testMove() {
		Field field = new Field(2, Column.a);
		Field moveField = new Field(4, Column.a);
		desk.moveFigure(Color.white, field, moveField);
		field = new Field(1, Column.a);
		moveField = new Field(3, Column.a);
		desk.moveFigure(Color.white, field, moveField);
		assertTrue(desk.getFields().containsKey(moveField));
		assertEquals(desk.getFields().get(moveField).getType(), FigureType.rook);
		System.out.println(desk.nicePrinted());
	}
	
	@Test
	public void testFalseMove() {
		Field field1 = new Field(2, Column.a);
		Field moveField1 = new Field(4, Column.a);
		desk.moveFigure(Color.white, field1, moveField1);
		Field field = new Field(1, Column.a);
		Field moveField = new Field(6, Column.d);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.white, field, moveField));
	}
	
	@Test
	public void testTakeFigure() {
		Field field = new Field(2, Column.a);
		Field moveField = new Field(4, Column.a);
		desk.moveFigure(Color.white, field, moveField);
		field = new Field(1, Column.a);
		moveField = new Field(3, Column.a);
		desk.moveFigure(Color.white, field, moveField);
		field = new Field(3, Column.a);
		moveField = new Field(3, Column.d);
		desk.moveFigure(Color.white, field, moveField);
		field = new Field(3, Column.d);
		moveField = new Field(7, Column.d);
		Figure takenFigure = desk.getFields().get(moveField);
		desk.moveFigure(Color.white, field, moveField);
		assertTrue(desk.getFields().containsKey(moveField));
		assertEquals(desk.getFields().get(moveField).getType(), FigureType.rook);
		assertFalse(desk.getFigures().contains(takenFigure));
		assertTrue(desk.getTakenFiguresBlack().contains(takenFigure));
		System.out.println(desk.nicePrinted());
	}
}
