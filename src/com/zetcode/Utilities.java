/**
 * Author: Nick Pieroni
 * Date: 3/14/2025
 */

/*
 * Changelog:
 * 1. This is the initial state of the file.
 * 
 * Explanation:
 * 1. This is the starting point, update this list after this.
 */

package com.zetcode;

// Import necessary classes
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Utilities {

	/**
	 * This will count all the valid save files (-save.csv) in the saves folder.
	 * 
	 * @return		count: The number of valid save files in the saves folder
	 */
	public static int countSaveFiles() {

		System.out.println("Utilities.countSaveFiles(): Counting save files...");

		// Initialize the path and 
		String savesFolderPath = "src\\saves";
		File savesFolder = new File(savesFolderPath);

		// Check that the saves folder exists
		if (!savesFolder.exists() || !savesFolder.isDirectory()) {

            System.err.println("Utilities.countSaveFiles(): Save folder has moved or been obstructed: " + savesFolderPath);
			return -1; // No files exist
        }

		File[] files = savesFolder.listFiles();

		// Count save files
		int count = 0;
		for (File file : files) {

			if (file.isFile()) { // Check if the file is a valid file

				if (isValidSaveFile(file)) {
					count++;
				}
			}
		}

		System.out.println("Utilities.countSaveFiles(): Save files counted.");		

		return count;
	}

	/**
	 * This will iterate through the saves folder and get the names of 
	 * all the files that end in "-save.csv". All other files will be
	 * ignored. 
	 * 
	 * @return		ArrayList<String> resultArrayList: This will contain 
	 * 				the names of the save files.
	 */
	// TODO remove unnecessary check for the "-save.csv", already checked in helper method isValidFile()
	// Can we replace lines 119 - 126 with:
 	/**
  	 * if (isValidSaveFile(file)) {
 	 * resultName = currentName.substring(0, currentName.length() - 9); // Remove the -save.csv
 	 * resultArrayList.add(resultName);
          * }
 	 */
	public static ArrayList<String> getSaveFileNames() {

		System.out.println("Utilities.getSaveFileNames(): Getting save file names...");

		String savesFolderPath = "src\\saves";
		File savesFolder = new File(savesFolderPath);

		// Create saves folder if it does not exist
		try {
			
			if (!savesFolder.exists()) {

				System.out.println("Utilities.getSaveFileNames(): Save folder does not exist at path: " + savesFolderPath + ", creating folder...");
				savesFolder.mkdir();
			}
		} 
		catch (Exception e) {

			System.err.println("Utilities.getSaveFileNames(): Error when attempting to create new saves folder.");
			return null;
		}

		System.out.println("Utilities.getSaveFileNames(): Save folder created successfully at path: " + savesFolderPath + ".");

		File[] files = savesFolder.listFiles();
		ArrayList<String> resultArrayList = new ArrayList<>();

		if (files != null) {
			
			String currentName;
			String resultName;

			for (File file : files) {
				
				resultName = ""; // Reset variable before going to next file name

				currentName = file.getName();

				if (currentName.endsWith("-save.csv")) { // Check for valid save files
					resultName = currentName.substring(0, currentName.length() - 9); // Remove the -save.csv at the end
				}
				else {
					resultName = currentName;
				}

				resultArrayList.add(resultName);
			}

			System.out.println("Utilities.getSaveFileNames(): Save file names stored.");

			return resultArrayList;
		}
		else { // Empty saves folder

			System.err.println("Utilities.getSaveFileNames(): Save file folder is empty.");

			return null;
		}
	}

	/**
	 * This method will be used to check if the file is valid and should
	 * be counted when iterating through the saves folder.
	 * 
	 * @param saveFile		This is the File object to be checked.
	 * 
	 * @return				True: if the file does contain '-save.csv' 
	 * 						at the end of the file name.
	 * @return 				False: if the file is does not contain 
	 * 						"-save.csv" at the end of the file name.
	 */
	// TODO Add hashing to files to prevent cheating??
	private static boolean isValidSaveFile(File saveFile) {

		System.out.println("Utilities.isValidSaveFile(File saveFile): Checking file: " + saveFile + " for validity...");

		if (saveFile.getName().endsWith("-save.csv")) {

			System.out.println("Utilities.isValidSaveFile(File saveFile): File: " + saveFile + " is valid.");
			return true;
		}
		else {

			System.out.println("Utilities.isValidSaveFile(File saveFile): File: " + saveFile + " is NOT valid.");
			return false;
		}
	}

	/**
	 * This will create a new file with the given name, then iterate through 
	 * the board information, stored in cellValues, to write to the newly 
	 * created csv file.
	 * 
	 * @param cellValues	Stores the information of the board (See Board class)
	 * @param saveName		The name of the new save
	 * @param boardWidth	The N_COLS stored in the Board class
	 * 
	 * @implNote	Do not remove the @SuppressWarnings("ConvertToTryWithResources").
	 * 				It is there as this line 
	 * 				FileWriter write = new FileWriter(newSaveFile);, is causing it.
	 * 				The implementation now is completely correct so do not remove it.
	 */
    @SuppressWarnings("ConvertToTryWithResources")
	public static void saveBoard(Cell[] field, String saveName, int boardWidth, int treasure, int UI) {

		System.out.println("Utilities.saveBoard(ArrayList<Integer> cellValues, String saveName, int boardWidth): Saving board...");
		
		String saveFilePath = "src\\saves\\" + saveName + "-save.csv";

		try {
			
			File newSaveFile = new File(saveFilePath);
			
			if (!newSaveFile.createNewFile()) {
				System.err.println("Utilities.saveBoard(ArrayList<Integer> cellValues, String saveName, int boardWidth): Error, save file with that name already exists.");
			} 
			else {
				System.out.println("Utilities.saveBoard(ArrayList<Integer> cellValues, String saveName, int boardWidth): Board saved.");
			}

			// Write data to new save file
			FileWriter write = new FileWriter(newSaveFile);
			write.append("Board Name: " + saveName + "\n");
			// TODO fix when difficulty is implemented
			write.append("Difficulty:" + Board.difficultyLevel + "\n");
			
			write.append("Treasure Remaining:" + treasure + "\n");
			
			write.append("UI type:" + UI + "\n");

			for (int i = 0; i < field.length; i++) {

				write.append(field[i].saveBoardValue() + ",");
				
				if ((i + 1) % boardWidth == 0) { // New line
					write.append("\n");
				}
			}

			// Reduce chances of corrupting data while saving
			write.flush();
			write.close(); 
			
		} catch (IOException e) {

			System.err.println("Utilities.saveBoard(ArrayList<Integer> cellValues, String saveName, int boardWidth): Error when attempting to save the board: " + e.getMessage());
		}
	}

	/**
	 * This prints the board in the terminal at the start of each new game.
	 * 
	 * @param cellValues	Cell values from the board
	 * @param boardWidth	N_COLS from Board class
	 * @param boardHeight	N_ROWS from Board class
	 */
	public static void printBoard(ArrayList<Integer> cellValues, int boardWidth, int boardHeight) {

		System.out.println("Utilities.printBoard(ArrayList<Integer> cellValues, int boardWidth, int boardHeight): Board size: " + boardWidth + " x " + boardHeight);
		System.out.println("Board values:");

		// Iterate through the 1D array list and print each value
        for (int i = 0; i < cellValues.size(); i++) {

            System.out.print(cellValues.get(i) + " ");
			
            if ((i + 1) % boardWidth == 0) { // New line

                System.out.println();
            }
        }

		System.out.println("Utilities.printBoard(ArrayList<Integer> cellValues, int boardWidth, int boardHeight): Board printed succesfully.");
	}

	/**
	 * 
	 */
	// TODO Implement function
	public static ArrayList<Integer> loadBoard(String loadSaveFileName) {

		System.out.println("Utilities.loadBoard(): Loading board...");
		
		try {
			
			ArrayList<String> saveFileNames = getSaveFileNames();

			for (String saveFileName : saveFileNames) {

				if (loadSaveFileName.equals(saveFileName)) {
					
					System.out.println("Utilities.loadBoard(): File found.");
					File file = new File("src\\saves\\" + loadSaveFileName + "-save.csv");
					Scanner scanner = new Scanner(file);
					int treasureRemaining = 0;
					int UI = 0;

		            // Skip the first line (Board Name)
		            if (scanner.hasNextLine()) {
		                scanner.nextLine();
		            }
		            // TODO this skips the difficulty level
		            int difficulty = 0;
		            if (scanner.hasNextLine()) {
		            	String difficultyLevel = scanner.nextLine();
		            	int index = difficultyLevel.indexOf(':');
		            	difficulty = Integer.parseInt(difficultyLevel.substring(index + 1));
		            }
		            if (scanner.hasNextLine()) {
		            	String treasure = scanner.nextLine();
		            	int index = treasure.indexOf(':');
		            	treasureRemaining = Integer.parseInt(treasure.substring(index + 1));
		            }
		            if (scanner.hasNextLine()) {
		            	String UIstr = scanner.nextLine();
		            	int index = UIstr.indexOf(':');
		            	UI = Integer.parseInt(UIstr.substring(index + 1));
		            }
		            int numCols = 0;
		            ArrayList<Integer> boardData = new ArrayList<>();
		            while (scanner.hasNextLine()) {
		                String line = scanner.nextLine().trim();
		                if (line.isEmpty()) continue; // Skip empty lines

		                // Remove trailing comma
		                if (line.endsWith(",")) {
		                    line = line.substring(0, line.length() - 1);
		                }

		                // Split by commas
		                String[] tokens = line.split(",");
		                
		                numCols = tokens.length;

		                for (String token : tokens) {
		                    boardData.add(Integer.parseInt(token.trim()));
		                }
		            }
		            boardData.add(difficulty);
		            boardData.add(treasureRemaining);
		            boardData.add(UI);
		            scanner.close();
		            return boardData;
				}
			}

			// TODO change this back to IOException
		} catch (Exception e) {

			System.err.println("Utilities.loadBoard(): Error when attempting to load the board. " + e.getMessage());
		}

		System.out.println("Utilities.loadBoard(): Board loaded.");
		return null;
	}
	
	
	
	public static ArrayList<Integer> loadTestCSV(String filename) {
		ArrayList<Integer> board = new ArrayList<>();

		try (Scanner scanner = new Scanner(new File(filename))) {
			int row = 0;
			while (scanner.hasNextLine() && row < 8) {
				String[] tokens = scanner.nextLine().split(",");
				if (tokens.length != 8)
					return null;
				for (int col = 0; col < 8; col++) {
					int val = Integer.parseInt(tokens[col].trim());
					if (val != 0 && val != 1 && val != 2) {
						return null;
					}
						
					board.add(val);
				}
				row++;
			}
			if (row != 8)
				return null;
		} catch (Exception e) {
			return null;
		}

		return board;
	}
	
	
	  public static boolean isValidTestBoard(List<Integer> board) {
	        if (board == null || board.size() != 64) return false;

	        List<int[]> mines = new ArrayList<>();
	        List<int[]> treasures = new ArrayList<>();

	        // Collect positions of mines and treasures
	        for (int r = 0; r < 8; r++) {
	            for (int c = 0; c < 8; c++) {
	                int val = board.get(r * 8 + c);
	                if (val == 1) mines.add(new int[]{r, c});
	                else if (val == 2) treasures.add(new int[]{r, c});
	            }
	        }

	        if (mines.size() != 10 || treasures.size() > 9) return false;

	        // Try all combinations of 8 mines out of 10 to find a valid distinct-row-and-col non-adjacent set
	        for (int i = 0; i < 10; i++) {
	            for (int j = i + 1; j < 10; j++) {
	                List<int[]> first8 = new ArrayList<>();
	                for (int k = 0; k < 10; k++) {
	                    if (k != i && k != j) first8.add(mines.get(k));
	                }
	                
	                if (!hasUniqueRowsAndCols(first8)) continue;
	                if (!areFirst8NonAdjacent(first8)) continue;
	                if (!containsDiagonal(first8)) continue;

	                int[] ninth = mines.get(i);
	                int[] tenth = mines.get(j);

	                if (!isAdjacentToAny(ninth, first8)) continue;
	                if (!isIsolatedFromAll(tenth, first8, ninth)) continue;

	                return true; // All conditions met
	            }
	        }

	        return false;
	    }

	    private static boolean hasUniqueRowsAndCols(List<int[]> positions) {
	        Set<Integer> rows = new HashSet<>();
	        Set<Integer> cols = new HashSet<>();
	        for (int[] pos : positions) {
	            if (!rows.add(pos[0]) || !cols.add(pos[1])) return false;
	        }
	        return true;
	    }

	    private static boolean areFirst8NonAdjacent(List<int[]> positions) {
	        for (int i = 0; i < positions.size(); i++) {
	            for (int j = i + 1; j < positions.size(); j++) {
	                int[] a = positions.get(i), b = positions.get(j);
	                if (a[0] == b[0] || a[1] == b[1]) return false; // same row or column
	            }
	        }
	        return true;
	    }

	    private static boolean containsDiagonal(List<int[]> positions) {
	        for (int[] pos : positions) {
	            if (pos[0] == pos[1]) return true;
	        }
	        return false;
	    }

	    private static boolean isAdjacentToAny(int[] target, List<int[]> others) {
	        for (int[] m : others) {
	            if (m[0] == target[0] || m[1] == target[1]) return true;
	        }
	        return false;
	    }

	    private static boolean isIsolatedFromAll(int[] target, List<int[]> others, int[] alsoCheck) {
	        for (int[] m : others) {
	            if (isTouching(target, m)) return false;
	        }
	        return !isTouching(target, alsoCheck);
	    }

	    private static boolean isTouching(int[] a, int[] b) {
	        return Math.abs(a[0] - b[0]) <= 1 && Math.abs(a[1] - b[1]) <= 1 && !(a[0] == b[0] && a[1] == b[1]);
	    }


	
	
}
