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
	
}
