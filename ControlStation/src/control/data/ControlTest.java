package control.data;
import javax.swing.*;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class ControlTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		// create InformationHandler test data
		InformationHandler testHandler=new InformationHandler();
		testHandler.addData(1,50,100,true); //test data point 1
		testHandler.addData(2,20,82,false); //2
		testHandler.addData(10,25,50,true); //3
		testHandler.addData(50,80,45,false); //4
		testHandler.addData(10,100,1,true); //5
		testHandler.addData(250,300,200,false); //6
		testHandler.addData(300,250,200,false); //7
		testHandler.addData(300,250,350,false); //8
		testHandler.addData(50,400,500,false); //9
		testHandler.addData(500,300,200,true); //10
		//testHandler.addData(200,200,200,true); //11 - to check for Array overflow update
		//testHandler.addData(200,200,200,false); //12 - ...
		
		//Create test frame
		ChartPanel testPanel = (ChartPanel) testHandler.updateGraph();
		//testPanel=testHandler.getGraph();
		
		//Create test Frame
		JFrame testFrame=new JFrame(); // Frame to show testPanels
		testFrame.setVisible(true);
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.setSize(450, 200);
		testFrame.add(testPanel);
		
		
		
		
	}

}
