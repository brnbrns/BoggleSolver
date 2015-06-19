/***************
 * Brian Burns
 * Output.java
 * Outputs valid words
 ***************/

import java.util.ArrayList;

public class Output {
	// Needed variable
	private ArrayList<String> solution;
	
	// Constructor
	public Output(ArrayList<String> s) {
		solution = s;
	}
	
	// Prints all valid words
	public void printSolution() {
		// Print each word
		for (String word : solution) {
			System.out.println(word);
		}
	}
}
