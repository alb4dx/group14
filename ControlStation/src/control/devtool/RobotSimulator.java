package control.devtool;

import java.io.InputStream;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import control.gui.GraphicsInterface;
import control.communication.*;

public class RobotSimulator
{
	
	public final Object			lock		= new Object();
	public final Queue<Byte>	byteQueue	= new LinkedList<Byte>();
	public final DevNXTComm testNXTComm = new DevNXTComm(this);
	public final InputStream testInputStream = new DevNXTInputStream(this);
	public DevToolWindow myWindow = null;
	public RobotSimulator(DevToolWindow window){
		myWindow = window;
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

	
	public void messageFromStation(String msg)
	{
		//TODO simulated robot behavior
		System.out.println(msg);
		// what type of message do we have?
		// what type of response do we send back?
		// compose command into a string
		// call simulateResponse(String response)
		myWindow.getComm().getMessageArea().append("\n"+"Message Received:"+msg);
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
		for (byte b : msg.getFormattedMessage().getBytes())
		{
			byteQueue.add(b);
		}
		synchronized (lock)
		{
			lock.notifyAll();
		}
	}
	
}
