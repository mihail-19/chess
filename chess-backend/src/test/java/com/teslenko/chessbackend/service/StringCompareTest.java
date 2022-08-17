package com.teslenko.chessbackend.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class StringCompareTest {

	@Test
	public void testString() {
		String strBig = "John";
		String part = "Jo";
		assertTrue(strBig.contains(part));
		assertTrue(strBig.contains("oh"));
		assertFalse(strBig.contains("oeh"));
	}
	@Test
	public void testOther() {
		String strBig = "usr1";
		String part = "sr";
		assertTrue(strBig.contains(part));
		assertFalse(strBig.contains("ser"));
	}
}
