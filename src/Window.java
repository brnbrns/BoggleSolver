/***************
* Brian Burns
* Window.java
* Provides GUI functionality
***************/

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class Window extends JFrame implements ActionListener {
	private JLabel enterDimensions;
	private JLabel by;
	private JLabel enterBoard;
	private JLabel selectDict;
	private JLabel dictSuccess;
  private JLabel wordCount;
	
	private JButton dictButton;
	private JButton solveButton;

	private JTextField rowEntry;
	private JTextField colEntry;
	private JTextField boardEntry;

  private DefaultListModel words;
  private JList wordList;
  private JScrollPane scrollPane;

	private JFileChooser fc;

  private CharacterTree dict = null;

	public Window() {
		super("Boggle Solver");

    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		Container c = getContentPane();
		c.setLayout(null);

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

    words = new DefaultListModel();
    wordList = new JList(words);
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

		this.setSize(800, 600);
		this.setLocation(100, 100);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		// User pressed select dictionary button
		if (e.getSource() == dictButton) {
			fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File dictFile = fc.getSelectedFile();
				try {
					dict = readDictionary(dictFile);
				} catch (FileNotFoundException error) {
					error.printStackTrace();
				}
				dictSuccess.setVisible(true);
			}
    // User pressed solve button
		} else if (e.getSource() == solveButton) {
      // Create the board
			Vertex[][] board = readBoard();
      // Send the board and dictionary chosen to Graph
      Graph g = new Graph(board, dict);
      // Get the solution
      ArrayList<String> solution = g.solve();
      // Display the scroll pane
      scrollPane.setVisible(true);
      // Add each answer to the scroll pane
      for (String word : solution) {
        words.addElement(word);
      }
      // Display the word count
      wordCount.setText("Words found: " + solution.size());
      wordCount.setVisible(true);
		}
	}

	private Vertex[][] readBoard() {
		int rows = Integer.parseInt(rowEntry.getText());
		int cols = Integer.parseInt(colEntry.getText());
		Vertex[][] board = new Vertex[rows][cols];
		String boardString = boardEntry.getText().replaceAll("\\s", "");
		String[] rowStrings = splitBoard(boardString, rows);
		int boardPosition = 0;
    for (int r = 0; r<rowStrings.length; r++) {
      char[] chars = rowStrings[r].toCharArray();
      Vertex[] row = new Vertex[cols];
      for (int c=0; c<cols; c++) {
        Vertex v = new Vertex(chars[c], boardPosition);
        row[c] = v;
        boardPosition++;
      }
      board[r] = row;
    }
		return board;
	}

	private CharacterTree readDictionary(File toRead) throws FileNotFoundException {
		CharacterTree dict = new CharacterTree();
		Scanner s = new Scanner(toRead);
		while (s.hasNextLine()) {
			String word = s.nextLine();
			dict.add(word);
		}
		s.close();
		return dict;
	}

	private String[] splitBoard(String text, int size) {
		String[] result = new String[(text.length() + size - 1) / size];
		int spot = 0;
		for (int i=0; i<text.length(); i+=size) {
			result[spot] = text.substring(i, Math.min(text.length(), i+size));
			spot++;
		}
		return result;
	}

  public static void main(String[] args) {
    Window w = new Window();
  }
}