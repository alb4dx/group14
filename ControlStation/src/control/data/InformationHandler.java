package control.data;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.*;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import java.awt.Color;

/**
 * Used to hold all of the data received from the robot and does any necessary
 * calculations
 * 
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
	/** The distance the robot has traveled from it's initial position */
	private int distance;
	/** The most recent claw reading */
	private int claw;
	/** The most recent heading calculation */
	private int heading;
	/** The speed the robot is currently moving at */
	private int speed;
	/** The graph displayed from the most recently read data */
	private JFreeChart graph;
	/** The most recently read data point */
	private DataPoint currentData;
	/** A list of data points to be drawn on the graph */
	private List<DataPoint> dataList;
	/** The chart panel used to display the graph */
	private ChartPanel panel;
	
	/** The series of light points to graph */
	private XYSeries lightSeries = new XYSeries("Light");
	/** The series of sound points to graph */
	private XYSeries soundSeries = new XYSeries("Sound");
	/** The series of ultrasonic points to graph */
	private XYSeries uSonicSeries = new XYSeries("Ultrasonic");
	/** The series of touch points to graph */
	private XYSeries touchSeries = new XYSeries("Touch");
	
	/** The series of all points to graph */
	private XYSeriesCollection graphDataCollection = new XYSeriesCollection();
	
	/**
	 * The constructor for InformationHandler: created upon an
	 * initialization of a GraphicsInterface object:
	 * Creates an empty InformationHandler object
	 */
	public InformationHandler()
	{
		distance = 0;
		claw = 0;
		heading = 0;
		speed = 0;
		currentData = null;
		dataList = new LinkedList<DataPoint>();
		initGraph();
		panel = new ChartPanel(graph);
	}
	
	/**
	 * Get method for Distance
	 * 
	 * @return Robot integer distance traveled
	 */
	public int getDistance()
	{
		return distance;
	}
	
	/**
	 * Get method for Claw data
	 * 
	 * @return claw float value
	 */
	public float getClaw()
	{
		return claw;
	}
	
	/**
	 * Get method for heading data
	 * 
	 * @return Integer value for heading
	 */
	public int getHeading()
	{
		return heading;
	}
	
	/**
	 * Get method for speed data
	 * 
	 * @return Integer value for speed
	 */
	public int getSpeed()
	{
		return speed;
	}
	
	/**
	 * Get method for telemetry data graph
	 * 
	 * @return JPanel of current graph
	 */
	public JFreeChart getGraph(){

		return graph;
	}
	
	/**
	 * Get method for current DataPoint
	 * 
	 * @return DataPoint object of current telemetry data
	 */
	public DataPoint getCurrData()
	{
		return currentData;
	}
	
	/**
	 * Get method for DataPoint list
	 * 
	 * @return ArrayList of DataPoints
	 */
	public List<DataPoint> getDataList()
	{
		return dataList;
	}
	
	/**
	 * Set method for distance
	 * 
	 * @param d integer distance value to set
	 */
	public void setDistance(int d)
	{
		this.distance = d;
	}
	
	/**
	 * Set method for Claw
	 * 
	 * @param c float value for claw to set
	 */
	public void setClaw(int c)
	{
		this.claw = c;
	}
	
	/**
	 * Set method for heading
	 * 
	 * @param h integer value for heading to set
	 */
	public void setHeading(int h)
	{
		this.heading = h;
	}
	
	/**
	 * Set method for speed
	 * 
	 * @param s integer value for speed to set
	 */
	public void setSpeed(int s)
	{
		this.speed = s;
	}
	
	/**
	 * Set method for graph
	 * 
	 * @param j Jpanel object of graph to set
	 */
	public void setGraph(JFreeChart j){
		this.graph=j;

	}
	
	/**
	 * Set method for current DataPoint
	 * 
	 * @param d DataPoint object of current telemetry data to set
	 */
	public void setCurrData(DataPoint d)
	{
		this.currentData = d;
	}
	
	/**
	 * Set method for ArrayList of DataPoints
	 * 
	 * @param list ArrayList of DataPoints to set
	 */
	public void setDataList(ArrayList<DataPoint> list)
	{
		this.dataList = list;
	}
	
	/**
	 * Returns the chart panel that displays the graph
	 * 
	 * @return panel the previously defined field
	 */
	public ChartPanel getPanel()
	{
		return panel;
	}

	/**
	 * Graph update method
	 * 
	 * @return Updated JPanel based on new telemetry data
	 */
	public void updateGraph()
	{
		updateAllSubSeries();
		
		System.out.println("Data list size: " + dataList.size());
		System.out.println("Light series size: " + lightSeries.getItemCount());

	}
	
	/**
	 * Adds telemetry sensor data to information handler object
	 * 
	 * @param light light sensor data
	 * @param sound sound sensor data
	 * @param ultrasonic ultrasonic sensor data
	 * @param touch touch sensor data
	 */
	public void addData(int light, int sound, int ultrasonic, boolean touch)
	{
		currentData = new DataPoint(light, sound, ultrasonic, touch);
		if (dataList.size() > 10) {
			dataList.remove(0);
		}
		dataList.add(currentData);
		
	}

	/**
	 * Updates the individual series with the data stored in the most current DataPoint
	 */
	private void updateAllSubSeries()
	{
		lightSeries.clear();
		soundSeries.clear();
		uSonicSeries.clear();
		touchSeries.clear();
		
		int i = 0;
		for(DataPoint d : dataList)
		{
			lightSeries.add(i,d.getLight());
			soundSeries.add(i,d.getSound());
			uSonicSeries.add(i,d.getUltrasonic());
			touchSeries.add(i,d.getTouch());
			i++;
		}
	}
	
	/**
	 * Adds the initial data to the graph and customizes the graph for optimal display.
	 */
	private void initGraph(){

		graphDataCollection.addSeries(lightSeries);
		graphDataCollection.addSeries(soundSeries);
		graphDataCollection.addSeries(uSonicSeries);
		graphDataCollection.addSeries(touchSeries);
		
		graph = ChartFactory.createXYLineChart(
				"Telemetry Log",	// chart title
				"Data Point",			// x axis label
				"Sensor Reading",			// y axis label
				graphDataCollection,		// data
				PlotOrientation.VERTICAL,
				true,			// include legend
				true,			// tool tips
				false			// urls
		);
		
		graph.setBackgroundPaint(Color.WHITE);

		XYPlot plot = (XYPlot) graph.getXYPlot();
		plot.setBackgroundPaint(Color.LIGHT_GRAY);
		plot.setDomainGridlinePaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.WHITE);
		
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		plot.setRenderer(renderer);
		
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

	}
}
