package control.data;

import java.util.List;
import java.util.ArrayList;
import java.awt.color.*;
import javax.swing.JPanel;

import lejos.robotics.Color;
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
	private ArrayList<DataPoint> dataList;
	
	/**
	 * The constructor for InformationHandler: created upon an
	 * initialization of a GraphicsInterface object: 
	 * Creates an empty InformationHandler object
	 */
	public InformationHandler()
	{
		distance=0;
		claw=0;
		heading=0;
		speed=0;
		graph=new JPanel();
		Color bg=new Color(190,190,190); // turn 190 into constants for r, g, b?
		//graph.setBackground(bg)
		currentData=null;
		dataList=new ArrayList<DataPoint>();
		
	}
	/**
	 * Get method for Distance
	 * @return Robot integer distance traveled
	 */
	public int getDistance(){
		return distance;
	}
	/**
	 * Get method for Claw data
	 * @return claw float value
	 */
	public float getClaw(){
		return claw;
	}
	/**
	 * Get method for heading data
	 * @return Integer value for heading
	 */
	public int getHeading(){
		return heading;
	}
	/**
	 * Get method for speed data
	 * @return	Integer value for speed
	 */
	public int getSpeed(){
		return speed;
	}
	/**
	 * Get method for telemetry data graph
	 * @return JPanel of current graph
	 */
	public JPanel getGraph(){
		return graph;
	}
	/**
	 * Get method for current DataPoint
	 * @return DataPoint object of current telemetry data
	 */
	public DataPoint getCurrData(){
		return currentData;
	}
	/**
	 * Get method for DataPoint list
	 * @return ArrayList of DataPoints
	 */
	public ArrayList<DataPoint>getDataList(){
		return dataList;
	}
	/**
	 * Set method for distance
	 * @param d		integer distance value to set
	 */
	public void setDistance(int d){
		this.distance=d;
	}
	/**
	 * Set method for Claw
	 * @param c		float value for claw to set
	 */
	public void setClaw(float c){
		this.claw=c;
	}
	/**
	 * Set method for heading
	 * @param h		integer value for heading to set
	 */
	public void setHeading(int h){
		this.heading=h;
	}
	/**
	 * Set method for speed
	 * @param s		integer value for speed to set
	 */
	public void setSpeed(int s){
		this.speed=s;
	}
	/**
	 * Set method for graph 
	 * @param j		Jpanel object of graph to set
	 */
	public void setGraph(JPanel j){
		this.graph=j;
	}
	/**
	 * Set method for current DataPoint
	 * @param d		DataPoint object of current telemetry data to set
	 */
	public void setCurrData(DataPoint d){
		this.currentData=d;
	}
	/**
	 * Set method for ArrayList of DataPoints
	 * @param list		ArrayList of DataPoints to set
	 */
	public void setDataList(ArrayList<DataPoint> list){
		this.dataList=list;
	}
	/**
	 * Graph update method
	 * @return Updated JPanel based on new telemetry data
	 */
	public JPanel updateGraph()
	{
		//Graph should have data for every XX amount of time (check spec docs) 
		//for each telemetry type and put them 
		
		//Y axis - should update if a data value exceeds the max
		//		default == 100
		
		//X axis - verticals for 1 - 10
		//		Under axis is T or F if true or false
		
		//Key    - shows which each colored line means as well as T/Fs 
		
		//if(
		//spot 1 - oldest data - dataList[1]
		//spot 2
		//spot 3
		//spot 4
		//spot 5
		//spot 6
		//spot 7
		//spot 8
		//spot 9
		//spot 10 - newest data - dataList[10]
		
		return graph;
	}
	
	/**
	 * Adds  telemetry sensor data to information handler ojbect
	 * @param light		light sensor data
	 * @param sound 	sound sensor data
	 * @param ultrasonic	ultrasonic sensor data
	 * @param touch		touch sensor data 
	 */
	public void addData(int light, int sound, int ultrasonic, boolean touch)
	{
		currentData=new DataPoint(light,sound,ultrasonic,touch);
		
		if(dataList.size()>10)
			dataList.remove(0);
	
		dataList.add(currentData);
		
	}// end addData function
}
