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

import java.util.List;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.color.*;
import javax.swing.JPanel;

import java.awt.Color;
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
	private JFreeChart graph;
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
		graph=null;
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
	public JFreeChart getGraph(){
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
	public void setGraph(JFreeChart j){
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
		XYDataset dataset=new XYSeriesCollection();
		
		//setContentPane(chartPanel);
		
		//Y axis - should update if a data value exceeds the max
		//		default == 100
		
		//X axis - verticals for 1 - 10
		//		Under axis is T or F if true or false
		
		//Key    - shows which each colored line means as well as T/Fs 
		
		((XYSeriesCollection) dataset).addSeries(this.createLightSeries());
		((XYSeriesCollection) dataset).addSeries(this.createSoundSeries());
		((XYSeriesCollection) dataset).addSeries(this.createUltrasonicSeries());
		((XYSeriesCollection) dataset).addSeries(this.createTouchSeries());
		
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(450, 200)); 
		
		chart = createChart(dataset);
		
		return chartPanel;
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
	
	//Create XY series of Light data
	private XYSeries createLightSeries(){
		
		XYSeries lightSeries = new XYSeries("Light");
		int first=dataList.size() - 1;
		
		for(int i=first;i>0;i--){
			lightSeries.add(i,dataList.get(i).getLight());
		}
		return(lightSeries);
	}
	
	//Create XY series of sound data
	private XYSeries createSoundSeries(){
		
		XYSeries soundSeries = new XYSeries("Sound");
		int first = dataList.size() - 1;
		
		for(int i=first;i>0;i--){
			soundSeries.add(i,dataList.get(i).getSound());
		}
		return (soundSeries);
	}
	
	//Create XY series of Ultrasonic (US) data
	private XYSeries createUltrasonicSeries(){
		
		XYSeries uSonicSeries = new XYSeries("Ultrasonic");
		int first = dataList.size() - 1;
		
		for(int i=first;i>0;i--){
			uSonicSeries.add(i,dataList.get(i).getUltrasonic());
		}
		return(uSonicSeries);
	}
	
	private XYSeries createTouchSeries() {
		XYSeries touchSeries = new XYSeries("Touch");
		for(int i = dataList.size() - 1; i > 0; i--) {
			touchSeries.add(i, dataList.get(i).getTouch());
		}
		return (touchSeries);
	}
	
	private JFreeChart createChart(XYDataset dataset){
		
		graph = ChartFactory.createXYLineChart(
				"Telemtry Log",	// chart title
				"Data Point",			// x axis label
				"Sensor Reading",			// y axis label
				dataset,		// data
				PlotOrientation.VERTICAL,
				true,			// include legend
				true,			// tool tips
				false			// urls
				);
		
		//Chart Customization
		
		graph.setBackgroundPaint(Color.WHITE);
		//StandardLegend legend = (StandardLegend) chart.getLegend();
		//legend.setDisplaySeriesShapes(true);
		
		XYPlot plot = (XYPlot) graph.getXYPlot();
		plot.setBackgroundPaint(Color.LIGHT_GRAY);
		//plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0,5.0,5.0,5.0);
		plot.setDomainGridlinePaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.WHITE);
		
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		plot.setRenderer(renderer);
		
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		
		return graph;
	}
}// end Information Handler class
