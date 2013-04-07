package control.data;

import org.jfree.data.general.Dataset;
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
import java.util.Queue;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.color.*;
import javax.swing.JPanel;

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
	private int distance;
	private float claw;
	private int heading;
	private int speed;
	private JFreeChart graph;
	private DataPoint currentData;
	private List<DataPoint> dataList;
	private ChartPanel panel;
	
	private XYSeries lightSeries = new XYSeries("Light");
	private XYSeries soundSeries = new XYSeries("Sound");
	private XYSeries uSonicSeries = new XYSeries("Ultrasonic");
	private XYSeries touchSeries = new XYSeries("Touch");
	
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
	 * @param d
	 *            integer distance value to set
	 */
	public void setDistance(int d)
	{
		this.distance = d;
	}
	
	/**
	 * Set method for Claw
	 * 
	 * @param c
	 *            float value for claw to set
	 */
	public void setClaw(float c)
	{
		this.claw = c;
	}
	
	/**
	 * Set method for heading
	 * 
	 * @param h
	 *            integer value for heading to set
	 */
	public void setHeading(int h)
	{
		this.heading = h;
	}
	
	/**
	 * Set method for speed
	 * 
	 * @param s
	 *            integer value for speed to set
	 */
	public void setSpeed(int s)
	{
		this.speed = s;
	}
	
	/**
	 * Set method for graph
	 * 
	 * @param j
	 *            Jpanel object of graph to set
	 */
	public void setGraph(JFreeChart j){
		this.graph=j;

	}
	
	/**
	 * Set method for current DataPoint
	 * 
	 * @param d
	 *            DataPoint object of current telemetry data to set
	 */
	public void setCurrData(DataPoint d)
	{
		this.currentData = d;
	}
	
	/**
	 * Set method for ArrayList of DataPoints
	 * 
	 * @param list
	 *            ArrayList of DataPoints to set
	 */
	public void setDataList(ArrayList<DataPoint> list)
	{
		this.dataList = list;
	}
	
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

		//setContentPane(chartPanel);

		
		// Y axis - should update if a data value exceeds the max
		// default == 100
		
		// X axis - verticals for 1 - 10
		// Under axis is T or F if true or false
		
		// Key - shows which each colored line means as well as T/Fs
		
		
		
		updateAllSubSeries();
		
		System.out.println("Data list size: " + dataList.size());
		System.out.println("Light series size: " + lightSeries.getItemCount());
		
		//return panel;
	}
	
	/**
	 * Adds telemetry sensor data to information handler ojbect
	 * 
	 * @param light
	 *            light sensor data
	 * @param sound
	 *            sound sensor data
	 * @param ultrasonic
	 *            ultrasonic sensor data
	 * @param touch
	 *            touch sensor data
	 */
	public void addData(int light, int sound, int ultrasonic, boolean touch)
	{
		currentData = new DataPoint(light, sound, ultrasonic, touch);
		
		if (dataList.size() > 10) dataList.remove(0);
		
		dataList.add(currentData);
		
	}// end addData function
	
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
		
		// Chart Customization
		
		graph.setBackgroundPaint(Color.WHITE);
		//StandardLegend legend = (StandardLegend) chart.getLegend();
		//legend.setDisplaySeriesShapes(true);

		
		XYPlot plot = (XYPlot) graph.getXYPlot();
		plot.setBackgroundPaint(Color.LIGHT_GRAY);
		// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0,5.0,5.0,5.0);
		plot.setDomainGridlinePaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.WHITE);
		
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		plot.setRenderer(renderer);
		
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

	}
}// end Information Handler class
