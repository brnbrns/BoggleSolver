/***************
 * Brian Burns
 * Graph.java
 * Creates an adjacency list for each vertex and solves the game using depth first search
 ***************/

import java.util.ArrayList;

public class Graph {
	// Needed variables
	private int rows, cols;
	private ArrayList<Vertex>[] adj;
	private ArrayList<String> solution;
	private CharacterTree dict;
	
	// Constructor
	public Graph(Vertex[][] matrix, CharacterTree t) {
		rows = matrix.length;
		cols = matrix[0].length;
		adj = new ArrayList[rows*cols];
		solution = new ArrayList<String>();
		dict = t;
		// Create adjacency lists
		listify(matrix);
	}
	
	// Creates each vertex's adjacency list
	private void listify(Vertex[][] matrix) {
		// Keep track of the current vertex spot
		int position = 0;
		// For each vertex
		for (int i=0; i<rows; i++) {
			for (int j=0; j<cols; j++) {
				// Create the vertex's adjacency list
				ArrayList<Vertex> list = new ArrayList<Vertex>();
				Vertex v = matrix[i][j];
				// Add the vertex to the list
				list.add(v);
				
				// Check all 8 neighbors and add them to the adjacency list if they exist
				// above
				if (i % rows != 0) list.add(matrix[i-1][j]);
				// above right
				if (i % rows != 0 && (j % (cols-1) != 0 || j==0)) list.add(matrix[i-1][j+1]);
				// right
				if (j==0 || j % (cols-1) != 0) list.add(matrix[i][j+1]);
				// below right
				if ((i % (rows-1) != 0 || i==0) && (j % (cols-1) != 0 || j==0)) list.add(matrix[i+1][j+1]);
				// below
				if (i % (rows-1) != 0 || i==0) list.add(matrix[i+1][j]);
				// below left
				if ((i % (rows-1) != 0 || i==0) && j % cols != 0) list.add(matrix[i+1][j-1]);
				// left
				if (j % cols != 0) list.add(matrix[i][j-1]);
				// above left
				if (i % rows != 0 && j % cols != 0) list.add(matrix[i-1][j-1]);
				
				// Add the list to the array
				adj[position] = list;
				// Move to the next spot
				position++;
			}
		}
	}
	
	// Solves the board by running a depth first search on each vertex
	public ArrayList<String> solve() {
		// Seen array to keep track of used letters
		boolean[] seen;
		// For each adjacency list (vertex)
		for (ArrayList<Vertex> list : adj) {	
			// Reset the seen array to all false
			seen = new boolean[rows*cols];
			// Start possible words with the current vertex's character
			String currentWord = String.valueOf(list.get(0).getChar());
			// Qu special case
			if (currentWord.equals("Q")) currentWord += "U";
			// Run a depth first search from the vertex
			dfs(list.get(0), currentWord, seen);
		}
		return solution;
	}
	
	// Runs a depth first search recursively
	private void dfs(Vertex v, String word, boolean[] seen) {
		// Mark the current vertex as seen
		seen[v.getPos()] = true;
		// If the current word is scorable (at least 3 letters)
		if (word.length() > 2) {
			// Get a list of valid words from the dictionary
			ArrayList<String> validWords = dict.getValidWords(word);
			// If there are valid words
			if (validWords != null) {
				// If the current word is valid add it to the solution list
				if (validWords.get(0).equals(word)) {
					solution.add(validWords.get(0));
					// If that's the only valid word stop searching
					if (validWords.size() == 1) return;
				}
			// If there are no valid words stop searching
			} else return;
		}
		// Get the vertex's adjacency list
		ArrayList<Vertex> currentList = null;
		for (ArrayList<Vertex> list : adj) {
			if (list.get(0).getPos() == v.getPos()) {
				currentList = list;
				break;
			}
		}
		
		// For each vertex in the adjacency list
		for (Vertex vertex : currentList) {
			// If the vertex hasn't been seen
			if (seen[vertex.getPos()] == false) {
				/* Copy the seen array into a new array to pass
				 * This is necessary so that the same seen array isn't altered
				 * Otherwise backtracking wouldn't work */
				boolean[] newSeen = new boolean[seen.length]; 
				System.arraycopy(seen, 0, newSeen, 0, seen.length);
				// Run dfs on the vertex, add its character to the word being checked
				// Qu special case
				if (vertex.getChar() =='Q') dfs(vertex, word+vertex.getChar()+"U", newSeen);
				else dfs(vertex, word+vertex.getChar(), newSeen);
			}
		}
	}
}
