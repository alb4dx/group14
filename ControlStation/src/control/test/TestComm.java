package control.test;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import control.gui.GraphicsInterface;


public class TestComm
{
	
	public final Object			lock		= new Object();
	public final Queue<Byte>	byteQueue	= new LinkedList<Byte>();
	public final TestToolComm testNXTComm = new TestToolComm(this);
	public final InputStream testInputStream = new TestToolInputStream(this);
	private final Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args)
	{
		new GraphicsInterface();
		TestComm t = new TestComm();
		t.launchCommandLine();
	}
	
	public void launchCommandLine()
	{
		System.out.println("Command line test tool now active.");
		String input = new String();
		do
		{
			System.out.println("Enter text to send from simulated robot, or \"exit\" to quit.");
			input = scan.nextLine();
			
			
			simulateResponse(input);
			
			
		} while (!input.equals("exit"));
		System.out.println("Toodles!");
	}

	
	public void messageFromStation(String msg)
	{
		System.out.println(msg);
	}
	
	public void simulateResponse(String response)
	{
		for (byte b : response.getBytes())
		{
			byteQueue.add(b);
		}
		synchronized (lock)
		{
			System.out.println("Notifying");
			lock.notifyAll();
		}
	}
	
}
