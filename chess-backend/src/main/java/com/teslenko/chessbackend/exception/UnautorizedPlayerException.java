package com.teslenko.chessbackend.exception;

public class UnautorizedPlayerException extends ChessException{
	public UnautorizedPlayerException(String msg) {
		super(msg);
	}
}
