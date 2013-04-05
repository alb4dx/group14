package control.devtool;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


@SuppressWarnings("serial")
public class TelemetryPanel extends JPanel
{
	ArrayList<Component>	myComps		= new ArrayList<Component>();
	private Driver			myDriver;
	String[]				mySensors	=
										{ "Light", "Ultrasonic", "Sound",
			"Touch", "Claw position", "Orientation", "Location" };
	
	public TelemetryPanel(Driver driver)
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
		setLayout(new GridLayout(0, 1, 15, 15));
		setPreferredSize(new Dimension(255, 400));
		// setBackground(Color.RED);
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		JLabel commLabel = new JLabel("Simulated Telemetry");
		commLabel.setFont(new Font("Arial", Font.BOLD, 18));
		add(commLabel);
		for (int x = 0; x < mySensors.length; x++)
		{
			add(makeSensorPanel(mySensors[x], x));
			
		}
	}
	
	private JPanel makeSensorPanel(String string, int x)
	{
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(255, 10));
		// panel.setBackground(Color.pink);
		panel.setLayout(new GridLayout(1, 0, 2, 2));
		JLabel myLabel = new JLabel(string);
		myLabel.setFont(new Font("Arial", Font.ITALIC, 15));
		myComps.add(myLabel);
		JTextPane myText = new JTextPane();
		myText.setDocument(new CustomDoc(myText, x, myDriver.getComm(), false));
		myText.setFont(new Font("Arial", Font.PLAIN, 12));
		myText.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,
				Color.black));
		myText.setText("0");
		StyledDocument doc = (StyledDocument) myText.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		((CustomDoc) myText.getDocument()).changeCommand();
		myComps.add(myText);
		panel.add(myLabel);
		panel.add(myText);
		return panel;
	}
	
	public void disableAll()
	{
		for (Component c : myComps)
		{
			c.setEnabled(false);
		}
	}
	
	public void enableAll()
	{
		for (Component c : myComps)
		{
			c.setEnabled(true);
		}
	}
}