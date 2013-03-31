package control.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.*;

/**
 * TODO: 
 * actual commands that should be in commandList (34)
 * change parameter spinner based on command selected(44)
 * actionlisteners
 * 
 * @author Steph
 *
 */

public class CommandComposer extends JPanel{
	
	private JLabel composerLabel;
	private JComboBox commandList;
	private JSpinner myDegrees;
	private JCheckBox myTimestamp;
	private JButton sendCommand;
	
	public CommandComposer() {

		setPreferredSize(new Dimension(250, 170));
		setLayout(new FlowLayout(FlowLayout.LEADING, 8, 3));
		
		composerLabel = new JLabel("Command Composer");
		composerLabel.setFont(new Font("Arial", Font.BOLD, 15));
		
		String[] commands = {"Motor A forwards", "Motor B forwards"};
		commandList = new JComboBox(commands);
		commandList.setFont(new Font("Arial", Font.PLAIN, 13));
		commandList.setPreferredSize(new Dimension(235, 40));
		commandList.setEditable(false);
		
		JLabel params = new JLabel("  Parameter");
		params.setFont(new Font("Arial", Font.PLAIN, 13));
		
		SpinnerModel model = new SpinnerNumberModel(0, -720, 720, 5);
		myDegrees = new JSpinner(model);
		myDegrees.setFont(new Font("Arial", Font.PLAIN, 13));
		myDegrees.setPreferredSize(new Dimension(90, 30));
		myDegrees.setEditor(new JSpinner.DefaultEditor(myDegrees));
		
		myTimestamp = new JCheckBox("Include Timestamp");
		myTimestamp.setFont(new Font("Arial", Font.PLAIN, 13));	
		
		sendCommand = new JButton("Send Command");
		sendCommand.setFont(new Font("Arial", Font.PLAIN, 14));
		sendCommand.setPreferredSize(new Dimension(235, 35));
		
		add(composerLabel);
		add(commandList);
		add(params);
		add(myDegrees);
		add(myTimestamp);
		add(sendCommand);
		
	}
	
	public JComboBox getCommands() {
		return commandList;
	}
	
	public JSpinner getDegrees() {
		return myDegrees;
	}
	
	public boolean getTimestamp() {
		return myTimestamp.isSelected();
	}
	
	public JButton getSendButton() {
		return sendCommand;
	}

}
