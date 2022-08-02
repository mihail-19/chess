package com.teslenko.chessbackend.entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.teslenko.chessbackend.exception.ImpossibleMoveException;
import com.teslenko.chessbackend.exception.NoSuchFigureException;

public class PawnTest {
	Desk desk = new DeskFactory().getDesk();
	
	@Test
	public void testMove() {
		Field figure = new Field(2, Column.e);
		Field moveField = new Field(4, Column.e);
		desk.moveFigure(Color.white, figure, moveField);
		assertTrue(desk.getFields().containsKey(moveField));
	}
	@Test
	public void testFalseMoveStraight() {
		Field field = new Field(2, Column.e);
		Field moveField = new Field(5, Column.e);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.white, field, moveField));
	}
	@Test
	public void testFalseMoveTakeNoFigure() {
		Field field = new Field(2, Column.e);
		Field moveField = new Field(4, Column.f);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.white, field, moveField));
	}
	@Test
	public void testFalseMoveTakeSameColor() {
		Field field1 = new Field(2, Column.e);
		Field moveField1 = new Field(3, Column.e);
		desk.moveFigure(Color.white, field1, moveField1);
		Field field = new Field(2, Column.d);
		Field moveField = new Field(3, Column.e);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.white, field, moveField));
	}
	@Test
	public void testFalseMoveStacked() {
		Field field1 = new Field(2, Column.e);
		Field moveField1 = new Field(4, Column.e);
		desk.moveFigure(Color.white, field1, moveField1);
		field1 = new Field(7, Column.e);
		moveField1 = new Field(5, Column.e);
		desk.moveFigure(Color.black, field1, moveField1);
		Field field = new Field(4, Column.e);
		Field moveField = new Field(5, Column.e);
		
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.white,field, moveField));
		System.out.println(desk.nicePrinted());
	}
	@Test
	public void testEatByWhite() {
		Field field1 = new Field(7, Column.f);
		Field moveField1 = new Field(5, Column.f);
		desk.moveFigure(Color.black, field1, moveField1);
		field1 = new Field(2, Column.e);
		moveField1 = new Field(4, Column.e);
		desk.moveFigure(Color.white,field1, moveField1);
		Field field = new Field(4, Column.e);
		Field moveField = new Field(5, Column.f);
		Figure takenFigure = desk.getFields().get(moveField);
		desk.moveFigure(Color.white, field, moveField);
		assertTrue(desk.getFields().containsKey(moveField));
		assertTrue(desk.getTakenFiguresBlack().contains(takenFigure));
		
	}
}
