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
	
	private JButton dictButton;
	private JButton solveButton;

	private JTextField rowEntry;
	private JTextField colEntry;
	private JTextField boardEntry;

	JFileChooser fc;

  CharacterTree dict = null;

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
		enterBoard.setSize(450, 50);
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
		dictSuccess.setSize(60, 50);
		dictSuccess.setLocation(220, 130);
		dictSuccess.setVisible(false);
		c.add(dictSuccess);

		solveButton = new JButton("Solve!");
		solveButton.addActionListener(this);
		solveButton.setSize(75, 50);
		solveButton.setLocation(350, 180);
		c.add(solveButton);

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
		} else if (e.getSource() == solveButton) {
      //System.out.println("READING BOARD");
			Vertex[][] board = readBoard();
      //System.out.println("BOARD READ");
      Graph g = new Graph(board, dict);
      //System.out.println("SOLVING");
      ArrayList<String> solution = g.solve();
      //System.out.println("PRINTING");
      Output o = new Output(solution);
      o.printSolution();
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