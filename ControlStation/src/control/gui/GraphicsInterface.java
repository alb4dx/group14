package control.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import control.data.InformationHandler;

/**
 * A class that creates the GUI that displays telemetry data read 
 * and communicated from the robot. GraphicsInterface is initialized
 * from Controller and has an instance of InformationHandler to do
 * any data manipulation before displayed. All other private fields
 * are GUI components.
 * 
 * This class has one method, called extract(), that extracts the 
 * data from the InformationHandler instance and displays the new 
 * graph and orientation/heading information by updating the GUI.
 * 
 * @author Steph
 *
 */
public class GraphicsInterface
{
	private JFrame				myFrame;			// The frame encompassing the whole GUI
	private JPanel				dataGraph;			// The graph displaying past and present telemetry data
	private JLabel				errorLabel;			// The label for the error section heading
	private JLabel				locationLabel;		// The label for the location section heading
	private JLabel				orientationLabel;  	// The label for the orientation section heading
	private JTextArea			errorText;			// The JTextArea displaying the error responses from the robot
	private JTextArea			locationText;		// The JTextArea displaying the location of the robot relative to its starting position
	private JTextArea			orientationText;	// The JTextArea displaying the orientation of the robot relative to its starting position
	private InformationHandler	myInfo;
	
	/**
	 * The constructor for the GraphicsInterface object: it is 
	 * called in Controller upon initialization and instantiates
	 * all the GUI components and displays myFrame to the user.
	 */
	public GraphicsInterface() {
		
	}
	
	/**
	 * A method that extracts the data from the InformationHandler 
	 * instance called myInfo and displays the new graph and 
	 * orientation/heading information by updating the GUI through 
	 * the various global GUI components defined above.
	 * 
	 * extract() is called from the Controller whenever new data 
	 * is received in the Controller from MessageListener.
	 */
	public void extract()
	{
		// TODO
	}
}
