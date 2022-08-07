package com.teslenko.chessbackend.entity;

import static com.teslenko.chessbackend.entity.Color.black;
import static com.teslenko.chessbackend.entity.Color.white;
import static com.teslenko.chessbackend.entity.Column.d;
import static com.teslenko.chessbackend.entity.Column.e;
import static com.teslenko.chessbackend.entity.Column.f;
import static com.teslenko.chessbackend.entity.Column.g;
import static com.teslenko.chessbackend.entity.Column.h;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.teslenko.chessbackend.entity.figures.Figure;
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
	@Test
	public void testNoMoveDiagonal() {
		Field from = new Field(2, g);
		Field to  = new Field(4, g);
		desk.moveFigure(white, from, to);
		from = new Field(7, e);
		to  = new Field(5, e);
		desk.moveFigure(black, from, to);
		from = new Field(2, f);
		to  = new Field(3, f);
		desk.moveFigure(white, from, to);
		from = new Field(8, d);
		to  = new Field(4, h);
		desk.moveFigure(black, from, to);
//		Field cfrom = new Field(2, h);
//		Figure pawn = desk.getFields().get(cfrom);
//		System.out.println(pawn + " moves: " + pawn.availableMoves(desk));
		Field cfrom = new Field(2, h);
		Field cto  = new Field(3, g);
		System.out.println(desk.getFields().get(cto));
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(white, cfrom, cto));
	}
}
