package control.input;

import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.ControllerEvent;
import net.java.games.input.Event;


public class GameControllerThread extends Thread
{
	
	private final Event	event				= new Event();
	static final int	EVENT_QUEUE_SIZE	= 12;
	
	public static void main(String[] args)
	{
		GameControllerThread g = new GameControllerThread();
		g.start();
	}
	
	public void run()
	{
		ControllerEnvironment en = ControllerEnvironment
				.getDefaultEnvironment();
		
		Controller pad = null;
		
		for (Controller c : en.getControllers())
		{
			// System.out.println(c.toString());
			if (c.getType() == Controller.Type.GAMEPAD)
			{
				pad = c;
				break;
			}
		}
		
		if (pad == null)
		{
			System.out.println("No controller found");
			return;
		}
		
		else
		{
			System.out.println("Found controller: " + pad);
		}
		
		pad.setEventQueueSize(EVENT_QUEUE_SIZE);
		
		while (true)
		{
			try
			{
				Thread.sleep(1000 / 60);
			}
			catch (InterruptedException e1)
			{
			}
			pad.poll();
			while (pad.getEventQueue().getNextEvent(event))
			{
				processEvent(event);
			}
		}
	}
	
	private boolean processEvent(Event event)
	{
		System.out.println(event);
		
		return false;
	}
	
	public void test()
	{
		ControllerEnvironment en = ControllerEnvironment
				.getDefaultEnvironment();
		
		Controller pad = null;
		
		for (Controller c : en.getControllers())
		{
			// System.out.println(c.toString());
			if (c.getType() == Controller.Type.GAMEPAD)
			{
				pad = c;
				break;
			}
		}
		
		System.out.println(pad);
		
		pad.setEventQueueSize(10);
		
		Event e = new Event();
		while (true)
		{
			try
			{
				Thread.sleep(1000 / 60);
			}
			catch (InterruptedException e1)
			{
			}
			pad.poll();
			pad.getEventQueue().getNextEvent(e);
			System.out.println(e);
		}
	}
}
