package com.teslenko.chessbackend.entity;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.teslenko.chessbackend.exception.ImpossibleMoveException;
import com.teslenko.chessbackend.exception.NoSuchFigureException;

public class DeskTest {
	Desk desk = new DeskFactory().getDesk();
	
	@Test
	public void testMoveUnexistentFigure() {
		Field field =  new Field(3, Column.b);
		assertThrows(NoSuchFigureException.class, () -> desk.moveFigure(Color.white, field, new Field(5, Column.d)));
	}
	@Test
	public void testMovePawn() {
		System.out.println(desk.nicePrinted());
		Field figure = new Field(2, Column.e);
		Field moveField = new Field(4, Column.e);
		desk.moveFigure(Color.white, figure, moveField);
		System.out.println(desk.nicePrinted());
		System.out.println(desk.getFields());
		assertTrue(desk.getFields().containsKey(moveField));
	}
	@Test
	public void testFalseoMovePawn() {
		Field field = new Field(2, Column.e);
		Field moveField = new Field(5, Column.e);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.white, field, moveField));
	}
}
