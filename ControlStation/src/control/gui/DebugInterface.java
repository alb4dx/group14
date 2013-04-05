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

import control.communication.CommandMessage;
import control.communication.CommandMessage.CommandType;
import control.communication.ResponseMessage;
import control.main.Controller;

public class DebugInterface {
	
	private static CommandQueue myQueue;
	private static CommandComposer myComposer;
	private static OtherCommands myOther;
	private static ProgramVariables myVariables;
	private static RobotResponse myResponse;
	
	private Controller myController;
	
	private JFrame myFrame;
	
	/* *
	 * debug
	 *		refresh variables based on received string
	 *
	 * command queue
	 *		own queue based off of what's displayed
	 * 		step sends one and removes it (to controller)
	 * 		execute sends and removes all of them (to controller)
	 * 		delete deletes the selected one
	 *		command queue list should be a JLIST not a text area
	 * */
	
//	public static void main(String[] args) {
//		
//		DebugInterface myDebug = new DebugInterface(new Controller());
//		
//	}
	
	public static CommandQueue getMyQueue() {
		return myQueue;
	}

	public static void setMyQueue(CommandQueue myQueue) {
		DebugInterface.myQueue = myQueue;
	}

	public static CommandComposer getMyComposer() {
		return myComposer;
	}

	public static void setMyComposer(CommandComposer myComposer) {
		DebugInterface.myComposer = myComposer;
	}

	public static OtherCommands getMyOther() {
		return myOther;
	}

	public static void setMyOther(OtherCommands myOther) {
		DebugInterface.myOther = myOther;
	}

	public static ProgramVariables getMyVariables() {
		return myVariables;
	}

	public static void setMyVariables(ProgramVariables myVariables) {
		DebugInterface.myVariables = myVariables;
	}

	public static RobotResponse getMyResponse() {
		return myResponse;
	}

	public static void setMyResponse(RobotResponse myResponse) {
		DebugInterface.myResponse = myResponse;
	}

	public Controller getMyController() {
		return myController;
	}

	public void setMyController(Controller myController) {
		this.myController = myController;
	}

	public JFrame getMyFrame() {
		return myFrame;
	}

	public void setMyFrame(JFrame myFrame) {
		this.myFrame = myFrame;
	}

	public DebugInterface(Controller contr) {
		JFrame frame = new JFrame("ROBOT DEBUGGER");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 600);
		frame.setLocation(650, 100);
		
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
				String selected = (String)myComposer.getCommands().getSelectedItem();
				if(selected.equals("Init")) {
					myController.addMessage(new CommandMessage(CommandType.INIT));
				} else if(selected.equals("Query")) {
					myController.addMessage(new CommandMessage(CommandType.QUERY));					
				} else if(selected.equals("Quit")) {
					myController.addMessage(new CommandMessage(CommandType.QUIT));					
				} else if(selected.equals("Move")) {
					myController.addMessage(new CommandMessage(CommandType.MOVE, myComposer.getDegrees().getValue()));
				} else if(selected.equals("Turn")) {
					myController.addMessage(new CommandMessage(CommandType.TURN, myComposer.getDegrees().getValue()));
				} else if(selected.equals("Claw")) {
					myController.addMessage(new CommandMessage(CommandType.CLAW, ((Integer)myComposer.getDegrees().getValue())/100.0));
				} else {
					myController.addMessage(new CommandMessage((CommandType.ACK)));
				}
			}
		});
		
		myOther.getAutonomous().addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(myOther.getAutonomous().isSelected()) {
					myController.addMessage(new CommandMessage(CommandType.AUTO));
				}
			}
		});
		
		myOther.getHalt().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myController.addMessage(new CommandMessage(CommandType.HALT));
			}
		});
		
		myOther.getPower().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myController.addMessage(new CommandMessage(CommandType.POWD));
			}
		});
		
		myOther.getReset().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myController.addMessage(new CommandMessage(CommandType.RSET));
			}
		});
		
		myVariables.getRequestUpdate().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myController.addMessage(new CommandMessage(CommandType.UPDT));
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
		//frame.setVisible(true);
	}
	
	public void display() {
		myFrame.setVisible(true);
	}
	
	// add response message to print out
	public void messageReceived(ResponseMessage r) {
		myResponse.getMyResponses().append("\n" + r.getMessageString());
	}
	
	public void updateVariables(ResponseMessage r) {
		// update variables in myVariables
		String response = r.getFormattedMessage(); // message with curly braces
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
	
	public CommandQueue getQueue() {
		return myQueue;
	}

}
