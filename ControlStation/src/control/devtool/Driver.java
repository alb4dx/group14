package control.devtool;

import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import control.test.TestComm;
import control.test.TestToolMessageListener;


/**
 * A class to initialize the main frame and all associated properties.
 * 
 * @author Steph
 */
public class Driver
{
	private JFrame			myFrame;
	private CommPanel		myComm;
	private TelemetryPanel	myTelem;
	@SuppressWarnings("unused")
	private String			myRobotCommand;	// command randomly generated
	private String			myReceivedCommand;
	
	private TestComm testComm = new TestComm();
	private TestToolMessageListener lis = new TestToolMessageListener(testComm);
	
	@SuppressWarnings("unused")
	/**
	 * The runnable part of the program -creates the Driver, which creates
	 * every other part of the GUI
	 * 
	 * @paramargs command line parameters
	 */
	public static void main(String[] args)
	{
		Driver driver = new Driver();
		(new Thread(driver.lis)).start();
	}
	
	/**
	 * The Driver constructor -takes no arguments, but sets up SudokuPane and
	 * ButtonPane, as well as links them together in one JFrame.
	 */
	public Driver()
	{
		// Set the look and feel of the Frame and catch any possible errors
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			System.out.println("Could not load look and feel - continuing");
		}
		// Setting main frame properties
		myFrame = new JFrame("Development Tool - Testing");
		myFrame.setSize(500, 405);
		myFrame.setLocation(500, 100);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel myContent = new JPanel();
		myContent.setLayout(new GridLayout(1, 2, 15, 5));
		myComm = new CommPanel(this);
		myTelem = new TelemetryPanel(this);
		myContent.add(myComm);
		myContent.add(myTelem);
		// Set content pane, make visible, and prevent resizing
		myFrame.setContentPane(myContent);
		myFrame.setVisible(true);
		myFrame.setResizable(false);
		myComm.setOutputText("Testing received commands (see text file)");
		try
		{
			Scanner scanner = new Scanner(new File("testcommands.txt"));
			while (scanner.hasNext())
			{
				myReceivedCommand = scanner.next();
				validateCommand();
			}
		}
		catch (FileNotFoundException e)
		{
			myComm.setOutputText("Test commands file not found");
		}
	}
	
	/**
	 * Returns the frame initialized in this Driver
	 * 
	 * @returnmyFrame
	 */
	public JFrame getFrame()
	{
		return myFrame;
	}
	
	public CommPanel getComm()
	{
		return myComm;
	}
	
	public void disableAll()
	{
		myTelem.disableAll();
	}
	
	public void enableAll()
	{
		myTelem.enableAll();
	}
	
	public void setCommand(String generateCommand)
	{
		myRobotCommand = generateCommand;
		testComm.simulateResponse(generateCommand);
	}
	
	public void validateCommand()
	{
		myComm.validateCommand(myReceivedCommand);
	}
}