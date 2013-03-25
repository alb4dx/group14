package control.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

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
		InputStream in = null;
		Scanner scan = new Scanner(System.in);
		
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
		
		in = nxtComm.getInputStream();
		
		System.out.println("Connected to NXT");
		
		System.out.println("Waiting for message...");
		try
		{
			String s;
			
			byte[] b = new byte[256];
			int len = in.read(b);
			s = new String(b, 0, len);
			
			System.out.println("Received: " + s);
		}
		catch (IOException e1)
		{
			System.err.println("Error listening for message");
			e1.printStackTrace();
		}
		
		System.out.println("Message received!");
		
		// BluetoothTest.sleep(5);
		System.out.print("Enter a message to be sent: ");
		
		String msg = scan.nextLine();
		
		System.out.println("Sending message...");
		
		try
		{
			nxtComm.write(msg.getBytes());
		}
		catch (IOException e)
		{
			System.err.println("Couldn't write message to byte stream!");
			e.printStackTrace();
		}
		
		System.out.println("Message sent!");
		
		// BluetoothTest.sleep(5);
		
		try
		{
			nxtComm.close();
		}
		catch (IOException e)
		{
			System.err.println("Could not close NXT Comm. How did this happen??");
			e.printStackTrace();
		}
		
		System.out.println("Connection closed. Bye!");
		
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
