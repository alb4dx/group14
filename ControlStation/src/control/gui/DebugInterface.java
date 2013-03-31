package control.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import control.test.Controller;

public class DebugInterface {
	
	private static CommandQueue myQueue;
	private static CommandComposer myComposer;
	private static OtherCommands myOther;
	private static ProgramVariables myVariables;
	private static RobotResponse myResponse;
	
	private Controller myController;
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("ROBOT DEBUGGER");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 600);
		frame.setLocation(400, 100);

		
		JPanel content = new JPanel();
		content.setLayout(new GridLayout(1, 2));
		
		
		myQueue = new CommandQueue();
		myComposer = new CommandComposer();
		myOther = new OtherCommands();
		myVariables = new ProgramVariables();
		myResponse = new RobotResponse();
		
		JPanel left = new JPanel();
		//left.setBackground(Color.green);
		left.setSize(250, 600);
		left.setLayout(new FlowLayout());
		
		JPanel right = new JPanel();
		right.setBackground(Color.blue);
		right.setSize(250, 600);
		right.setLayout(new FlowLayout());
		
		left.add(myQueue);
		left.add(myComposer);
		left.add(myOther);
		
		right.add(myVariables);
		right.add(myResponse);
		
		content.add(left);
		content.add(right);
		
		frame.setContentPane(content);
		frame.setResizable(false);
		frame.setVisible(true);
		
	}

}
