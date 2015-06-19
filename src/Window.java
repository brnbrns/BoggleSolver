/***************
* Brian Burns
* Window.java
* Provides GUI functionality
***************/

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.*;

public class Window extends JFrame implements ActionListener {
	private JLabel enterBoard;
	private JLabel selectDict;
	
	private JButton dict;

	private JTextField boardEntry;

	JFileChooser fc;

	public Window() {
		super("Boggle Solver");

		Container c = getContentPane();
		c.setLayout(null);

		enterBoard = new JLabel("Please enter the game board, with spaces between letters, from top to bottom:");
		enterBoard.setSize(500, 50);
		enterBoard.setLocation(10, 2);
		c.add(enterBoard);

		this.setSize(800, 600);
		this.setLocation(100, 100);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

	}
}