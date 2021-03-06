/***************
* Brian Burns
* Window.java
* Provides GUI functionality
***************/

// Imports
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

// Suppress Serial Warnings
@SuppressWarnings("serial")

/**
 * Controls the GUI and contains main
 * @author Brian Burns
 */
public class Window extends JFrame implements ActionListener {
  /**
   * Instructs user to enter dimensions
   */
	private JLabel enterDimensions;
  /**
   * Puts 'x' between row and column entry fields
   */
	private JLabel by;
  /**
   * Instructs user how to enter the game board
   */
	private JLabel enterBoard;

  /**
   * Instructs user to enter dimensions
   */
	private JLabel selectDict;
  /**
   * Instructs user to select a dictionary file
   */
	private JLabel dictSuccess;
  /**
   * Shows the number of words found
   */
  private JLabel wordCount;
	/**
	 * Displays 'Search:' above the search box
	 */
  private JLabel search;

  /**
   * Button for selecting a dictionary file
   */
	private JButton dictButton;
  /**
   * Button for solving the board
   */
	private JButton solveButton;

  /**
   * Text field to enter number of rows
   */
	private JTextField rowEntry;
  /**
   * Text field to enter number of columns
   */
	private JTextField colEntry;
  /**
   * Text field to enter the game board
   */
	private JTextField boardEntry;
	/**
	 * Text field to enter a search query
	 */
  private JTextField searchEntry;

  /**
   * List Model for the solution
   */
  private DefaultListModel<String> words;
  /**
   * List for the solution words
   */
  private JList<String> wordList;
  /**
   * Scroll pane to display solution words
   */
  private JScrollPane scrollPane;

  /**
   * File chooser to get dictionary file
   */
	private JFileChooser fc;

  /**
   * Character tree to search for valid words
   */
  private CharacterTree dict = null;

	/**
	 * Character tree to search through solution words
	 */
  private CharacterTree solutionTrie;

  /**
   * Creates the window and positions the items
   */
	public Window() {
    // Initialize JFrame
		super("Boggle Solver");
    // Exit the application on window close
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    // Create the container
		Container c = getContentPane();
		c.setLayout(null);

    // Initialize and position items
		enterDimensions = new JLabel("Please enter the board dimensions:");
		enterDimensions.setSize(300, 50);
		enterDimensions.setLocation(10, 2);
		c.add(enterDimensions);

		rowEntry = new JTextField("4");
		rowEntry.setSize(30, 20);
		rowEntry.setLocation(10, 40);
		rowEntry.setHorizontalAlignment(SwingConstants.CENTER);
		c.add(rowEntry);

		by = new JLabel("x");
		by.setSize(10, 50);
		by.setLocation(43, 25);
		c.add(by);

		colEntry = new JTextField("4");
		colEntry.setSize(30, 20);
		colEntry.setLocation(55, 40);
		colEntry.setHorizontalAlignment(SwingConstants.CENTER);
		c.add(colEntry);

		enterBoard = new JLabel("Now the game board, with spaces between letters, from top to bottom:");
		enterBoard.setSize(550, 50);
		enterBoard.setLocation(10, 55);
		c.add(enterBoard);

		boardEntry = new JTextField();
		boardEntry.setSize(200, 20);
		boardEntry.setLocation(10, 95);
		c.add(boardEntry);

		dictButton = new JButton("Select Dictionary File");
		dictButton.addActionListener(this);
		dictButton.setSize(200, 50);
		dictButton.setLocation(10, 130);
		c.add(dictButton);

		dictSuccess = new JLabel("Success!");
		dictSuccess.setSize(100, 50);
		dictSuccess.setLocation(220, 130);
		dictSuccess.setVisible(false);
		c.add(dictSuccess);

		solveButton = new JButton("Solve!");
		solveButton.addActionListener(this);
		solveButton.setSize(80, 50);
		solveButton.setLocation(350, 180);
		c.add(solveButton);

    words = new DefaultListModel<String>();
    wordList = new JList<String>(words);
    wordList.setFont(new Font("Monospaced", Font.PLAIN, 12));
    scrollPane = new JScrollPane(wordList);
    scrollPane.setSize(500, 300);
    scrollPane.setLocation(145, 240);
    scrollPane.setVisible(false);
    c.add(scrollPane);

    wordCount = new JLabel("Nothing to see here");
    wordCount.setSize(125, 50);
    wordCount.setLocation(655, 240);
    wordCount.setVisible(false);
    c.add(wordCount);

    search = new JLabel("Search:");
    search.setSize(125, 50);
    search.setLocation(655, 260);
    search.setVisible(false);
    c.add(search);

    searchEntry = new JTextField();
    searchEntry.addActionListener(this);
    searchEntry.setSize(125, 20);
    searchEntry.setLocation(655, 300);
    searchEntry.setVisible(false);
    c.add(searchEntry);

		this.setSize(800, 600);
		this.setLocation(100, 100);
		this.setVisible(true);
	}

	/**
   * Handles actions on dictionary and solve buttons
   * @param e The event to act on
   */
  public void actionPerformed(ActionEvent e) {
		// User pressed select dictionary button
		if (e.getSource() == dictButton) {
      // Open the file chooser
			fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(this);
			// Check for approval
      if (returnVal == JFileChooser.APPROVE_OPTION) {
				// Get the file selected
        File dictFile = fc.getSelectedFile();
				try {
					dict = readDictionary(dictFile);
				} catch (FileNotFoundException error) {
					error.printStackTrace();
				}
        // Inform user of success
				dictSuccess.setVisible(true);
			}

    // User pressed solve button
		} else if (e.getSource() == solveButton) {
      // Clear the scroll pane in case of previous search
      words.removeAllElements();
      // Create the board
			Vertex[][] board = readBoard();
      // Send the board and dictionary chosen to Graph
      Graph g = new Graph(board, dict);
      // Get the solution
      ArrayList<String> solution = g.solve();
			// Convert the solution to an array
      String[] solutionArray = new String[solution.size()];
      for (int i=0; i<solutionArray.length; i++) {
        solutionArray[i] = solution.get(i);
      }
			// Sort the solution
      Sorter sorter = new Sorter(solutionArray);
      sorter.alphabetize(0, solutionArray.length);
      solutionArray = sorter.getSorted();
      // Display the scroll pane
      scrollPane.setVisible(true);
			// Create a character tree from the solution words
      solutionTrie = new CharacterTree();
      // Add each answer to the scroll pane
      for (String word : solutionArray) {
        words.addElement(word);
				// Add each answer to the solution trie
        solutionTrie.add(word);
      }
      // Display the word count
      wordCount.setText("Words found: " + solution.size());
      wordCount.setVisible(true);
			// Display the search function
      search.setVisible(true);
      searchEntry.setVisible(true);

		// User pressed enter in the search field
		} else if (e.getSource() == searchEntry) {
      // Get the query
			String query = searchEntry.getText();
      // Search the solution trie for matches
			ArrayList<String> searchResult = solutionTrie.getValidWords(query.toUpperCase());
      // If there are results
			if (searchResult != null) {
        // Convert the result to an array
				String[] resultArray = new String[searchResult.size()];
        for (int i=0; i<resultArray.length; i++) {
          resultArray[i] = searchResult.get(i);
        }
				// Remove all words from the answer box
        words.removeAllElements();
				// Sort the search result
        Sorter sorter = new Sorter(resultArray);
        sorter.alphabetize(0, resultArray.length);
        resultArray = sorter.getSorted();
        // Add the result to the answer box
				for (String s : resultArray) {
          words.addElement(s);
        }
				// If no result empty the answer box
      } else words.removeAllElements();
    }
	}

  /**
   * Reads and formats the board from the input text field
   * @return The game board formatted in a matrix
   */
	private Vertex[][] readBoard() {
    // Get the rows and columns
		int rows = Integer.parseInt(rowEntry.getText());
		int cols = Integer.parseInt(colEntry.getText());
    // Create the board matrix
		Vertex[][] board = new Vertex[rows][cols];
    // Get the string the user entered and split it by row
		String boardString = boardEntry.getText().replaceAll("\\s", "");
		String[] rowStrings = splitBoard(boardString, cols);
		int boardPosition = 0;
    // Split each row into a char array
    for (int r = 0; r<rowStrings.length; r++) {
      char[] chars = rowStrings[r].toCharArray();
      // Create a row of vertices
      Vertex[] row = new Vertex[cols];
      // Create each vertex and add it to the row
      for (int c=0; c<cols; c++) {
        Vertex v = new Vertex(Character.toUpperCase(chars[c]), boardPosition);
        row[c] = v;
        boardPosition++;
      }
      // Add the row to the board
      board[r] = row;
    }
		return board;
	}

  /**
   * Reads in the dictionary file
   * @param toRead The file to read
   * @return The CharacterTree representing the dicitonary file
   */
	private CharacterTree readDictionary(File toRead) throws FileNotFoundException {
		// Create the character tree
    CharacterTree dict = new CharacterTree();
    // Read the file and add each word to the tree
		Scanner s = new Scanner(toRead);
		while (s.hasNextLine()) {
			String word = s.nextLine();
			dict.add(word);
		}
		s.close();
		return dict;
	}

  /**
   * Splits the text entry based on where the rows end
   * @param text The string to split
   * @param size The size of each resulting string after splitting
   * @return An array of the resulting strings after splitting
   */
	private String[] splitBoard(String text, int size) {
    // Create the return string
		String[] result = new String[(text.length() + size - 1) / size];
		// Split the string
    int spot = 0;
		for (int i=0; i<text.length(); i+=size) {
			result[spot] = text.substring(i, Math.min(text.length(), i+size));
			spot++;
		}
		return result;
	}

  /**
   * Starts the program
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    Window w = new Window();
  }
}
