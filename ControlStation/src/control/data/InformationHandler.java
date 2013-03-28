package control.data;

import java.util.List;

import javax.swing.JPanel;
/**
 * Used to hold all of the data received from the robot and does any necessary calculations
 * @version 1.0 - Build 04/01/2013
 * @author Stephanie Colen
 * @author Sarina Padilla
 * @author Hubert Chen
 * @author Andy Barron
 * @author John Zambrotta
 *
 */
public class InformationHandler
{
	private int distance;
	private float claw;
	private int heading;
	private int speed;
	private JPanel graph;
	private DataPoint currentData;
	private List<DataPoint> dataList;
	
	/**
	 * The constructor for InformationHandler: created upon an
	 * initialization of a GraphicsInterface object: 
	 */
	public InformationHandler()
	{
		//TODO
	}
	
	/**
	 * Graph update method
	 * @return TBD
	 */
	public JPanel updateGraph()
	{
		//TODO
		return null;
	}
	
	/**
	 * adds all telemetry sensor data to information handler
	 * @param light		light sensor data
	 * @param sound 	sound sensor data
	 * @param ultrasonic	ultrasonic sensor data
	 * @param touch		touch sensor data 
	 */
	public void addData(int light, int sound, int ultrasonic, boolean touch)
	{
		//TODO
	}
}
