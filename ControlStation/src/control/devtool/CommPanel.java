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
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultCaret;


@SuppressWarnings("serial")
public class CommPanel extends JPanel
{
	
	ArrayList<Component>	myComps		= new ArrayList<Component>();
	
	String[]				responses	=
										{ "Query", "Command received" };
	private Driver			myDriver;
	private JTextArea		messageArea;
	String[]				mySensors	=
										{ "0", "0", "0", "0", "0", "0", "0" };
	private JTextPane		customMessage;
	
	public CommPanel(Driver driver)
	{
		myDriver = driver;
		// Set the look and feel of the Frame and catch any possible errors
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Could not load look and feel - continuing");
		}
		catch (InstantiationException e)
		{
			System.out.println("Could not load look and feel - continuing");
		}
		catch (IllegalAccessException e)
		{
			System.out.println("Could not load look and feel - continuing");
		}
		catch (UnsupportedLookAndFeelException e)
		{
			System.out.println("Could not load look and feel - continuing");
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
								e.getAdjustable().getMaximum());
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
		JCheckBox randomBox = new JCheckBox("Random");
		randomBox.setFont(new Font("Arial", Font.PLAIN, 14));
		
		randomBox.addItemListener(new ItemListener()
		{
			private Timer	cTimer;
			
			public void itemStateChanged(ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.DESELECTED)
				{
					enableAll();
					cTimer.stop();
				}
				else
				{
					disableAll();
					ActionListener commandTimer = new ActionListener()
					{
						public void actionPerformed(ActionEvent arg0)
						{
							// generate new command, print it to messages
							myDriver.setCommand(generateCommand());
						}
					};
					cTimer = new Timer(3000, commandTimer);
					cTimer.setInitialDelay(0);
					cTimer.start();
				}
			}
		});
		JPanel responsePanel = new JPanel();
		responsePanel.setLayout(new FlowLayout(FlowLayout.LEADING, 4, 4));
		responsePanel.setPreferredSize(new Dimension(235, 100));
		// responsePanel.setBackground(Color.RED);
		JButton sendButton = new JButton("Send");
		sendButton.setFont(new Font("Arial", Font.PLAIN, 13));
		sendButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				updateCustomCommand(customMessage.getText());
				customMessage.setText("");
			}
		});
		sendButton.setPreferredSize(new Dimension(215, 30));
		myComps.add(sendButton);
		customMessage = new JTextPane();
		customMessage
				.setDocument(new CustomDoc(customMessage, 100, this, true));
		customMessage.setFont(new Font("Arial", Font.PLAIN, 15));
		customMessage.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,
				Color.black));
		customMessage.setPreferredSize(new Dimension(215, 25));
		myComps.add(customMessage);
		responsePanel.add(customMessage);
		responsePanel.add(fillerPanel);
		responsePanel.add(sendButton);
		panel.add(commLabel);
		panel.add(fillerPanel);
		panel.add(randomBox);
		panel.add(responsePanel);
		// panel.add(customMessage);
		return panel;
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
				command = "{connected}";
			break;
			case 1: // received
				command = "{received&message:You win}";
			break;
			case 2: // success
				command = "{success&message:congrats}";
			break;
			case 3: // failure
				command = "{failure&message:you suck}";
			break;
			case 4: // data
				command = "{data&distance:1&light:60&sound:20&touch:0&claw:.75&heading:90&speed:2}";
			break;
			case 5: // error
				command = "{error:didn't work}";
			break;
		}
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
	
	public void setOutputText(String string)
	{
		messageArea.append(string + "\n");
		// messageArea.setCaretPosition(messageArea.getDocument().getLength());
	}
}
