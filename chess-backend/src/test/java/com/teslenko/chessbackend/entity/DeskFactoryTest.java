package com.teslenko.chessbackend.entity;

import org.junit.jupiter.api.Test;

public class DeskFactoryTest {

	@Test
	public void testStandartCreation() {
		DeskFactory factory = new DeskFactory();
		Desk desk = factory.getDesk();
		System.out.println(desk.nicePrinted());
	}
}
