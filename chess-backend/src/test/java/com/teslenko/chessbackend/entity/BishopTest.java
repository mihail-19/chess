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

public class BishopTest {
	Desk desk = new StandartDeskService().create();

	@Test
	public void testFalseMoveUpLeft() {
		Field field = new Field(1, Column.c);
		Field moveField = new Field(2, Column.b);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.white, field, moveField));
	}
	@Test
	public void testFalseMoveUpRight() {
		Field field = new Field(1, Column.c);
		Field moveField = new Field(2, Column.d);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.white, field, moveField));
	}
	@Test
	public void testFalseMoveDownLeft() {
		Field field = new Field(8, Column.c);
		Field moveField = new Field(7, Column.b);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.black, field, moveField));
	}
	@Test
	public void testFalseDownRight() {
		Field field = new Field(8, Column.c);
		Field moveField = new Field(7, Column.d);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.black, field, moveField));
	}
	
	@Test
	public void testMove() {
		Field field = new Field(2, Column.d);
		Field moveField = new Field(4, Column.d);
		desk.moveFigure(Color.white, field, moveField);
		field = new Field(1, Column.c);
		moveField = new Field(3, Column.e);
		desk.moveFigure(Color.white, field, moveField);
		assertTrue(desk.getFields().containsKey(moveField));
		assertEquals(desk.getFields().get(moveField).getType(), FigureType.bishop);
		System.out.println(desk.nicePrinted());
	}
	
	@Test
	public void testNotMoveThroughFigures() {
		Field field = new Field(2, Column.d);
		Field moveField = new Field(4, Column.d);
		desk.moveFigure(Color.white, field, moveField);
		field = new Field(1, Column.c);
		moveField = new Field(4, Column.f);
		desk.moveFigure(Color.white, field, moveField);
		Field cfield = new Field(4, Column.f);
		Field cmoveField = new Field(8, Column.b);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.white, cfield, cmoveField));
		System.out.println(desk.nicePrinted());
	}
	@Test
	public void testEatFigure() {
		Field field = new Field(2, Column.d);
		Field moveField = new Field(4, Column.d);
		desk.moveFigure(Color.white, field, moveField);
		field = new Field(1, Column.c);
		moveField = new Field(4, Column.f);
		desk.moveFigure(Color.white, field, moveField);
		field = new Field(4, Column.f);
		moveField = new Field(7, Column.c);
		Figure takenFigure = desk.getFields().get(moveField);
		desk.moveFigure(Color.white, field, moveField);
		assertTrue(desk.getFields().containsKey(moveField));
		assertEquals(desk.getFields().get(moveField).getType(), FigureType.bishop);
		assertTrue(desk.getTakenFiguresBlack().contains(takenFigure));
		System.out.println(desk.nicePrinted());
	}
	
}
