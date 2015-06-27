/***************
 * Brian Burns
 * Vertex.java
 * Contains the value and position on the board of a vertex (letter)
 ***************/

/**
 * Stores a character as a vertex on the board
 * @author Brian Burns
 */
public class Vertex {
	/**
   * The value of the character
   */
	private char value;
	/**
   * The vertex's position on the board
   */
  private int position;
	
	/**
   * Initializes the vertex
   * @param v The character to store in the vertex
   * @param p The vertex's position on the board
   */
	public Vertex(char v, int p) {
		value = v;
		position = p;
	}
	
  /**
   * Returns the character stored in this vertex
   * @return The character stored in this vertex
   */
	public char getChar() {return value;}

  /**
   * Returns this vertex's position on the board
   * @return This vertex's position on the board
   */
	public int getPos() {return position;}
}
