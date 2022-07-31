package com.teslenko.chessbackend.entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.teslenko.chessbackend.service.StandartDeskService;

public class CheckStateTest {
	Desk desk = new StandartDeskService().create();
	
	@Test
	public void noCheckInitial() {
		desk.proccessCheck();
		assertFalse(desk.getIsUnderCheckWhite());
		assertFalse(desk.getIsUnderCheckBlack());
	}
	
	@Test
	public void checkWhite() {
		desk.moveFigure(Color.black, new Field(7, Column.e), new Field(5, Column.e));
		desk.moveFigure(Color.black, new Field(8, Column.f), new Field(4, Column.b));
		desk.moveFigure(Color.white,  new Field(2, Column.d), new Field(4, Column.d));
		assertTrue(desk.getIsUnderCheckWhite());
	}
	
	@Test
	public void checkRemovedWhite() {
		desk.moveFigure(Color.black, new Field(7, Column.e), new Field(5, Column.e));
		desk.moveFigure(Color.black, new Field(8, Column.f), new Field(4, Column.b));
		desk.moveFigure(Color.white,  new Field(2, Column.d), new Field(4, Column.d));
		assertTrue(desk.getIsUnderCheckWhite());
		desk.moveFigure(Color.black, new Field(4, Column.b), new Field(3, Column.a));
		assertFalse(desk.getIsUnderCheckWhite());
	}
}
