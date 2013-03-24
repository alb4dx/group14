package control.test;

import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;


public class BluetoothTest
{
	public static void main(String[] args)
	{
		
		// get NXTComm
		
		NXTComm nxtComm = null;
		// InputStream in = null;
		// OutputStream out = null;
		try
		{
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		}
		catch (NXTCommException e)
		{
			System.err.println("Couldn't create NXT comm");
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println("Got NXTComm");
		// Connect
		NXTInfo info = new NXTInfo();
		info.protocol = NXTCommFactory.BLUETOOTH;
		info.name = "LEAD7";
		info.deviceAddress = "00165313E6DB";
		
		try
		{
			nxtComm.open(info, NXTComm.PACKET);
		}
		catch (NXTCommException e)
		{
			System.err.println("Couldn't connect to NXT " + info.name);
			e.printStackTrace();
			System.exit(-2);
		}
		
		System.out.println("Connected to NXT");
		
		System.out.println("Waiting for message...");
		try
		{
			byte[] b = new byte[256];
			nxtComm.getInputStream().read(b);
			String s = new String(b);
			System.out.println("Received: " + s);
		}
		catch (IOException e1)
		{
			System.err.println("Error listening for message");
			e1.printStackTrace();
		}
		
		System.out.println("Message acquired! Waiting to send...");
		
		BluetoothTest.sleep(5);
		
		System.out.println("Sending message...");
		
		try
		{
			nxtComm.getOutputStream().write("Hello World".getBytes());
			nxtComm.getOutputStream().flush();
		}
		catch (IOException e)
		{
			System.err.println("Couldn't write message to byte stream!");
			e.printStackTrace();
		}
		
		System.out.println("Message sent, waiting...");
		
		BluetoothTest.sleep(5);
		
		try
		{
			nxtComm.close();
		}
		catch (IOException e)
		{
			System.err.println("Could not close nxt comm. WTF?");
			e.printStackTrace();
		}
		
		System.out.println("Connection closed");
		
		// done
	}
	
	public static void sleep(float seconds)
	{
		try
		{
			Thread.sleep((long) ((float) seconds * 1000f));
		}
		catch (InterruptedException e)
		{
			System.err.println("Wait interrupted!!");
			e.printStackTrace();
		}
		
	}
}
