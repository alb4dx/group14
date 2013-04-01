package control.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
		
		DebugInterface myDebug = new DebugInterface(new Controller());
		
	}
	
	public DebugInterface(Controller contr) {
		JFrame frame = new JFrame("ROBOT DEBUGGER");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 600);
		frame.setLocation(400, 100);
		
		JPanel content = new JPanel();
		content.setLayout(new GridLayout(1, 2));
		
		myController = contr;
		
		myQueue = new CommandQueue();
		myComposer = new CommandComposer();
		myOther = new OtherCommands();
		myVariables = new ProgramVariables();
		myResponse = new RobotResponse();
		
		myComposer.getSendButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// make command message and add to controller queue
				// for original programming purposes, print string
				String command = "{";
				command += myController.seq;
				if(myController.seq == 0) {
					myController.seq = 1;
				} else { myController.seq = 0; }
				String selected = (String)myComposer.getCommands().getSelectedItem();
				if(selected.equals("Init") || selected.equals("Query") || selected.equals("Quit")) {
					command += selected.toLowerCase() + "|";
				} else if (selected.equals("Move") || selected.equals("Turn")) {
					command += selected.toLowerCase() + ":" + myComposer.getDegrees().getValue() + "|";
				} else if (selected.equals("Claw")) {
					int s = (Integer) myComposer.getDegrees().getValue();
					double temp = s / 100.0;
					DecimalFormat form = new DecimalFormat("#0.00");
					command += selected.toLowerCase() + ":" + form.format(temp) + "|";
				} else { 
					command += "ack|"; }
				command += checksum(command);
				if(myComposer.getTimestamp()) {
					DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
					Date date = new Date();
					command += "|" + dateFormat.format(date);
				}
				command += "}";
				System.out.println("Command: \t" + command);
				//myController.addMessage(command);
			}
		});
		
		myOther.getAutonomous().addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(myOther.getAutonomous().isSelected()) {
					String command = "{";
					command += myController.seq;
					if(myController.seq == 0) {
						myController.seq = 1;
					} else { myController.seq = 0; }
					command += "auto|";
					command += checksum(command) + "}";
					System.out.println("Command: \t" + command);
					//myController.addMessage(command);
				}
			}
		});
		
		myOther.getHalt().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String command = "{";
				command += myController.seq;
				if(myController.seq == 0) {
					myController.seq = 1;
				} else { myController.seq = 0; }
				command += "halt|";
				command += checksum(command) + "}";
				System.out.println("Command: \t" + command);
				//myController.addMessage(command);
			}
		});
		
		myOther.getPower().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String command = "{";
				command += myController.seq;
				if(myController.seq == 0) {
					myController.seq = 1;
				} else { myController.seq = 0; }
				command += "powd|";
				command += checksum(command) + "}";
				System.out.println("Command: \t" + command);
				//myController.addMessage(command);
			}
		});
		
		myOther.getReset().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String command = "{";
				command += myController.seq;
				if(myController.seq == 0) {
					myController.seq = 1;
				} else { myController.seq = 0; }
				command += "rset|";
				command += checksum(command) + "}";
				System.out.println("Command: \t" + command);
				//myController.addMessage(command);
			}
		});
		
		myVariables.getRequestUpdate().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String command = "{";
				command += myController.seq;
				if(myController.seq == 0) {
					myController.seq = 1;
				} else { myController.seq = 0; }
				command += "updt|";
				command += checksum(command) + "}";
				System.out.println("Command: \t" + command);
				//myController.addMessage(command);
				
				// HOW do I get the robot response message with all the telemetry?
				String response = "{0updr&distance:5&light:5&sound:5&touch:false&claw:" +
						".5&heading:90&speed:5&ultrasonic:5&connectionStatus:true&motorA:" +
						"37&motorB:65&motorC:48|709}";
				String[] groups = response.split("&");
				ArrayList<Object> splits = new ArrayList<Object>();
				for(int x = 1; x < groups.length; x++) {
					if(x < groups.length - 1) {
						String[] temp = groups[x].split(":");
						splits.add(temp[1]);
					}
					else {
						String[] temp = groups[x].split(":");
						int index = temp[1].indexOf('|');
						splits.add(temp[1].substring(0, index));
					}
				}
				System.out.println(splits);
				myVariables.update(splits);
			}
		});
		
		JPanel left = new JPanel();
		left.setSize(250, 600);
		left.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
		
		JPanel right = new JPanel();
		right.setSize(250, 600);
		right.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
		
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
	
	public int checksum(String s) {
		int check = 0;
		for(int x= 0; x < s.length(); x++) {
			check += s.charAt(x);
		}
		return check;
	}

}
