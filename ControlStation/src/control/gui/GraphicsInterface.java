package control.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import org.jfree.chart.ChartPanel;

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
 * testing 32 bit eclipse!s
 * 
 * John was here
 *
 */
public class GraphicsInterface
{
	public JFrame getMyFrame() {
		return myFrame;
	}

	public void setMyFrame(JFrame myFrame) {
		this.myFrame = myFrame;
	}

	public JPanel getDataGraph() {
		return dataGraph;
	}

	public void setDataGraph(JPanel dataGraph) {
		this.dataGraph = dataGraph;
	}

	public JLabel getErrorLabel() {
		return errorLabel;
	}

	public void setErrorLabel(JLabel errorLabel) {
		this.errorLabel = errorLabel;
	}

	public JLabel getLocationLabel() {
		return locationLabel;
	}

	public void setLocationLabel(JLabel locationLabel) {
		this.locationLabel = locationLabel;
	}

	public JLabel getOrientationLabel() {
		return orientationLabel;
	}

	public void setOrientationLabel(JLabel orientationLabel) {
		this.orientationLabel = orientationLabel;
	}

	public JTextArea getErrorText() {
		return errorText;
	}

	public void setErrorText(JTextArea errorText) {
		this.errorText = errorText;
	}

	public JTextArea getLocationText() {
		return locationText;
	}

	public void setLocationText(JTextArea locationText) {
		this.locationText = locationText;
	}

	public JTextArea getOrientationText() {
		return orientationText;
	}

	public void setOrientationText(JTextArea orientationText) {
		this.orientationText = orientationText;
	}

	public InformationHandler getMyInfo() {
		return myInfo;
	}

	public void setMyInfo(InformationHandler myInfo) {
		this.myInfo = myInfo;
	}

	private JFrame				myFrame;			// The frame encompassing the whole GUI
	private ChartPanel			dataGraph;			// The graph displaying past and present telemetry data
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
		
		myInfo = new InformationHandler();
		
		myFrame = new JFrame("Robot Control Station");
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setSize(500, 450);
		myFrame.setLocation(100, 100);
		 try {
	        UIManager.setLookAndFeel(
	            UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (UnsupportedLookAndFeelException e) { }
	    catch (ClassNotFoundException e) { }
	    catch (InstantiationException e) { }
	    catch (IllegalAccessException e) { }
		
		JPanel content = new JPanel();
		
		content.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
		content.setPreferredSize(new Dimension(600, 500));
		 
		dataGraph = (ChartPanel) myInfo.updateGraph();
		dataGraph.setPreferredSize(new Dimension(450, 200));
		//dataGraph.setBackground(Color.blue);
	
		errorLabel = new JLabel("Error Log:");
		errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
		
		JPanel errorPanel = new JPanel();
		errorPanel.setPreferredSize(new Dimension(450, 200));
		errorPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 10));
		
		errorText = new JTextArea();
		errorText.setFont(new Font("Arial", Font.PLAIN, 13));
		errorText.setRows(5);
		errorText.setColumns(15);
		errorText.setLineWrap(true);
		errorText.setEditable(false);
		errorText.setPreferredSize(new Dimension(450, 50));
		errorText.setAutoscrolls(true);
		errorText.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
		// add in scroll bars, set auto scroll
		JScrollPane sp = new JScrollPane(errorText);
		sp.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
		sp.setPreferredSize(new Dimension(450, 50));
		
		errorPanel.add(errorLabel);
		errorPanel.add(errorText);
		
		JPanel locPanel = new JPanel();
		locPanel.setPreferredSize(new Dimension(450, 80));
		locPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		
		locationLabel = new JLabel("Location:  ");
		locationLabel.setFont(new Font("Arial", Font.BOLD, 14));
		
		locationText = new JTextArea();
		locationText.setFont(new Font("Arial", Font.PLAIN, 13));
		locationText.setRows(1);
		locationText.setColumns(5);
		locationText.setLineWrap(true);
		locationText.setEditable(false);
		locationText.setPreferredSize(new Dimension(50, 30));
		locationText.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
		
		locPanel.add(locationLabel);
		locPanel.add(locationText);
		
		JPanel filler = new JPanel();
		filler.setPreferredSize(new Dimension(168, 30));
		locPanel.add(filler);
		
		orientationLabel = new JLabel("Orientation:  ");
		orientationLabel.setFont(new Font("Arial", Font.BOLD, 14));
		
		orientationText = new JTextArea();
		orientationText.setFont(new Font("Arial", Font.PLAIN, 13));
		orientationText.setRows(1);
		orientationText.setColumns(5);
		orientationText.setLineWrap(true);
		orientationText.setEditable(false);
		orientationText.setPreferredSize(new Dimension(50, 30));
		orientationText.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
		
		locPanel.add(orientationLabel);
		locPanel.add(orientationText);
		
		errorPanel.add(locPanel);
		
		content.add(dataGraph);
		content.add(errorPanel);
		
		myFrame.setContentPane(content);
		myFrame.setVisible(true);
		
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
		this.dataGraph = this.myInfo.updateGraph();
		this.locationText.setText(Integer.toString(this.myInfo.getDistance()));
		int heading = this.myInfo.getHeading();
		String direction="";
		switch(heading){
		case 0: direction = "N";
				break;
		case 30: direction = "NE";
				break;
		case 60: direction = "NE";
				break;
		case 90: direction = "E";
				break;
		case 120: direction = "SE";
				break;
		case 150: direction = "SE";
				break;
		case 180: direction = "S";
				break;
		case -30: direction = "NW";
				break;
		case -60: direction = "NW";
				break;
		case -90: direction = "W";
				break;
		case -120: direction = "SW";
				break;
		case -150: direction = "SW";
				break;
		case -180: direction = "S";
				break;
		}
		this.locationText.setText(direction);
		this.myFrame.invalidate();
		this.myFrame.validate();
		this.myFrame.repaint();
	}
}
