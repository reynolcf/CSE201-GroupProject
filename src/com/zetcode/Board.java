/*
 * Changelog:
 * 1. added private final int DRAW_TREASURE = 13;
 * 2. added Cell class functions to replace old calculations that use the 'Constants for cell states' variables
 * 3. added treasure and generation of treasure
 * 4. added new status bar that tracks the number of treasure collected
 * 5. added commented sections denoting new global variables and functions
 * 6. added comment bars to separate the methods
 * 
 * Explanation:
 * 1. Added constant for the hidden treasure to be drawn on the board
 * 2. The old calculations with those constants were horrible, the Cell class makes it much easier to
 *    manage everything
 * 3. We needed to add treasure for the project, currently it works, in the console where a grid of the
 *    cells show up, the 1 means mine, 2 means treasure, and 0 is everything else. This grid in the console
 *    is currently the only way to tell which cells are treasure
 * 4. Allows the user to see how much treasure they've collected
 * 5. A lot of the code is repetitive, so the helper methods make it easier to read, and separating it from
 *    the code we were given helps figure out what was changed. The new stuff is above the rest of it's kind.
 *    For example, the new global variables are just above the rest of the global variables.
 * 6. Makes code easier to read
 */

package com.zetcode;

// Import necessary classes
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Board extends JPanel {
	
	//Newly added variables
	private int collectedTreasure;
	private final int NUM_TREASURE = 20; // Number of treasure to be generated, must be 'less than number of mines'
	private final JLabel statusbar2;
	public static Cell[] field = null;    // Stores all the cells in the game
	
	
	
/*********************************************************************************************************************/
	
	

    // Constants for the game settings
    private final int NUM_IMAGES = 13;
    private final int CELL_SIZE = 15;

    // Constants for the cell states
   /** private final int COVER_FOR_CELL = 10;
    private final int MARK_FOR_CELL = 10;
    private final int EMPTY_CELL = 0;
    private final int MINE_CELL = 9;
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL; */

    // Constants for drawing states
    private final int DRAW_MINE = 9;
    private final int DRAW_COVER = 10;
    private final int DRAW_MARK = 11;
    private final int DRAW_WRONG_MARK = 12;
    private final int DRAW_TREASURE = 13;

    // Game mines couunt
    private final int N_MINES = 40;
    public static int N_ROWS = 16;
    public static int N_COLS = 16;

    // Game board size
    public final int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;
    public final int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;

    // Game state variables
    
    private boolean inGame; // Tracks if the game is ongoing
    private int minesLeft;  // Number of mines left to be marked
    private Image[] img;    // Stores images for different cell states

    private int allCells;   // Total number of cells on the board
    private final JLabel statusbar; // Label to display game status
    

    public static ArrayList<Integer> cellValues; // Store the locations of the mines and hidden treasure
    
/*********************************************************************************************************************/
    
    // New helper methods
    
    /**
     * This method is run when the user left clicks on a mine, and checks if there
     * is enough treasure to save them, then processes accordingly
     * 
     */
    public void checkTreasureAmount() {
    	if (collectedTreasure > 0) {
    		collectedTreasure -= 2;
    		minesLeft--;
    		statusbar.setText("Mines remaining: " + Integer.toString(minesLeft));
    	} else {
    		inGame = false;
    	}
    }
    
    
    /**
     * Generates the mines and the treasure on the game board. Also, it tracks how many mines are adjacent
     * to each cell
     */
    public void generateBoard() {
    	// Array list to track the cells that are mines
        cellValues = new ArrayList<>();
    	
    	allCells = N_ROWS * N_COLS;
    	
    	if (field != null) {
    		for (Cell cell : field) {
    			if (cell.isMine()) {
    				cellValues.add(1);
    			} else if (cell.isTreasure()) {
    				cellValues.add(2);
    			} else {
    				cellValues.add(0);
    			}
    		}
    		initializeTrackers();
    	} else {
    		int cell;
            field = new Cell[allCells];
            var random = new Random();

            for (int i = 0; i < allCells; i++) {

                field[i] = new Cell();
                cellValues.add(0); // Fill with list with empty cells
            }
            

            int i = 0;
            
            // Generates the mines, and updates the numAdjacentMines of all 8 surrounding cells
            while (i < N_MINES) {

                int position = (int) (allCells * random.nextDouble());

                if ((position < allCells)
                        && (field[position].isMine() == false)) {

                    int current_col = position % N_COLS;
                    field[position].setIsMine(true);
                	field[position].setIsEmptyCell(false);
                    
                    
                    cellValues.set(position, 1); // Update to mine
                    i++;

                    if (current_col > 0) {
                        cell = position - 1 - N_COLS;
                        if (cell >= 0) {
                        	trackNumAdjacentMines(cell);
                        }
                        cell = position - 1;
                        if (cell >= 0) {
                        	trackNumAdjacentMines(cell);
                        }

                        cell = position + N_COLS - 1;
                        if (cell < allCells) {
                        	trackNumAdjacentMines(cell);
                        }
                    }

                    cell = position - N_COLS;
                    if (cell >= 0) {
                    	trackNumAdjacentMines(cell);
                    }

                    cell = position + N_COLS;
                    if (cell < allCells) {
                    	trackNumAdjacentMines(cell);
                    }

                    if (current_col < (N_COLS - 1)) {
                        cell = position - N_COLS + 1;
                        if (cell >= 0) {
                        	trackNumAdjacentMines(cell);
                        }
                        cell = position + N_COLS + 1;
                        if (cell < allCells) {
                        	trackNumAdjacentMines(cell);
                        }
                        cell = position + 1;
                        if (cell < allCells) {
                            trackNumAdjacentMines(cell);
                        }
                    }
                }
            }
            
            // New loop to add the mines to the field
            i = 0;
            while (i < NUM_TREASURE) {
            	int position = (int) (allCells * random.nextDouble());
            	if (field[position].isMine() == false) {
            		field[position].setIsTreasure(true);
            		cellValues.set(position, 2);
            		i++;
            	}
            }
    	}
    }
    
    
    /**
     * Directly manages the numAdjacentMines variable for each cell
     * 
     * @param cell The index of the cell being considered
     */
    public void trackNumAdjacentMines(int cell) {
    	if (field[cell].isMine() == false) {
        	field[cell].setIsEmptyCell(false);
        	field[cell].addOneToNumber();
        }
    }
    
    
    /**
     * This method is called in the find_empty_cells method, and it checks if the find_empty_cells method
     * needs to be called recursively as well as uncovers the cell, and helps keep track of treasure
     * 
     * @param cell The index of the cell being considered
     */
    public void checkForEmptyAdjacents(int cell) {
    	if (field[cell].isCovered()) {
            field[cell].setIsCovered(false);
            if (field[cell].isEmptyCell()) {
                find_empty_cells(cell);
            } else if (field[cell].isTreasure()) {
            	collectedTreasure++;
            }
        }
    }
    
    
    
    
    public void initializeTrackers() {
    	for (Cell cell : field) {
    		if ((cell.isMine() && !cell.isCovered()) || cell.isFlagged()) {
    			minesLeft--;
    		}
    		if (cell.isTreasure() && !cell.isCovered()) {
    			collectedTreasure++;
    		}
    	}
    	
    }
    
    
    
    
/*********************************************************************************************************************/
    
    

    /**
     * Constructor initializes the board with status bar.
     * 
     * @param statusbar     This is being passed to this class so
     *                      that it can be changed to show the number
     *                      of flags that are left during the game.
     */
    public Board(JLabel statusbar, JLabel statusbar2, ArrayList<Integer> save) {

        this.statusbar = statusbar;
        this.statusbar2 = statusbar2;
        if (save != null) {
        	N_COLS = save.get(save.size() - 1);
        	N_ROWS = (save.size() - 1) / N_COLS;
        	field = new Cell[save.size() - 1];
        	for (int i = 0; i < save.size() - 1; i++) {
        		Cell cell = new Cell(save.get(i));
        		field[i] = cell;
        	}
        }
        
        initBoard(); 
    }

    
/*********************************************************************************************************************/
    
    /**
     * This initializes the board. It will set the preffered dimensions
     * for the JPanel, initialize the img[] array to store the images,
     * then load the images from the resources folder. After that a
     * mouse listener will be added, followed up by launching a new
     * game. 
     */
    private void initBoard() {

        System.out.println("Board.initBoard(): Initializing board...");

        // Sets the prefered size of the JPanel (unless otherwise specified)
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        img = new Image[NUM_IMAGES];

        // Load images for the game
        for (int i = 0; i < NUM_IMAGES; i++) {

            var path = "src/resources/" + i + ".png";
            img[i] = (new ImageIcon(path)).getImage();
        }

        System.out.println("Board.initBoard(): Board initialized.");

        addMouseListener(new MinesAdapter());
        newGame();
    }
    
    
    
/*********************************************************************************************************************/

    /**
     * 
     */
    // TODO Implement the hidden treasure feature
    private void newGame() {

        System.out.println("Board.newGame(): New game starting...");


        
        inGame = true;
        minesLeft = N_MINES;
        collectedTreasure = 0;
        
        

        
        generateBoard();
        
        statusbar.setText("Mines remaining: " + Integer.toString(minesLeft));
        statusbar2.setText("Treasure collected: " + Integer.toString(collectedTreasure));
        
        
        System.out.println("Board.newGame(): New game started.");
        Utilities.printBoard(cellValues, N_COLS, N_ROWS); // Debugging function
    }
    
    
    
/*********************************************************************************************************************/
    

    /**
     * Note: This method happens when you click on one of those big empty spaces, and all the surrounding
     *       empty spaces are also revealed along with the bordering non-mines
     * 
     * This checks all surrounding cells
     * 
     * Recursive method; used when clicking to reveal a cell. This will
     * check the adjacent spaces to see if a "branching" effect
     * will happen or not.
     * 
     * @param j     Recursive variable, note that j is the square clicked
     */
    private void find_empty_cells(int j) {

        System.out.println("Board.find_empty_cells(int j): Finding empty cells...");
        if (field[j].isTreasure()) {
        	collectedTreasure++;
        }

        int current_col = j % N_COLS;
        int cell;

        if (current_col > 0) {
            cell = j - N_COLS - 1;
            if (cell >= 0) {
                checkForEmptyAdjacents(cell);
            }

            cell = j - 1;
            if (cell >= 0) {
            	checkForEmptyAdjacents(cell);
            }

            cell = j + N_COLS - 1;
            if (cell < allCells) {
            	checkForEmptyAdjacents(cell);
            }
        }

        cell = j - N_COLS;
        if (cell >= 0) {
        	checkForEmptyAdjacents(cell);
        }

        cell = j + N_COLS;
        if (cell < allCells) {
        	checkForEmptyAdjacents(cell);
        }

        if (current_col < (N_COLS - 1)) {
            cell = j - N_COLS + 1;
            if (cell >= 0) {
            	checkForEmptyAdjacents(cell);
            }

            cell = j + N_COLS + 1;
            if (cell < allCells) {
            	checkForEmptyAdjacents(cell);
            }

            cell = j + 1;
            if (cell < allCells) {
            	checkForEmptyAdjacents(cell);
            }
        }
        System.out.println("Board.find_empty_cells(int j): Empty cells found.");
    }
    
    
    
/*********************************************************************************************************************/

    /**
     * IMPORTANT: This method is called every time repaint() is called
     */
    @Override
    public void paintComponent(Graphics g) {

        System.out.println("Board.paintComponent(Graphics g): Repainting the cells...");

        int uncover = 0;

        for (int i = 0; i < N_ROWS; i++) {

            for (int j = 0; j < N_COLS; j++) {

                
                int index = (i * N_COLS) + j;
                int cell = field[index].number();

               /** if (inGame && field[index].isMine() && !field[index].isCovered()) {

                    inGame = false;
                } */

                if (!inGame) {

                    if (field[index].isMine()) {
                        cell = DRAW_MINE;
                    } else if (field[index].isFlagged() && field[index].isMine()) {
                        cell = DRAW_MARK;
                    } else if (!field[index].isMine() && field[index].isFlagged()) {
                        cell = DRAW_WRONG_MARK;
                    } else if (field[index].isCovered()) { //if (cell > MINE_CELL)
                        cell = DRAW_COVER;
                    }
                } else {

                    if (field[index].isFlagged()) {
                        cell = DRAW_MARK;
                    } else if (field[index].isCovered()) {
                        cell = DRAW_COVER;
                        uncover++;
                    } else if (field[index].isMine() && !field[index].isCovered()) {
                    	cell = DRAW_MINE;
                    }
                }

                
                g.drawImage(img[cell], (j * CELL_SIZE),
                        (i * CELL_SIZE), this);
            }
        }

        if (uncover == 0 && inGame) {

            inGame = false;
            statusbar.setText("Game won");
            statusbar2.setText("");

        } else if (!inGame) {
            statusbar.setText("Game lost");
            statusbar2.setText("");
        }

        System.out.println("Board.paintComponent(Graphics g): Cells repainted.");
    }

    
/*********************************************************************************************************************/
    
    
    // Note: BUTTON3 is right mouse button, which flags cells
    private class MinesAdapter extends MouseAdapter {

        /**
         * 
         */
        @Override
        public void mousePressed(MouseEvent e) {

            System.out.println("Board.mousePressed(MouseEvent e): Checking if mouse pressed...");

            int x = e.getX();
            int y = e.getY();

            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;
            
            int index = (cRow * N_COLS) + cCol;

            boolean doRepaint = false;

            if (!inGame) {

                newGame();
                repaint();
            }

            if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {

                if (e.getButton() == MouseEvent.BUTTON3) { // THIS IS THE RIGHT CLICK, FOR ADDING FLAG

                    if (field[index].isCovered()) {

                        doRepaint = true;

                        if (!(field[index].isFlagged())) { // Add flag

                            if (minesLeft > 0) {
                                field[index].setIsFlagged(true);
                                minesLeft--;
                                statusbar.setText("Mines remaining: " + Integer.toString(minesLeft));
                            } else {
                                statusbar.setText("No marks left");
                            }
                        } else { // Remove flag

                            field[index].setIsFlagged(false);
                            minesLeft++;
                            String msg = Integer.toString(minesLeft);
                            statusbar.setText("Mines remaining" + msg);
                        }
                    }

                } else { // THIS IS THE LEFT CLICK, FOR UNCOVERING

                    if (field[index].isCoveredAndFlaggedMine()) {

                        return;
                    }

                    if ((field[index].isCovered())
                            && (!field[index].isCoveredAndFlaggedMine())) {

                        field[index].setIsCovered(false);
                        doRepaint = true;

                        if (field[index].isMine()) {
                            checkTreasureAmount();
                        }

                        if (field[index].isEmptyCell()) {
                            find_empty_cells(index);
                        } else if (field[index].isTreasure()) {
                        	collectedTreasure++;
                        }
                        
                        statusbar2.setText("Treasure collected: " + Integer.toString(collectedTreasure));
                        
                    }
                }

                if (doRepaint) {
                    repaint();
                }
            }

            System.out.println("Board.mousePressed(MouseEvent e): Mouse check completed.");
        }
    }
}
