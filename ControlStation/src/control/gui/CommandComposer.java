package control.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.*;

/**
 * TODO: 
 * change parameter spinner based on command selected
 * 
 * @author Steph
 *
 */

public class CommandComposer extends JPanel{
	
	private JLabel composerLabel;
	private JComboBox commandList;
	private JSpinner myDegrees;
	//private JCheckBox myTimestamp;
	private JButton sendCommand;
	
	public CommandComposer() {

		setPreferredSize(new Dimension(250, 170));
		setLayout(new FlowLayout(FlowLayout.LEADING, 8, 3));
		
		composerLabel = new JLabel("Command Composer");
		composerLabel.setFont(new Font("Arial", Font.BOLD, 15));
		
		String[] commands = {"Init", "Move", "Turn", "Claw", "Stop", "Query", "Quit", "Acknowledge"};
		commandList = new JComboBox(commands);
		commandList.setFont(new Font("Arial", Font.PLAIN, 13));
		commandList.setPreferredSize(new Dimension(235, 40));
		commandList.setEditable(false);
		commandList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				String command = (String) cb.getSelectedItem();
				if(command.equals("Init")) {
					myDegrees.setEnabled(false);	
				} else if(command.equals("Move")) {
					SpinnerModel model = new SpinnerNumberModel(0, -720, 720, 5);
					myDegrees.setModel(model);
					myDegrees.setValue(0);
					try {
						myDegrees.commitEdit();
					} catch (ParseException e1) { }
					myDegrees.setEnabled(true);
				} else if(command.equals("Turn")) {
					SpinnerModel model = new SpinnerNumberModel(0, -180, 180, 5);
					myDegrees.setModel(model);
					myDegrees.setValue(0);
					myDegrees.setEnabled(true);
				} else if(command.equals("Claw")) {
					SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 100, 5);	
					myDegrees.setModel(model);
					myDegrees.setValue(0);
					myDegrees.setEnabled(true);
				} else if(command.equals("Stop")) {
					myDegrees.setEnabled(false);
				} else if(command.equals("Query")) {
					myDegrees.setEnabled(false);	
				} else if(command.equals("Quit")) {
					myDegrees.setEnabled(false);	
				} else if(command.equals("Acknowledge")) {
					myDegrees.setEnabled(false);
				}
			}
		});
		
		JLabel params = new JLabel("  Parameter");
		params.setFont(new Font("Arial", Font.PLAIN, 13));
		
		SpinnerModel model = new SpinnerNumberModel(0, -720, 720, 5);
		myDegrees = new JSpinner(model);
		myDegrees.setFont(new Font("Arial", Font.PLAIN, 13));
		myDegrees.setPreferredSize(new Dimension(90, 30));
		myDegrees.setEditor(new JSpinner.DefaultEditor(myDegrees));
		myDegrees.setEnabled(false);
		
		//myTimestamp = new JCheckBox("Include Timestamp");
		//myTimestamp.setFont(new Font("Arial", Font.PLAIN, 13));	
		
		sendCommand = new JButton("Send Command");
		sendCommand.setFont(new Font("Arial", Font.PLAIN, 14));
		sendCommand.setPreferredSize(new Dimension(235, 35));
		
		add(composerLabel);
		add(commandList);
		add(params);
		add(myDegrees);
		//add(myTimestamp);
		add(sendCommand);
		
	}
	
	public JComboBox getCommands() {
		return commandList;
	}
	
	public JSpinner getDegrees() {
		return myDegrees;
	}
	
	/*public boolean getTimestamp() {
		return myTimestamp.isSelected();
	}*/
	
	public JButton getSendButton() {
		return sendCommand;
	}

}
