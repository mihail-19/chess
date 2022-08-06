package com.teslenko.chessbackend.entity;

/**
 * Chess color constants
 * @author Mykhailo Teslenko
 *
 */
public enum Color {
	white, black;
	
	public Color other() {
		if(this == white) {
			return black;
		} else {
			return white;
		}
	}
}
