package com.teslenko.chessbackend.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.teslenko.chessbackend.exception.ImpossibleMoveException;
import com.teslenko.chessbackend.service.StandartDeskService;


public class CheckMovesTest {
	Desk desk = new StandartDeskService().create();

	
	@Test
	public void testMove() {
		Field field = new Field(2, Column.d);
		Field moveField = new Field(4, Column.d);
		desk.moveFigure(Color.white, field, moveField);
		field = new Field(7, Column.e);
		moveField = new Field(5, Column.e);
		desk.moveFigure(Color.black, field, moveField);
		field = new Field(8, Column.f);
		moveField = new Field(4, Column.b);
		desk.moveFigure(Color.black, field, moveField);
		System.out.println(desk.nicePrinted());
		Field cfield = new Field(2, Column.e);
		Field cmoveField = new Field(4, Column.e);
		assertThrows(ImpossibleMoveException.class,() -> desk.moveFigure(Color.white, cfield, cmoveField));
		field = new Field(1, Column.d);
		moveField = new Field(2, Column.d);
		desk.moveFigure(Color.white, field, moveField);
	}
	
}
