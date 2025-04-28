/**
 * This class is to track everything going on with the cells. The names should be
 * self explanatory. Each variable has a setter and a getter method, except
 * numAdjacentMines doesn't have a setter method, only a method to increment by one 
 * because all that is needed for it is to increment it by one
 */
package com.zetcode;

public class Cell {
	private boolean isEmptyCell; // Is this an empty cell?
	private boolean isMine; // Is this a mine?
	private boolean isTreasure; // Is this treasure?
	private boolean isFlagged; // Is this flagged?
	private boolean isCovered; // Is this covered? As in un-clicked/undiscovered
	private int numAdjacentMines; // Number of mines adjacent to this cell
	
	
	// Default state of a cell
	public Cell() {
		isEmptyCell = true;
		isMine = false;
		isTreasure = false;
		isFlagged = false;
		isCovered = true;
		numAdjacentMines = 0;
	}
	
	public Cell(int value) {
		int modValue = value % 100;
		if (modValue < 10) {
			this.isCovered = false;
		}
		if (value % 10 != 0) {
			this.isEmptyCell = false;
		}
		if (8 >= value % 10 && value % 10 >= 1) {
			this.numAdjacentMines = value;
		}
		if (value % 10 == 9) {
			this.isMine = true;
		}
		if (value >= 100) {
			this.isTreasure = true;
		}
		if (29 >= modValue && modValue >= 20) {
			this.isFlagged = true;
		}
	}
	
	public boolean isEmptyCell() {
		return isEmptyCell;
	}
	
	public boolean isMine() {
		return isMine;
	}
	
	public boolean isTreasure() {
		return isTreasure;
	}
	
	public boolean isFlagged() {
		return isFlagged;
	}
	
	public boolean isCovered() {
		return isCovered;
	}
	
	public int number() {
		return numAdjacentMines;
	}
	
	
	public void setIsEmptyCell(boolean val) {
		isEmptyCell = val;
	}
	
	public void setIsMine(boolean val) {
		isMine = val;
	}
	
	public void setIsTreasure(boolean val) {
		isTreasure = val;
	}
	
	public void setIsFlagged(boolean val) {
		isFlagged = val;
	}
	
	public void setIsCovered(boolean val) {
		isCovered = val;
	}
	
	public void addOneToNumber() {
		numAdjacentMines += 1;
	}
	
	
	public boolean isNumberedCell() {
		return !isEmptyCell && !isMine;
	}
	
	public boolean isCoveredAndFlaggedMine() {
		return isCovered() && isFlagged() && isMine();
	}
	
	/**
	 * This method gets the numeric representation of the cell, used to save the 
	 * board in the form of a text file.
	 * 
	 * 0 = Empty
	 * 1-8 = Cell adjacent to this number of mines
	 * 9 = Mine
	 * 
	 * 10 = Empty but not uncovered
	 * 11-18 = Cell adjacent to this number of mines but not uncovered
	 * 19 = Mine but not uncovered
	 * 
	 * 20 = Empty but not uncovered and is flagged
	 * 21-28 = Cell adjacent to this number of mines but not uncovered and is flagged
	 * 29 = Mine but not uncovered and is flagged
	 * 
	 * @return returns the number representation of the cell
	 */
	public int saveBoardValue() {
		int i = this.number();
		
		if (this.isEmptyCell()) {
			i = 0;
		} else if (this.isMine()) {
			i = 9;
		}
		
		if (this.isCovered()) {
			i += 10;
		}
		
		if (this.isFlagged()) {
			i += 10;
		}
		if (this.isTreasure()) {
			i += 100;
		}
		
		return i;
	}
	
	
	
}
