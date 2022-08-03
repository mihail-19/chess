package com.teslenko.chessbackend.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.teslenko.chessbackend.service.StandartDeskService;
import static com.teslenko.chessbackend.entity.Column.*;
import static com.teslenko.chessbackend.entity.Color.*;

public class TestCheckmate {
	Desk desk = new StandartDeskService().create();
	
	@Test
	public void testNoCheckmate() {
		Field from = new Field(2, e);
		Field to  = new Field(4, e);
		desk.moveFigure(white, from, to);
		assertEquals(desk.getCheckmate(), Checkmate.NONE);
	}
	
	@Test
	public void testWhiteCheckmate() {
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
		System.out.println(desk.nicePrinted());
		assertEquals(desk.getCheckmate(), Checkmate.WHITE_CHECKMATE);
	}
}
