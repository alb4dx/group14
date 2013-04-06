package control.devtool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import javax.swing.Timer;


import control.gui.GraphicsInterface;
import control.communication.*;
import control.communication.ResponseMessage.ResponseType;

public class RobotSimulator
{
	
	public final Object			lock		= new Object();
	public final Queue<Byte>	byteQueue	= new LinkedList<Byte>();
	public final DevNXTComm testNXTComm = new DevNXTComm(this);
	public final InputStream testInputStream = new DevNXTInputStream(this);
	public DevToolWindow myWindow = null;
	private Timer msgTimer;
	private String responseMessage;
	private RobotState myState;
	public RobotSimulator(DevToolWindow window){
		myWindow = window;
		ActionListener timeOut = new ActionListener()
		{
			public void actionPerformed(ActionEvent arg)
			{	
				System.out.println("Robot timeout occured");
				simulateResponse(responseMessage);
			}
		};
		msgTimer = new Timer(10000,timeOut);
		this.responseMessage= null;
		myState = RobotState.WAITCOMMAND;
	}
//	private final Scanner scan = new Scanner(System.in);
	
//	public static void main(String[] args)
//	{
//		new GraphicsInterface();
//		RobotSimulator r = new RobotSimulator();
//		r.launchCommandLine();
//	}
	
//	public void launchCommandLine()
//	{
//		System.out.println("Command line test tool now active.");
//		String input = new String();
//		do
//		{
//			System.out.println("Enter text to send from simulated robot, or \"exit\" to quit.");
//			input = scan.nextLine();
//			
//			
//			simulateResponse(input);
//			
//			
//		} while (!input.equals("exit"));
//		System.out.println("Toodles!");
//	}

	
	public Timer getMsgTimer() {
		return msgTimer;
	}

	public void setMsgTimer(Timer msgTimer) {
		this.msgTimer = msgTimer;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public void messageFromStation(String msg)
	{
		//TODO simulated robot behavior
		System.out.println(msg);
		// what type of message do we have?
		// what type of response do we send back?
		// compose command into a string
		// call simulateResponse(String response)
		myWindow.getComm().getMessageArea().append("\n"+"Message Received:"+msg);
		if(myState == RobotState.WAITCOMMAND){
			if(msgTimer.isRunning()){
				msgTimer.stop();	
			}
			System.out.println("here");
			if(!msg.substring(2, 5).equals("ack")){
				//System.out.println("not ack so robot state is"+RobotState.SENDINGRESPONSE1);
				myState = RobotState.SENDINGRESPONSE1;
			}
				
		}
	}
	
	public void simulateResponse(String response)
	{
		int sum=0;
		for(int i=0; i < response.length(); ++i){
			sum += response.charAt(i);
		}
		response ="{"+response + "|" + ~sum+"}";
		ResponseMessage msg = ResponseMessage.parse(response);
		myWindow.getComm().getMessageArea().append("\n"+"Message Sent:"+msg.getFormattedMessage());
		if(myState == RobotState.SENDINGRESPONSE1){
			//System.out.println("state is"+RobotState.SENDINGRESPONSE2);
			
			myState = RobotState.SENDINGRESPONSE2;
			
		}
		else if(myState == RobotState.SENDINGRESPONSE2){
			//System.out.println("state is"+RobotState.WAITCOMMAND);
			if(msg.getResponse() != ResponseType.DATA){
				System.out.println("repsonse not data");
				myState = RobotState.WAITCOMMAND;
				msgTimer.start();
			}
		}
		else if (myState == RobotState.WAITCOMMAND){
			msgTimer.restart();
		}
		for (byte b : msg.getFormattedMessage().getBytes())
		{
			byteQueue.add(b);
		}
		synchronized (lock)
		{
			lock.notifyAll();
		}
	}
	
	public enum RobotState{
		WAITCOMMAND, SENDINGRESPONSE1, SENDINGDATA, SENDINGRESPONSE2  
	}
}
