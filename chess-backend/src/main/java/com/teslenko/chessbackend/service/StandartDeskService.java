package com.teslenko.chessbackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.teslenko.chessbackend.entity.Color;
import com.teslenko.chessbackend.entity.Column;
import com.teslenko.chessbackend.entity.Desk;
import com.teslenko.chessbackend.entity.Field;
import com.teslenko.chessbackend.entity.figures.Bishop;
import com.teslenko.chessbackend.entity.figures.Figure;
import com.teslenko.chessbackend.entity.figures.FigureType;
import com.teslenko.chessbackend.entity.figures.King;
import com.teslenko.chessbackend.entity.figures.Knight;
import com.teslenko.chessbackend.entity.figures.Pawn;
import com.teslenko.chessbackend.entity.figures.Queen;
import com.teslenko.chessbackend.entity.figures.Rook;

@Service
public class StandartDeskService implements DeskService{

	@Override
	public Desk create() {
		List<Figure> figures = getStandartFigures();
		Desk desk = new Desk(figures);
		return desk;
	}

	private List<Figure> getStandartFigures(){
		List<Figure> figures = new ArrayList<>();
		for(int i = 0; i<8; i++) {
			figures.add(new Pawn(new Field(2, Column.values()[i]), Color.white, FigureType.pawn));
			figures.add(new Pawn(new Field(7, Column.values()[i]), Color.black, FigureType.pawn));
		}
		
		figures.add(new Knight(new Field(1, Column.b), Color.white, FigureType.knight));
		figures.add(new Knight(new Field(1, Column.g), Color.white, FigureType.knight));
		figures.add(new Knight(new Field(8, Column.b), Color.black, FigureType.knight));
		figures.add(new Knight(new Field(8, Column.g), Color.black, FigureType.knight));
		
		figures.add(new Bishop(new Field(1, Column.c), Color.white, FigureType.bishop));
		figures.add(new Bishop(new Field(1, Column.f), Color.white, FigureType.bishop));
		figures.add(new Bishop(new Field(8, Column.c), Color.black, FigureType.bishop));
		figures.add(new Bishop(new Field(8, Column.f), Color.black, FigureType.bishop));
		
		figures.add(new Rook(new Field(1, Column.a), Color.white, FigureType.rook));
		figures.add(new Rook(new Field(1, Column.h), Color.white, FigureType.rook));
		figures.add(new Rook(new Field(8, Column.a), Color.black, FigureType.rook));
		figures.add(new Rook(new Field(8, Column.h), Color.black, FigureType.rook));
		
		figures.add(new Queen(new Field(1, Column.d), Color.white, FigureType.queen));
		figures.add(new Queen(new Field(8, Column.d), Color.black, FigureType.queen));
		
		figures.add(new King(new Field(1, Column.e), Color.white, FigureType.king));
		figures.add(new King(new Field(8, Column.e), Color.black, FigureType.king));
		return figures;
	}
}
