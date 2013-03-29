package control.gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import control.test.Controller;

public class DebugInterface {
	
	private CommandQueue myQueue;
	private CommandComposer myComposer;
	private OtherCommands myOther;
	private ProgramVariables myVariables;
	private RobotResponse myResponse;
	
	private Controller myController;
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("ROBOT DEBUGGER");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 600);
		frame.setLocation(400, 100);

		
		JPanel content = new JPanel();
		content.setLayout(new GridLayout(1, 2));
		
		
		
		frame.setContentPane(content);
		frame.setVisible(true);
		
	}

}
