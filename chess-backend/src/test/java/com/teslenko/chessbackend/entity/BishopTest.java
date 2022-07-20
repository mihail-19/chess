package com.teslenko.chessbackend.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.teslenko.chessbackend.exception.ImpossibleMoveException;
import com.teslenko.chessbackend.exception.NoSuchFigureException;

public class BishopTest {
	Desk desk = new DeskFactory().getDesk();

	@Test
	public void testFalseMoveUpLeft() {
		Field field = new Field(1, Column.c);
		Field moveField = new Field(2, Column.b);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(field, moveField));
	}
	@Test
	public void testFalseMoveUpRight() {
		Field field = new Field(1, Column.c);
		Field moveField = new Field(2, Column.d);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(field, moveField));
	}
	@Test
	public void testFalseMoveDownLeft() {
		Field field = new Field(8, Column.c);
		Field moveField = new Field(7, Column.b);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(field, moveField));
	}
	@Test
	public void testFalseDownRight() {
		Field field = new Field(8, Column.c);
		Field moveField = new Field(7, Column.d);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(field, moveField));
	}
	
	@Test
	public void testMove() {
		Field field = new Field(2, Column.d);
		Field moveField = new Field(4, Column.d);
		desk.moveFigure(field, moveField);
		field = new Field(1, Column.c);
		moveField = new Field(3, Column.e);
		desk.moveFigure(field, moveField);
		assertTrue(desk.getFields().containsKey(moveField));
		assertEquals(desk.getFields().get(moveField).getType(), FigureType.bishop);
		System.out.println(desk.nicePrinted());
	}
	@Test
	public void testEatFigure() {
		Field field = new Field(2, Column.d);
		Field moveField = new Field(4, Column.d);
		desk.moveFigure(field, moveField);
		field = new Field(1, Column.c);
		moveField = new Field(4, Column.f);
		desk.moveFigure(field, moveField);
		field = new Field(4, Column.f);
		moveField = new Field(7, Column.c);
		Figure takenFigure = desk.getFields().get(moveField);
		desk.moveFigure(field, moveField);
		assertTrue(desk.getFields().containsKey(moveField));
		assertEquals(desk.getFields().get(moveField).getType(), FigureType.bishop);
		assertFalse(desk.getFigures().contains(takenFigure));
		assertTrue(desk.getTakenFiguresBlack().contains(takenFigure));
		System.out.println(desk.nicePrinted());
	}
	
}
