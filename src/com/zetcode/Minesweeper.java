/**
 * Java Minesweeper Game
 *
 * Author: Jan Bodnar
 * CO Authors: Nick Pieroni, Cameron Reynolds, Carlito Wheeler, and Owen Walpole
 * 
 * Website: http://zetcode.com
*/

/*
 * Changelog:
 * 1. Added private void initMainMenu()
 * 2. Added saves folder
 * 3. Added Utilities.java
 * 4. Added a second status bar named 'statusbar2', this is to display the amount
 *    of treasure collected
 * 5. Fixed the Load Menu back button
 * 6. Added function to the save and load function, STILL NOT FULLY FUNCTIONAL
 * 
 * Explanation:
 * 1. This will allow us to create a main menu rather than launching
 *      straight into the game
 * 2. This will house all the save .csv files so that users can save
 *      and load into that specific board
 * 3. This new file houses some of the utilites that allow the program
 *      to pull data from the computer
 * 6. This allows the user to save their game and load back into it.
 *      Please note that currently, the saves don't work if the game
 *      is closed and reopened, it only works in the session it is
 *      saved in. Also, currently if you save the game, the treasure
 *      count doesn't update correctly based on how much treasure
 *      the user spent before saving. Also, if you save a game, make
 *      more progress in the same game, and then save again, even with
 *      a different name, it overwrites the old save with this new save.
*/

package com.zetcode;

// Import necessary classes
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Minesweeper extends JFrame {

    // Constants for window dimensions
    // TODO Use the ones from the board?
    private final static int WINDOW_WIDTH = 255;
    private final static int WINDOW_HEIGHT = 400;
    
    private JLabel statusbar;
    private JLabel statusbar2;

    // Created globally to allow layered frames to work correctly
    JPanel pausePanel;
    JPanel levelPanel; 

    /**
     * Default constructor for the minesweeper class
    */
    public Minesweeper() {

        initFrame(); // initialize the JFrame (window)
    }

    /**
     * This method will initialize the JFrame (window) for the minesweeper game to show.
     */
    private void initFrame() {

        System.out.println("Minesweeper.initFrame(): initializing JFrame...");

        // Frame settings (window)
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)); // Set frame dimensions
        setResizable(false);
        
        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        getContentPane().setLayout(new BorderLayout());
        
		pack();
		
		
        System.out.println("Minesweeper.initFrame(): JFrame initialized.");

        // Launch main menu after initialization
        mainMenu();
    }
    
    /**
     * This will initialize a new JPanel to house the functions of 
     * the main menu, such as loading previous games, starting new games, 
     * or switching to test mode.
     * 
     * @implNote    The way the action listeners are set up is correct, there
     *              is no way to listen for more than one button at a time
     *              without using a map.
     * 
     * @impliNote   Since panel is set at the bottom of the frame, we use the
     *              createVerticalStrut to create extra space so that the label
     *              is at the correct position; near the top of the frame.
    */
    public void mainMenu() {

        System.out.println("Minesweeper.mainMenu(): Launching main menu...");

        // Create components
        statusbar = new JLabel("Main Menu");
        JButton newGameButton = new JButton("New Game");
        JButton loadGameButton = new JButton("Load Game");
        JButton testGameButton = new JButton("Test Game");
        JButton exitGameButton = new JButton("Exit Game");

        // Create panel to store buttons
        JPanel menuPanel = new JPanel();
        menuPanel.setVisible(true);

        // Adjust button layout
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS)); // Align buttons vertically

        // Align buttons in center of frame
        statusbar.setAlignmentX(CENTER_ALIGNMENT);
        newGameButton.setAlignmentX(CENTER_ALIGNMENT);
        loadGameButton.setAlignmentX(CENTER_ALIGNMENT);
        testGameButton.setAlignmentX(CENTER_ALIGNMENT);
        exitGameButton.setAlignmentX(CENTER_ALIGNMENT);

        // Add buttons to menuPanel
        menuPanel.add(statusbar);
        menuPanel.add(Box.createVerticalStrut(185));
        menuPanel.add(newGameButton);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(loadGameButton);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(testGameButton);
        menuPanel.add(Box.createVerticalStrut(15));
        menuPanel.add(exitGameButton);
        menuPanel.add(Box.createVerticalStrut(15));

        getContentPane().add(menuPanel, BorderLayout.SOUTH); // Add menuPanel to the bottom of the frame
        
        revalidate();
        repaint();

        System.out.println("Minesweeper.mainMenu(): Main menu launched.");

        // Button actions
        newGameButton.addActionListener((ActionEvent e) -> { // newGameButton action

            menuPanel.setVisible(false); // Make panel invisible
            //initUI(null);
            levelSelectionMenu();
        });

        loadGameButton.addActionListener((ActionEvent e) -> { // loadGameButton action

            menuPanel.setVisible(false);
            loadMenu();
        });

        testGameButton.addActionListener((ActionEvent e) -> { // testGameButton action

            menuPanel.setVisible(false);
            testGameMenu();
        });

        exitGameButton.addActionListener((ActionEvent e) -> { // exitGameButton action

            System.out.println("Minesweeper.mainMenu(): Exiting the program...");
            System.exit(0); // Terminate the program
        });
    }

    /**
     * This will initialize a new JPanel to house the functions of 
     * the save menu. Such as clicking on a save to play it, or 
     * pressing the back button to return to the menu.
    */
    // TODO create proportional elements
    // TODO if no files are found set the status bar to say so.
    private void saveMenu() {

        System.out.println("Minesweeper.saveMenu(): Launching save menu...");

        // Create panel to store gui elements
        JPanel savePanel = new JPanel();
        savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.Y_AXIS)); // Makes buttons stack vertically

        // Initialize gui elements
        JLabel pauseText = new JLabel("Save game"); // Display pause text at top
        JTextField fileNameInput = new JTextField(); // User inout for save file name
        JButton saveButton = new JButton("Save"); // Save button to save the current board
        JButton cancelButton = new JButton("Cancel"); // Cancel button to return to the pause menu

        // Set maximum dimensions to prevent excessively tall text field
        fileNameInput.setMaximumSize(new Dimension(WINDOW_WIDTH - 20, 30));
        fileNameInput.setPreferredSize(new Dimension(WINDOW_WIDTH - 20, 30));

        // Align buttons to center
        saveButton.setAlignmentX(CENTER_ALIGNMENT);
        cancelButton.setAlignmentX(CENTER_ALIGNMENT);

        // Add elements to panel
        savePanel.add(Box.createVerticalStrut(15)); // Add spacing between buttons vertically
        savePanel.add(pauseText);
        savePanel.add(Box.createVerticalStrut(5));
        savePanel.add(fileNameInput);
        savePanel.add(Box.createVerticalStrut(5)); 
        savePanel.add(saveButton);
        savePanel.add(Box.createVerticalStrut(5));
        savePanel.add(cancelButton);

        savePanel.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT); // Adjust panel dimensions

        // Add panel to layered frame
        getLayeredPane().add(savePanel, JLayeredPane.POPUP_LAYER);

        savePanel.setVisible(true); // Ensure panel is visible

        System.out.println("Minesweeper.saveMenu(): Save menu launched.");
    
        // Button actions
        cancelButton.addActionListener((ActionEvent e) -> { // cancelButton action

            savePanel.setVisible(false);
        });

        saveButton.addActionListener((ActionEvent e) -> { // saveButton action

            String saveFileName = fileNameInput.getText();
            Utilities.saveBoard(Board.field, saveFileName, Board.N_COLS, Board.collectedTreasure);
            savePanel.setVisible(false); // Remove savePanel after saving
        });        
    }
    
    /**
     * 
     */
    private void levelSelectionMenu() {

        // Create a new panel to store gui elements
        levelPanel = new JPanel();
        levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.Y_AXIS)); // Makes buttons stack vertically

        // Initialize gui elements
        JLabel levelText = new JLabel("Level Selection"); // Display pause text at top
        JLabel bsText = new JLabel("Board size: ");
        JButton beginnerButton = new JButton("Beginner"); // Resume button to return to game
        JButton intermediateButton = new JButton("Intermediate"); // Save button to save the current board
        JButton expertButton = new JButton("Expert"); // Return button to return to the main menu
        JButton backButton = new JButton("< Back");

        // Align buttons to the center of the panel
        levelText.setAlignmentX(CENTER_ALIGNMENT);
        bsText.setAlignmentX(CENTER_ALIGNMENT);
        beginnerButton.setAlignmentX(CENTER_ALIGNMENT);
        intermediateButton.setAlignmentX(CENTER_ALIGNMENT);
        expertButton.setAlignmentX(CENTER_ALIGNMENT);
        backButton.setAlignmentX(CENTER_ALIGNMENT);

        // Add elements to panel
        levelPanel.add(Box.createVerticalStrut(15)); // Add spacing between buttons vertically
        levelPanel.add(levelText);
        levelPanel.add(bsText);
        levelPanel.add(Box.createVerticalStrut(185));
        levelPanel.add(Box.createVerticalStrut(5));
        levelPanel.add(beginnerButton);
        levelPanel.add(Box.createVerticalStrut(5));
        levelPanel.add(intermediateButton);
        levelPanel.add(Box.createVerticalStrut(5));
        levelPanel.add(expertButton);
        levelPanel.add(Box.createVerticalStrut(15));
        levelPanel.add(backButton);
        levelPanel.add(Box.createVerticalStrut(15));

        levelPanel.setBounds(0, 0, getWidth(), getHeight()); // Adjust panel dimensions

        // Add panel to layered frame
        getLayeredPane().add(levelPanel, JLayeredPane.POPUP_LAYER);

        levelPanel.setVisible(true); // Ensure panel is visible
    
        // Button actions
        beginnerButton.addActionListener((ActionEvent e) -> { // beginnerButton action

        	levelPanel.setVisible(false);
            Board.field = null;
            Board.cellValues = null;
            Board.collectedTreasure = 0;
            
            getContentPane().removeAll(); // Remove the all cell covers (Ie the board)

            // Refresh frame after removing board
            revalidate();
            repaint();

            UIMenu(1);
        });

        intermediateButton.addActionListener((ActionEvent e) -> { // intermediateButton action

        	levelPanel.setVisible(false);
            Board.field = null;
            Board.cellValues = null;
            Board.collectedTreasure = 0;
            
            getContentPane().removeAll();

            // Refresh frame after removing board
            revalidate();
            repaint();

            UIMenu(2);
        });

        expertButton.addActionListener((ActionEvent e) -> { // expertButton action

        	levelPanel.setVisible(false);
            Board.field = null;
            Board.cellValues = null;
            Board.collectedTreasure = 0;
            
            getContentPane().removeAll();

            // Refresh frame after removing board
            revalidate();
            repaint();

            UIMenu(3);
        });

        backButton.addActionListener((ActionEvent e) -> { // backButton action

        	levelPanel.setVisible(false);
            Board.field = null;
            Board.cellValues = null;
            Board.collectedTreasure = 0;
            
            getContentPane().removeAll(); // Remove the all cell covers (Ie the board)

            // Refresh frame after removing board
            revalidate();
            repaint();

            mainMenu(); 
        });
        
     // Make the numbers for board size change when mouse hover
        beginnerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                bsText.setText("Board size: 8 x 8    ");
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	bsText.setText("Board size:             ");
            }
        });
        
        intermediateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                bsText.setText("Board size: 16 x 16");
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	bsText.setText("Board size:             ");
            }
        });
        
        expertButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                bsText.setText("Board size: 30 x 16");
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	bsText.setText("Board size:             ");
            }
        });
    }
    
    private void UIMenu(int difficulty) {
        // TODO Implement test game functionality
        // Create panel to store components
        JPanel uiPanel = new JPanel();
        uiPanel.setLayout(new BoxLayout(uiPanel, BoxLayout.Y_AXIS));

        // Initialize gui components
        statusbar = new JLabel("Test Game");
        JButton guiButton = new JButton("GUI"); // Return button to return to the main menu
        JButton textButton = new JButton("Text Based");
        JButton backButton = new JButton("Back");


        // Align componenets in the center
        statusbar.setAlignmentX(CENTER_ALIGNMENT);
        guiButton.setAlignmentX(CENTER_ALIGNMENT);
        textButton.setAlignmentX(CENTER_ALIGNMENT);
        backButton.setAlignmentX(CENTER_ALIGNMENT);

        // Add components to panel
        uiPanel.add(Box.createVerticalStrut(15)); // Add spacing between components vertically
        uiPanel.add(statusbar);
        uiPanel.add(Box.createVerticalStrut(35)); // Add spacing between components vertically
        uiPanel.add(guiButton);
        uiPanel.add(Box.createVerticalStrut(15)); // Add spacing between components vertically
        uiPanel.add(textButton);
        uiPanel.add(Box.createVerticalStrut(25)); // Add spacing between components vertically
        uiPanel.add(backButton);

        uiPanel.setBounds(0, 0, getWidth(), getHeight()); // Adjust panel dimensions

        // Add panel to layered frame
        getLayeredPane().add(uiPanel, JLayeredPane.POPUP_LAYER);

        uiPanel.setVisible(true); // Ensure panel is visible
        
     // Button actions
        guiButton.addActionListener((ActionEvent e) -> { 

        	uiPanel.setVisible(false);
            
            getContentPane().removeAll(); 

            // Refresh frame after removing board
            revalidate();
            repaint();

            initUI(null, difficulty, 1);
        });

        textButton.addActionListener((ActionEvent e) -> { 

        	uiPanel.setVisible(false);
            
            getContentPane().removeAll();

            // Refresh frame after removing board
            revalidate();
            repaint();

            initUI(null, difficulty, 2);
        });

        backButton.addActionListener((ActionEvent e) -> { 

        	uiPanel.setVisible(false);
            
            getContentPane().removeAll(); 

            // Refresh frame after removing board
            revalidate();
            repaint();

            levelSelectionMenu(); 
        });

    }
    
    /**
     * This will initialize a new JPanel to house the functions of 
     * the load menu. Such as the buttons for each save file, 
     * the scrolling pane and the back button.
    */
    // TODO Make the buttons not exceed the length of the window
    // TODO Remove the sideways scroll bar
    private void loadMenu() {

        System.out.println("Minesweeper.loadMenu(): Launching load menu...");
        getContentPane().removeAll();

        // Create panel to store the buttons
        JPanel saveFilePanel = new JPanel();
        saveFilePanel.setLayout(new BoxLayout(saveFilePanel, BoxLayout.Y_AXIS)); // Makes buttons stack vertically
    
        // Store save file names in an ArrayList to be iterated through later
        ArrayList<String> saveFileNames = Utilities.getSaveFileNames();

        // Create buttons
        for (String fileName : saveFileNames) {

            JButton saveFileButton = new JButton(fileName); // Create new button with new name

            // Set dimensions of the buttons to the same, regardless of button name length
            saveFileButton.setMinimumSize(new Dimension(WINDOW_WIDTH, 30));
            saveFileButton.setPreferredSize(new Dimension(WINDOW_WIDTH, 30));
            saveFileButton.setMaximumSize(new Dimension(WINDOW_WIDTH, 30));
            
            saveFileButton.addActionListener((ActionEvent e) -> {
                System.out.println("Loading save file: " + fileName);
                ArrayList<Integer> save = Utilities.loadBoard(fileName);
                initUI(save, 0, 1);
            });
            
            saveFilePanel.add(saveFileButton); // Add button to panel
            saveFilePanel.add(Box.createVerticalStrut(5)); // Add spacing between buttons vertically
        }

        // Back button to return to main menu
        JButton backButton = new JButton("< Back");
        saveFilePanel.add(backButton);
    
        // Wrap panel in scrolling frame
        JScrollPane scrollPane = new JScrollPane(saveFilePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)); 

        // Add scroll panel to frame
        getContentPane().add(scrollPane);

        // Button actions
        backButton.addActionListener((ActionEvent e) -> { // backButton action

            // TODO Fix back button
            // TODO Check if removing the components is better or worse than setting the visibility to false
            // FIXED THE BACK BUTTON, the issue was that in the main menu method, repaint and revalidate
        	// was never called
        	
        	getContentPane().removeAll();

            // Refresh JFrame after removing componenets
            revalidate();
            repaint();

            mainMenu();
        });

        System.out.println("Minesweeper.loadMenu(): Load menu launched.");
    }

    /**
     * This will initialize a new JPanel to house the functions of
     * the pause menu. Such as holding the status bar and changing
     * the status of the bar to "Game Paused", as well as the 
     * "Resume" button, "Save" button, "Return to Main Menu" button and
     * the "Exit Game" button.
     * 
     * @implNote    Do not clear elements of pane at beginning of method.
     *              The pause menu should act as a layer over the
     *              previous panel, not as a replacement layer.
    */
    // TODO MAKE IT SO YOU CANNOT PLAY THE GAME THRU THIS WINDOW (HINT CLICK THE WHITESPACE)
    private void pauseMenu() {

        System.out.println("Minesweeper.pauseMenu(): Launching pause menu...");

        // Create a new panel to store gui elements
        pausePanel = new JPanel();
        pausePanel.setLayout(new BoxLayout(pausePanel, BoxLayout.Y_AXIS)); // Makes buttons stack vertically

        // Initialize gui elements
        JLabel pauseText = new JLabel("Game Paused"); // Display pause text at top
        JButton resumeButton = new JButton("Resume"); // Resume button to return to game
        JButton saveButton = new JButton("Save"); // Save button to save the current board
        JButton returnButton = new JButton("Return to main menu"); // Return button to return to the main menu
        JButton exitButton = new JButton("Exit Game");

        // Align buttons to the center of the panel
        pauseText.setAlignmentX(CENTER_ALIGNMENT);
        resumeButton.setAlignmentX(CENTER_ALIGNMENT);
        saveButton.setAlignmentX(CENTER_ALIGNMENT);
        returnButton.setAlignmentX(CENTER_ALIGNMENT);
        exitButton.setAlignmentX(CENTER_ALIGNMENT);

        // Add elements to panel
        pausePanel.add(Box.createVerticalStrut(15)); // Add spacing between buttons vertically
        pausePanel.add(pauseText);
        pausePanel.add(Box.createVerticalStrut(5));
        pausePanel.add(resumeButton);
        pausePanel.add(Box.createVerticalStrut(5));
        pausePanel.add(saveButton);
        pausePanel.add(Box.createVerticalStrut(5));
        pausePanel.add(returnButton);
        pausePanel.add(Box.createVerticalStrut(15));
        pausePanel.add(exitButton);
        pausePanel.add(Box.createVerticalStrut(15));

        pausePanel.setBounds(0, 0, getWidth(), getHeight()); // Adjust panel dimensions

        // Add panel to layered frame
        getLayeredPane().add(pausePanel, JLayeredPane.POPUP_LAYER);

        pausePanel.setVisible(true); // Ensure panel is visible
    
        // Button actions
        resumeButton.addActionListener((ActionEvent e) -> { // resumeButton action

            pausePanel.setVisible(false); // Remove panel from frame
        });

        saveButton.addActionListener((ActionEvent e) -> { // saveButton action

            pausePanel.setVisible(false); 
            saveMenu();
        });

        returnButton.addActionListener((ActionEvent e) -> { // returnButton action
        	this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            pausePanel.setVisible(false);
            Board.field = null;
            Board.cellValues = null;
            Board.collectedTreasure = 0;
            
            getContentPane().removeAll(); // Remove the all cell covers (Ie the board)

            // Refresh frame after removing board
            revalidate();
            repaint();

            mainMenu(); 
        });

        exitButton.addActionListener((ActionEvent e) -> { // exitButton action

            System.out.println("Minesweeper.pauseMenu(): Exiting program...");
            System.exit(0); // Terminate program
        });

        System.out.println("Minesweeper.pauseMenu(): Pause menu launched.");
    }

    /**
     * 
     */
    private void testGameMenu() {
        // TODO Implement test game functionality
        // Create panel to store components
        JPanel testPanel = new JPanel();
        testPanel.setLayout(new BoxLayout(testPanel, BoxLayout.Y_AXIS));

        // Initialize gui components
        statusbar = new JLabel("Test Game");

        // Align componenets in the center
        statusbar.setAlignmentX(CENTER_ALIGNMENT);

        // Add components to panel
        testPanel.add(Box.createVerticalStrut(15)); // Add spacing between components vertically
        testPanel.add(statusbar);

        // Add panel to the frame
        getContentPane().add(testPanel);
    }

    /**
     * This will initialize a new JPanel to house the functions of 
     * the of the board (and board elements), as well as the status bar,
     * and the "Pause" button.
     * 
     * @param UI 1 = GUI version, 2 = Text version
     * 
     * @implNote    Keep getContentPane().removeAll(); at the beginning of the 
     *              method. This is a special case where we want to remove
     *              everything from the content frame before creating.
    */
    private void initUI(ArrayList<Integer> save, int difficultyLevel, int UI) {

        System.out.println("Minesweeper.initUI(): Initializing UI...");

        getContentPane().removeAll(); // Remove all previous content from frame

        // Create panel to store components
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));

        // Initialize gui elements
        statusbar = new JLabel(""); // Displays flag count, game won, game lost
        statusbar2 = new JLabel("");
        JButton pauseButton = new JButton("Pause"); // Back button to return to main menu
        JLabel inputLabel = new JLabel("Enter X,Y:");
        JTextField coordinateInput = new JTextField();
        coordinateInput.setMaximumSize(new Dimension(80, 25));
        JButton selectMineButton = new JButton("Select");
        JButton flagButton = new JButton("Flag");


        // Align elements in the center
        statusbar.setAlignmentX(CENTER_ALIGNMENT);
        statusbar.setAlignmentX(CENTER_ALIGNMENT);
        pauseButton.setAlignmentX(CENTER_ALIGNMENT);
        inputLabel.setAlignmentX(CENTER_ALIGNMENT);
        coordinateInput.setAlignmentX(CENTER_ALIGNMENT);
        selectMineButton.setAlignmentX(CENTER_ALIGNMENT);
        flagButton.setAlignmentX(CENTER_ALIGNMENT);

        // Add elements to panel
        Board board = new Board(statusbar, statusbar2, save, difficultyLevel, this, UI);

        

        
        gamePanel.add(board);
        gamePanel.add(statusbar);
        gamePanel.add(Box.createVerticalStrut(10)); // Add spacing between buttons vertically
        gamePanel.add(statusbar2);
        gamePanel.add(Box.createVerticalStrut(20)); // Add spacing between buttons vertically
        if (UI == 2) {
        	gamePanel.add(inputLabel);
        	gamePanel.add(Box.createVerticalStrut(5));
        	gamePanel.add(coordinateInput);
        	gamePanel.add(Box.createVerticalStrut(5));
        	gamePanel.add(selectMineButton);
        	gamePanel.add(Box.createVerticalStrut(5)); // Add spacing between buttons vertically
        	gamePanel.add(flagButton);
        	gamePanel.add(Box.createVerticalStrut(5)); // Add spacing between buttons vertically
        }
        
        gamePanel.add(pauseButton);
        
        
        
        

        

        // Add panel to frame
        getContentPane().add(gamePanel);

        // Refresh frame after adding panel
        revalidate();
        repaint();
        
        // Button actions
        pauseButton.addActionListener((ActionEvent e) -> { // pauseButton action

            pauseMenu();
        });
        
        selectMineButton.addActionListener(e -> {
        	if (board.inGame) {
        		String input = coordinateInput.getText();
            
        		try {
                	String[] parts = input.split(",");
                	int x = Integer.parseInt(parts[0].trim());
                	int y = Integer.parseInt(parts[1].trim());
                	board.clickSimulator(x - 1, y - 1, 1); // <-- You must implement this method in your Board class
                	inputLabel.setText("Enter X,Y within range: ");
            	} catch (Exception ex) {
            		inputLabel.setText("Invalid format. Use 'x,y' within range");
            	}
        	}
            
        });
        flagButton.addActionListener(e -> {
        	if (board.inGame) {
        		String input = coordinateInput.getText();
            
        		try {
                	String[] parts = input.split(",");
                	int x = Integer.parseInt(parts[0].trim());
                	int y = Integer.parseInt(parts[1].trim());
                	board.clickSimulator(x - 1, y - 1, 2); // <-- You must implement this method in your Board class
                	inputLabel.setText("Enter X,Y within range: ");
            	} catch (Exception ex) {
            		inputLabel.setText("Invalid format. Use 'x,y' within range");
            	}
        	}
            
        });

        System.out.println("Minesweeper.initUI(): UI initialized.");
    }
    
    
    public void changeSize(int x, int y) {
    	this.setSize(x, y);
    }

    /**
     * This will schedule the creation / display of the Minesweeper GUI
     * on the event dispatch thread (EDT). This is the specific thread
     * that is resposible for handling Swing UI updates.
     * 
     * @param args  Default arguments for the main method
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var ex = new Minesweeper();
            ex.setVisible(true);
        });
    }
}
