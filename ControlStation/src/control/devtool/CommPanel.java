package control.devtool;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultCaret;

import control.communication.Message;


@SuppressWarnings("serial")
public class CommPanel extends JPanel
{
	
	ArrayList<Component>	myComps		= new ArrayList<Component>();
	
	String[]				responses	=
										{ "Query", "Command received" };
	private DevToolWindow			myDriver;
	private JTextArea		messageArea;
	String[]				mySensors	=
										{ "0", "0", "0", "0", "0", "0", "0" };
	//private JTextPane		customMessage;
	private JTextArea		customMessage;
	public CommPanel(DevToolWindow driver)
	{
		myDriver = driver;
		// Set the look and feel of the Frame and catch any possible errors
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			System.err.println("Could not load look and feel - continuing");
		}
		setLayout(new FlowLayout(FlowLayout.LEADING, 2, 2));
		setPreferredSize(new Dimension(305, 400));
		// setBackground(Color.RED);
		add(makeTopPanel());
		add(makeMessagePanel());
	}
	
	private JPanel makeMessagePanel()
	{
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(235, 295));
		panel.setLayout(new FlowLayout(FlowLayout.LEADING, 2, 4));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
		// panel.setBackground(Color.BLACK);
		JLabel messageLabel = new JLabel("Messages");
		messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
		messageLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JPanel fillerPanel = new JPanel();
		fillerPanel.setPreferredSize(new Dimension(120, 10));
		messageArea = new JTextArea();
		messageArea.setFont(new Font("Arial", Font.BOLD, 13));
		messageArea.setRows(8);
		messageArea.setColumns(17);
		messageArea.setLineWrap(true);
		messageArea.setEditable(false);
		messageArea.setAutoscrolls(true);
		
		InputMap input = customMessage.getInputMap();
		KeyStroke key = KeyStroke.getKeyStroke("ENTER");
		
		input.put(key, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				sendAndClear();
				
			}

		});
		
		
		
		DefaultCaret caret = (DefaultCaret) messageArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		// add in scroll bars, set auto scroll
		JScrollPane sp = new JScrollPane(messageArea);
		sp.setAutoscrolls(true);
		sp.getVerticalScrollBar().addAdjustmentListener(
				new AdjustmentListener()
				{
					public void adjustmentValueChanged(AdjustmentEvent e)
					{
						e.getAdjustable().setValue(
								e.getAdjustable().getValue());
					}
				});
		sp.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		sp.setPreferredSize(new Dimension(225, 170));
		panel.add(messageLabel);
		panel.add(fillerPanel);
		panel.add(sp);
		return panel;
	}
	
	private JPanel makeTopPanel()
	{
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(235, 155));
		panel.setLayout(new FlowLayout(FlowLayout.LEADING, 4, 4));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
		JLabel commLabel = new JLabel("Communication");
		commLabel.setFont(new Font("Arial", Font.BOLD, 18));
		JPanel fillerPanel = new JPanel();
		fillerPanel.setPreferredSize(new Dimension(50, 10));
		
		JPanel responsePanel = new JPanel();
		responsePanel.setLayout(new FlowLayout(FlowLayout.LEADING, 4, 4));
		responsePanel.setPreferredSize(new Dimension(235, 100));
		// responsePanel.setBackground(Color.RED);
		JButton sendButton = new JButton("Send");
		sendButton.setFont(new Font("Arial", Font.PLAIN, 13));
		sendButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{	sendAndClear();
			}
		});
		sendButton.setPreferredSize(new Dimension(215, 30));
		myComps.add(sendButton);
		customMessage = new JTextArea();
		//customMessage
			//	.setDocument(new CustomDoc(customMessage, 100, this, true));
		JScrollPane customMessageScroller = new JScrollPane(customMessage);
		customMessageScroller.setPreferredSize(new Dimension(215, 60));
		myComps.add(customMessageScroller);
		customMessage.setFont(new Font("Arial", Font.PLAIN, 15));
		//customMessage.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,
		//		Color.black));
		customMessage.setPreferredSize(new Dimension(215, 100));
		customMessage.setLineWrap(true);
		myComps.add(customMessage);
		responsePanel.add(customMessageScroller);
		responsePanel.add(fillerPanel);
		responsePanel.add(sendButton);
		panel.add(commLabel);
		panel.add(fillerPanel);
		panel.add(responsePanel);
		// panel.add(customMessage);
		return panel;
	}
	
	private void sendAndClear()
	{
		myDriver.robotSim.setResponseMessage(customMessage.getText());
		myDriver.robotSim.simulateResponse(customMessage.getText());
		customMessage.setText("");
	}
	
	public void disableAll()
	{
		myDriver.disableAll();
		for (Component c : myComps)
		{
			c.setEnabled(false);
		}
	}
	
	public void enableAll()
	{
		myDriver.enableAll();
		for (Component c : myComps)
		{
			c.setEnabled(true);
		}
	}
	
	public String generateCommand()
	{
		String command = "";
		int choice = (int) (Math.random() * 6);
		switch (choice)
		{
			case 0: // connected
				command = "{0connected|";
			break;
			case 1: // received
				command = "{1received&message:You win|";
			break;
			case 2: // success
				command = "{0success&message:congrats|";
			break;
			case 3: // failure
				command = "{1failure&message:you suck|";
			break;
			case 4: // data
				command = "{0data&distance:1&light:60&sound:20&touch:0&claw:.75&heading:90&speed:2|";
			break;
			case 5: // error
				command = "{1error:didn't work|";
			break;
		}
		
		command += Message.genCheckSum(command) + "}";
		
		messageArea.append(command + "\n");
		messageArea.setCaretPosition(messageArea.getDocument().getLength());
		return command;
	}
	
	public void updateDataCommand(int myTag, String text)
	{
		mySensors[myTag] = text;
		myDriver.setCommand(updateDataText());
	}
	
	private String updateDataText()
	{
		String dataString = "{data&distance:" + mySensors[1] + "&light:"
				+ mySensors[0] + "&sound:" + mySensors[2] + "&touch:"
				+ mySensors[3] + "&claw:" + mySensors[4] + "&heading:"
				+ mySensors[5] + "&speed:" + mySensors[6];
		messageArea.append(dataString + "\n");
		messageArea.setCaretPosition(messageArea.getDocument().getLength());
		return dataString;
	}
	
	public void updateCustomCommand(String text)
	{
		myDriver.setCommand(updateCustomText(text));
	}
	
	private String updateCustomText(String text)
	{
		messageArea.append(text + "\n");
		// messageArea.setCaretPosition(messageArea.getDocument().getLength());
		return text;
	}
	
	public void validateCommand(String myReceivedCommand)
	{
		boolean valid = false;
		String[] firstSplit = myReceivedCommand.split("&");
		if (firstSplit.length == 1)
		{
			if (firstSplit[0].equals("{init}")
					|| firstSplit[0].equals("{stop}")
					|| firstSplit[0].equals("{query}")
					|| firstSplit[0].equals("{terminate}"))
			{
				valid = true;
			}
		}
		else if (firstSplit[0].equals("{move") || firstSplit[0].equals("{turn")
				|| firstSplit[0].equals("{claw"))
		{
			String[] split2 = firstSplit[1].split(":");
			if (split2.length > 1)
			{
				if (firstSplit[0].equals("move") && split2[0].equals("speed")
						&& split2[1].charAt(split2[1].length()) == '}')
				{
					try
					{
						Integer temp = new Integer(split2[1].substring(0,
								split2[1].length() - 1));
						if (temp >= -720 && temp <= 720)
						{
							valid = true;
						}
					}
					catch (NumberFormatException e)
					{
						valid = false;
					}
				}
				else if (firstSplit[0].equals("turn")
						&& split2[0].equals("heading")
						&& split2[1].charAt(split2[1].length()) == '}')
				{
					try
					{
						Integer temp = new Integer(split2[1].substring(0,
								split2[1].length() - 1));
						if (temp >= -180 && temp <= 180)
						{
							valid = true;
						}
					}
					catch (NumberFormatException e)
					{
						valid = false;
					}
				}
				else if (firstSplit[0].equals("claw")
						&& split2[0].equals("position")
						&& split2[1].charAt(split2[1].length()) == '}')
				{
					try
					{
						Float temp = new Float(split2[1].substring(0,
								split2[1].length() - 1));
						if (temp >= 0.0 && temp <= 1.0)
						{
							valid = true;
						}
					}
					catch (NumberFormatException e)
					{
						valid = false;
					}
				}
			}
		}
		if (valid)
		{
			messageArea.append("VALID COMMAND RECEIVED \n");
			// messageArea.setCaretPosition(messageArea.getDocument().getLength());
		}
		else
		{
			messageArea.append("INVALID COMMAND RECEIVED: " + myReceivedCommand
					+ "\n");
			// messageArea.setCaretPosition(messageArea.getDocument().getLength());
		}
	}
	
	public ArrayList<Component> getMyComps() {
		return myComps;
	}

	public void setMyComps(ArrayList<Component> myComps) {
		this.myComps = myComps;
	}

	public String[] getResponses() {
		return responses;
	}

	public void setResponses(String[] responses) {
		this.responses = responses;
	}

	public DevToolWindow getMyDriver() {
		return myDriver;
	}

	public void setMyDriver(DevToolWindow myDriver) {
		this.myDriver = myDriver;
	}

	public JTextArea getMessageArea() {
		return messageArea;
	}

	public void setMessageArea(JTextArea messageArea) {
		this.messageArea = messageArea;
	}

	public String[] getMySensors() {
		return mySensors;
	}

	public void setMySensors(String[] mySensors) {
		this.mySensors = mySensors;
	}

	public JTextArea getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage(JTextArea customMessage) {
		this.customMessage = customMessage;
	}

	public void setOutputText(String string)
	{
		messageArea.append(string + "\n");
		// messageArea.setCaretPosition(messageArea.getDocument().getLength());
	}
}
