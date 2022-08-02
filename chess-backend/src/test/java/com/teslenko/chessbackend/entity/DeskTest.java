package com.teslenko.chessbackend.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
		Field figure = new Field(2, Column.e);
		Field moveField = new Field(4, Column.e);
		desk.moveFigure(Color.white, figure, moveField);
		assertTrue(desk.getFields().containsKey(moveField));
		assertTrue(desk.getMoveRecords().size() == 1);
		MoveRecord mr = desk.getMoveRecords().get(0);
		assertEquals(mr.getMove().getFrom(), figure);
		assertEquals(mr.getMove().getTo(), moveField);
	}
	@Test
	public void testFalseoMovePawn() {
		Field field = new Field(2, Column.e);
		Field moveField = new Field(5, Column.e);
		assertThrows(ImpossibleMoveException.class, () -> desk.moveFigure(Color.white, field, moveField));
	}
	
	@Test
	public void cloning() {
		Desk other = new Desk(desk);
		assertEquals(other.getFields(), desk.getFields());
		other.getFields().remove(new Field(2, Column.e));
		assertNotEquals(other.getFields(), desk.getFields());
	}
	
	@Test
	public void testUndoMove() {
		Field figure = new Field(2, Column.e);
		Field moveField = new Field(4, Column.e);
		desk.moveFigure(Color.white, figure, moveField);
		assertTrue(desk.getFields().containsKey(moveField));
		assertEquals(desk.getFields().get(moveField).getType(), FigureType.pawn);
		assertTrue(desk.getMoveRecords().size() == 1);
		System.out.println(desk.nicePrinted());
		desk.undoMove();
		assertEquals(desk.getMoveRecords().size(), 0);
		System.out.println(desk.nicePrinted());
		assertTrue(desk.getFields().containsKey(figure));
		assertEquals(desk.getFields().get(figure).getType(), FigureType.pawn);
		assertFalse(desk.getFields().containsKey(moveField));
		assertEquals(desk.getFields().get(figure).getType(), FigureType.pawn);
	}
}
