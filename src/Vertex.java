/***************
 * Brian Burns
 * Vertex.java
 * Contains the value and position on the board of a vertex (letter)
 ***************/

public class Vertex {
	// Needed variables
	private char value;
	private int position;
	
	// Constructor
	public Vertex(char v, int p) {
		value = v;
		position = p;
	}
	// Getters
	public char getChar() {return value;}
	public int getPos() {return position;}
}
