package control.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

import org.jfree.chart.ChartPanel;

import control.communication.Message;
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
	

	private JFrame				myFrame;			// The frame encompassing the whole GUI
	private ChartPanel			dataPanel;			// The graph displaying past and present telemetry data
	private JLabel				messageLabel;		// The label for the message section heading
	private JLabel				locationLabel;		// The label for the location section heading
	private JLabel				orientationLabel;  	// The label for the orientation section heading
	private JTextArea			messageText;		// The JTextArea displaying the messages sent and received
	private JTextArea			locationText;		// The JTextArea displaying the location of the robot relative to its starting position
	private JTextArea			orientationText;	// The JTextArea displaying the orientation of the robot relative to its starting position
	private InformationHandler	myInfo;
	private JLabel				speedLabel;
	private JTextArea			speedText;
	
	
	public JFrame getMyFrame() {
		return myFrame;
	}

	public void setMyFrame(JFrame myFrame) {
		this.myFrame = myFrame;
	}

	public JPanel getDataGraph() {
		return dataPanel;
	}

	public void setDataGraph(JPanel dataGraph) {
		this.dataPanel = (ChartPanel) dataGraph;
	}

	public JLabel getErrorLabel() {
		return messageLabel;
	}

	public void setErrorLabel(JLabel errorLabel) {
		this.messageLabel = errorLabel;
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

	public JTextArea getMessageText() {
		return messageText;
	}

	public void setMessageText(JTextArea errorText) {
		this.messageText = errorText;
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
	
	public void updateMessageLog(Message msg, boolean commandOrResponse){
		if(commandOrResponse){
			messageText.append("Message sent:"+msg.getMessageString()+'\n');
		}
		else{
			messageText.append("Message received:"+msg.getMessageString()+'\n');
		}
//		messageText.invalidate();
//		messageText.validate();
//		messageText.repaint();
	}
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
		 
		dataPanel = myInfo.getPanel();
		dataPanel.setPreferredSize(new Dimension(450, 200));
		//dataGraph.setBackground(Color.blue);
	
		messageLabel = new JLabel("Message Log:");
		messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
		
			JPanel panel = new JPanel();
			panel.setPreferredSize(new Dimension(450, 220));
			panel.setLayout(new FlowLayout(FlowLayout.LEADING, 2, 4));
			panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
			// panel.setBackground(Color.BLACK);
			JLabel messageLabel = new JLabel("Messages");
			messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
			messageLabel.setHorizontalAlignment(SwingConstants.LEFT);
			JPanel fillerPanel = new JPanel();
			fillerPanel.setPreferredSize(new Dimension(120, 10));
			messageText = new JTextArea();
			messageText.setFont(new Font("Arial", Font.BOLD, 13));
			messageText.setRows(5);
			messageText.setColumns(15);
			messageText.setLineWrap(true);
			messageText.setEditable(false);
			messageText.setAutoscrolls(true);
			
			DefaultCaret caret = (DefaultCaret) messageText.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			// add in scroll bars, set auto scroll
			JScrollPane sp = new JScrollPane(messageText);
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
			sp.setPreferredSize(new Dimension(450, 80));
			panel.add(messageLabel);
			panel.add(fillerPanel);
			panel.add(sp);
		
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
		filler.setPreferredSize(new Dimension(26, 20));
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
		
		JPanel filler1 = new JPanel();
		filler1.setPreferredSize(new Dimension(26, 20));
		locPanel.add(filler1);
		
		speedLabel = new JLabel("Speed:  ");
		speedLabel.setFont(new Font("Arial", Font.BOLD, 14));
		
		speedText = new JTextArea();
		speedText.setFont(new Font("Arial", Font.PLAIN, 13));
		speedText.setRows(1);
		speedText.setColumns(5);
		speedText.setLineWrap(true);
		speedText.setEditable(false);
		speedText.setPreferredSize(new Dimension(50, 30));
		speedText.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
		
		locPanel.add(speedLabel);
		locPanel.add(speedText);
		
		panel.add(locPanel);
		
		content.add(dataPanel);
		content.add(panel);
		
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
		// TODO graph does not update.
		System.out.println("Im in extract");
		myInfo.updateGraph();
		this.locationText.setText(Integer.toString(this.myInfo.getDistance()));
		this.speedText.setText(Integer.toString(this.myInfo.getSpeed()));
		int heading = this.myInfo.getHeading();
		String direction="";
		if(heading <= 15 && heading >= -15 ){
			direction = "N";
		}
		else if(heading < -15 && heading > -75){
			direction = "NW";
		}
		else if(heading <= -75 && heading >= -105){
			direction = "W";
		}
		else if(heading <= -105 && heading > -165){
			direction = "SW";
		}
		else if(heading <= -165 || heading >= 165){
			direction = "S";
		}
		else if(heading < 165 && heading > 105){
			direction = "SE";
		}
		else if(heading <= 105 && heading >= 75){
			direction = "E";
		}
		else if(heading < 75 && heading > 15){
			direction = "NE";
		}
		
		this.orientationText.setText(direction);
		this.myFrame.invalidate();
		this.myFrame.validate();
		this.myFrame.repaint();
		//this doesn't work:(
//		this.dataGraph.invalidate();
//		this.dataGraph.validate();
//		this.dataGraph.repaint();
	}
}
