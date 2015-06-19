/***************
 * Brian Burns
 * Input.java
 * Reads in the board and dictionary files, controls the program
 ***************/
import java.io.*;
import java.util.*;

public class Input {
	// Needed variables
	private static CharacterTree dict;
	private static Vertex[][] board;
	private Scanner s;
	
	// Reads the game board into a matrix
	private void readInBoard() throws FileNotFoundException {
		// Open the file
		s = new Scanner(new File("board.txt"));
		// Get the dimensions
		String[] input = s.nextLine().split(" ");
		int rows = Integer.parseInt(input[0]);
		int cols = Integer.parseInt(input[1]);
		// Create a board matrix with the dimensions
		board = new Vertex[rows][cols];
		// Keep track of current vertex
		int boardPosition = 0;
		// Take in each row
		for (int r=0; r<rows; r++) {
			String rowString = s.nextLine().replaceAll("\\s", "");
			// Convert the board row into characters
			char[] chars = rowString.toCharArray();
			// Create a vertex array
			Vertex[] row = new Vertex[cols];
			for (int c=0; c<cols; c++) {
				// Make each character a vertex
				Vertex v = new Vertex(chars[c], boardPosition);
				row[c] = v;
				boardPosition++;
			}
			// Add the vertex row to the board
			board[r] = row;
		}
		s.close();
	}
	
	// Reads in the dictionary to use
	private void readInDict() throws FileNotFoundException {
		// Create a new character tree
		dict = new CharacterTree();
		// Open the file
		s = new Scanner(new File("dict.txt"));
		// Add each word to the tree
		while (s.hasNextLine()) {
			String word = s.nextLine();
			dict.add(word);
		}
		s.close();
	}
	
	public static void main(String[] args) {
		Input i = new Input();
		try {
			// Read in the board and the dictionary
			i.readInBoard();
			i.readInDict();
		// Catch errors
		} catch (FileNotFoundException e) {
			System.err.println("File not found.");
			System.exit(1);
		}

		Window w = new Window();
		// Create a new graph
		Graph g = new Graph(board, dict);
		// Solve the game
		ArrayList<String> solution = g.solve();
		// Output all valid words
		Output o = new Output(solution);
		o.printSolution();
	}
}
