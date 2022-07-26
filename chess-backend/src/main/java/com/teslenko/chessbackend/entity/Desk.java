package com.teslenko.chessbackend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teslenko.chessbackend.exception.ImpossibleMoveException;
import com.teslenko.chessbackend.exception.NoSuchFigureException;

public class Desk {
	private static Logger LOG = LoggerFactory.getLogger(Desk.class);
	private static final int FIELD_STRING_LENGTH = 14;
	private List<Figure> figures;
	@JsonIgnore
	private Map<Field, Figure> fields;
	private List<Figure> takenFiguresWhite = new ArrayList<>();
	private List<Figure> takenFiguresBlack = new ArrayList<>();
	public Desk(List<Figure> figures) {
		this.figures = figures;
		fields = figures.stream()
				.collect(Collectors.toMap(Figure::getField, Function.identity()));
	}
	public void addFigure(Figure figure) {
		figures.add(figure);
	}
	
	public void takeFigure(Figure figure) {
		LOG.info("taking figure {}", figure);
		figure.setIsAlive(false);
		fields.remove(figure.getField());
		figures.remove(figure);
		if(figure.getColor() == Color.white) {
			takenFiguresWhite.add(figure);
		} else {
			takenFiguresBlack.add(figure);
		}
	}
	
	/**
	 * Moves figure to given field if possible, otherwise throws an Exception.
	 * 
	 * @param figure
	 * @param rowId
	 * @param columnId
	 */
	public void moveFigure(Color moveColor, Field from, Field to) {
		LOG.info("move from {} to {}", from, to);
		if(fields.containsKey(from)) {
			Figure deskFigure = fields.get(from);
			if(deskFigure.getColor() != moveColor) {
				LOG.error("error while move: figure color is {}, move color is {}", deskFigure.getColor(), moveColor);
				throw new ImpossibleMoveException("trying to move wrong color figure");
			}
			deskFigure.move(this, to);
		} else {
			throw new NoSuchFigureException("no figure to move in field [" + from + "]");
		}
		
	}
	
	/**
	 * Finds a figure on desk according to the given one
	 * @param figure
	 * @return
	 */
	public Figure getFigure(Figure figure) {
		if(!figures.contains(figure)) {
			throw new NoSuchFigureException("figure is not presented in field [" + figure + "]");
		}
		int index = figures.indexOf(figure);
		return figures.get(index);
	}
	
	
	
	
	
	
	public List<Figure> getFigures() {
		return figures;
	}
	public Map<Field, Figure> getFields() {
		return fields;
	}
	
	public List<Figure> getTakenFiguresWhite() {
		return takenFiguresWhite;
	}
	public void setTakenFiguresWhite(List<Figure> takenFiguresWhite) {
		this.takenFiguresWhite = takenFiguresWhite;
	}
	public List<Figure> getTakenFiguresBlack() {
		return takenFiguresBlack;
	}
	public void setTakenFiguresBlack(List<Figure> takenFiguresBlack) {
		this.takenFiguresBlack = takenFiguresBlack;
	}
	/**
	 * Printing in console according to chess desk view
	 * @return
	 */
	public String nicePrinted() {
		StringBuilder sb = new StringBuilder();
		sb.append("  ");
		for(int i = 0; i< Column.values().length; i++) {
			sb.append("|");
			String val = Column.values()[i].toString();
			sb.append(val);
			fillFieldSpaces(val.length(), sb);
			sb.append("|");
		}
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		for(int i = 8; i>0; i--) {
			sb.append(i + " ");
			appendRow(i, sb);
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
	private void appendRow(int rowId, StringBuilder sb) {
		List<Figure> rowFigures = figures.stream()
				.filter(f -> f.getField().getRowId() == rowId)
				.sorted((f1, f2) -> f1.getField().getColumnId().compareTo(f2.getField().getColumnId()))
			//	.map(f -> " " + f.getType().toString() + "(" + f.getColor().toString().substring(0,1) + ")")
				.collect(Collectors.toList());
		for(int i = 0; i<8; i++) {
			sb.append("|");
			if(rowFigures.size() > 0 && rowFigures.get(0).getField().getColumnId().ordinal() == i) {
				String str = rowFigures.get(0).toString();
				sb.append(str);
				fillFieldSpaces(str.length(), sb);
				rowFigures.remove(0);		
			} else {
				fillFieldSpaces(0, sb);
			}
			sb.append("|");
		}
		
	}
	private void fillFieldSpaces(int strLength, StringBuilder sb) {
		if(strLength < FIELD_STRING_LENGTH) {
			int diff = FIELD_STRING_LENGTH - strLength;
			for(int j = 0; j<diff; j++) {
				sb.append(" ");
			}
		}
	}
}
