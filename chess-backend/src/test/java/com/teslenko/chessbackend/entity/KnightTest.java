package com.teslenko.chessbackend.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.teslenko.chessbackend.entity.figures.Figure;
import com.teslenko.chessbackend.entity.figures.FigureType;
import com.teslenko.chessbackend.exception.ImpossibleMoveException;
import com.teslenko.chessbackend.exception.NoSuchFigureException;
import com.teslenko.chessbackend.service.StandartDeskService;

public class KnightTest {
	Desk desk = new StandartDeskService().create();

	@Test
	public void testFalseMoveStraight() {
		Field field = new Field(1, Column.b);
		Field moveField = new Field(5, Column.e);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.white, field, moveField));
	}
	@Test
	public void testFalseMoveOutDesk() {
		Field field = new Field(1, Column.b);
		Field moveField = new Field(-3, Column.f);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.white, field, moveField));
	}
	@Test
	public void testFalseMoveTakeSameColor() {
		Field field = new Field(1, Column.b);
		Field moveField = new Field(2, Column.d);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.white, field, moveField));
	}
	
	@Test
	public void testMoveStraight() {
		Field field = new Field(1, Column.b);
		Field moveField = new Field(3, Column.c);
		desk.moveFigure(Color.white, field, moveField);
		assertTrue(desk.getFields().containsKey(moveField));
		assertEquals(desk.getFields().get(moveField).getType(), FigureType.knight);
		System.out.println(desk.nicePrinted());
	}
	
	@Test
	public void testEatByWhite() {
		Field field1 = new Field(7, Column.d);
		Field moveField1 = new Field(5, Column.d);
		desk.moveFigure(Color.black, field1, moveField1);
		field1 = new Field(1, Column.b);
		moveField1 = new Field(3, Column.c);
		desk.moveFigure(Color.white, field1, moveField1);
		Field field = new Field(3, Column.c);
		Field moveField = new Field(5, Column.d);
		Figure takenFigure = desk.getFields().get(moveField);
		desk.moveFigure(Color.white, field, moveField);
		assertTrue(desk.getFields().containsKey(moveField));
		assertTrue(desk.getTakenFiguresBlack().contains(takenFigure));
		System.out.println(desk.nicePrinted());
	}
}
