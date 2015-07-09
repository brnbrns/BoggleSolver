/***************
 * Brian Burns
 * CharacterTree.java
 * Creates a tree with characters as children to store a constant time access dictionary
 ***************/

// Import
import java.util.ArrayList;

/**
 * Implements a trie (tree of characters) for quick searching
 * @author Brian Burns
 */
public class CharacterTree {
	/**
   * Root node that holds an impossible character
   */
	private Node root = new Node(';');
	
	/**
   * Returns a list of words in the dictionary with the prefix given
   * @param word The prefix to search for
   * @return An ArrayList of strings beginning with the parameter
   */
	public ArrayList<String> getValidWords(String word) {
		// Start at the root
		Node end = root;
		// Traverse down the tree one character at a time
		for (int i=0; i<word.length(); i++) {
			end = end.getChild(word.charAt(i));
			// If the tree ends return null
			if (end == null) return null;
		}
		// Return the list of words
		return end.words();
	}
	
	/**
   * Adds a word to the trie
   * @param word The string to add to the tree
   */
	public void add(String word) {root.add(word);}
	
	/**
   * A node in the character tree
   * @author Brian Burns
   */
	private class Node {
		/**
     * The character stored in the node
     */
		private char value;
    /**
     * This node's parent
     */
		private Node parent;
    /**
     * This node's children (one for each letter of the alphabet)
     */
		private Node[] children;
    /**
     * Returns whether this node is a leaf node
     */
		private boolean leaf;
    /**
     * Returns whether this node is a word
     */
    private boolean word;
				
		/**
     * Initializes the node
     * @param c The character to store in this node
     */
		public Node(char c) {
			value = c;
			// One child for each character
			children = new Node[26];
			// Each node is a leaf at time of creation
			leaf = true;
			word = false;
		}
		
		/**
     * Recursively adds a word to the trie one character at a time
     * @param word The string to add to the trie
     */
		public void add(String word) {
			// No longer a leaf
			leaf = false;
			// Get the number of the character in the alphabet (0-25)
			int alphabetSpot = word.charAt(0) - 'A';
			// If the child doesn't exit yet create it
			if (children[alphabetSpot] == null) {
				children[alphabetSpot] = new Node(word.charAt(0));
				children[alphabetSpot].setParent(this);
			}
			// Add the next character if the word continues
			if (word.length() > 1) children[alphabetSpot].add(word.substring(1));
			// Otherwise the word is added, make it a word
			else children[alphabetSpot].makeWord();
		}
		
		/**
     * Recursively creates a list of all the words from the children of the current node
     * @return A list of all valid words that stem from this node
     */
		public ArrayList<String> words() {
			// Create the list
			ArrayList<String> list = new ArrayList<String>();
			// If the current node is a word add it
			if (word) list.add(makeString());
			// If the node has children
			if (!leaf) {
				// Check each child
				for (int i=0; i<children.length; i++) {
					if (children[i] != null) {
						// Call words() on the child
						list.addAll(children[i].words());
					}
				}
			}
			return list;
		}
		
		/**
     * Creates a string from characters, used when making a list of words to return
     * @return A string representing this node's lineage (All parent node characters and this node's character)
     */
		public String makeString() {
			// If we're at the root return
			if (parent == null) return "";
			// Otherwise keep going up the tree and adding characters
			else return parent.makeString() + value;
		}
		
		/**
     * Returns the specified child given a character
     * @param c The character stored in the child to return
     * @return The child of this node with the specified character
     */
		public Node getChild(char c) {return children[c - 'A'];}
		
    /**
     * Sets the current node's parent
     * @param p The node to be set as the parent
     */
		public void setParent(Node p) {parent = p;}
		
    /**
     * Classifies the current node as representing a word
     */ 
		public void makeWord() {word = true;}
	}
}
