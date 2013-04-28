package control.input;

import java.awt.geom.Point2D;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import control.communication.CommandMessage;
import control.communication.CommandMessage.CommandType;


public class GameControllerThread extends Thread
{
	
	private final Event				event				= new Event();
	private static final int		EVENT_QUEUE_SIZE	= 12;
	private Point2D.Float			lStick				= new Point2D.Float();
	private Point2D.Float			rStick				= new Point2D.Float();
	
	private Component				lsx;
	private Component				lsy;
	private Component				rsx;
	private Component				rsy;
	
	private static final String		LS_X_ID				= "x";
	private static final String		LS_Y_ID				= "y";
	private static final String		RS_X_ID				= "rx";
	private static final String		RS_Y_ID				= "ry";
	
	/**
	 * Minimum percent of analog axis movement required for non-zero value
	 */
	static final float				STICK_DEADZONE		= .1f;
	
	private int						move, turn, claw;
	private int						lastMove, lastTurn, lastClaw;
	
	private control.main.Controller	myControl;
	
	private static final int		MAX_MOVE			= control.main.Controller.MAXSPEED;
	private static final int		MAX_TURN			= control.main.Controller.MAXTURN;
	private static final int		MAX_CLAW			= 360;
	
	/**
	 * Number of discrete for gamepad movement speed value
	 */
	static final int				MOVE_STEPS			= 6;
	
	/**
	 * Number of discrete for gamepad turn speed value
	 */
	static final int				TURN_STEPS			= 6;
	
	/**
	 * Number of discrete for gamepad claw speed value
	 */
	static final int				CLAW_STEPS			= 6;
	
	/**
	 * Number of times per second to poll the gamepad
	 */
	static final long				POLLS_PER_SEC		= 10;
	
	/**
	 * Constructor for the GameController
	 * 
	 * @param control
	 *            the Controller that uses this GameControllerThread
	 */
	public GameControllerThread(control.main.Controller control)
	{
		myControl = control;
	}
	
	/**
	 * Continually polls the game pad for input events and processes them if
	 * necessary.
	 */
	public void run()
	{
		ControllerEnvironment en = ControllerEnvironment
				.getDefaultEnvironment();
		
		Controller pad = null;
		
		for (Controller c : en.getControllers())
		{
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
		
		for (Component c : pad.getComponents())
		{
			String id = c.getIdentifier().getName();
			if (id.equals(LS_X_ID))
			{
				lsx = c;
			}
			else if (id.equals(LS_Y_ID))
			{
				lsy = c;
			}
			else if (id.equals(RS_X_ID))
			{
				rsx = c;
			}
			else if (id.equals(RS_Y_ID))
			{
				rsy = c;
			}
			else
			{
				// do nothing
			}
			
		}
		
		while (true)
		{
			try
			{
				Thread.sleep(1000 / POLLS_PER_SEC);
			}
			catch (InterruptedException e1)
			{
			}
			pad.poll();
			while (pad.getEventQueue().getNextEvent(event))
			{
				processEvent(event);
			}
			sendMessageIfRequired();
		}
	}
	
	private void sendMessageIfRequired()
	{
		
		if (Math.abs(lStick.x) >= Math.abs(lStick.y))
		{ // turn
			move = 0;
			turn = (int) (lStick.x * (float) MAX_TURN);
		}
		else
		{ // move
			turn = 0;
			move = (int) (lStick.y * (float) MAX_MOVE);
		}
		
		claw = (int) (rStick.x * (float) MAX_CLAW);
		
		turn = granularize(turn, MAX_TURN, TURN_STEPS);
		move = granularize(move, MAX_MOVE, MOVE_STEPS);
		claw = granularize(claw, MAX_CLAW, CLAW_STEPS);
		
		if (move != lastMove)
		{
			myControl.addMessage(new CommandMessage(CommandType.MOVE, move));
			lastMove = move;
		}
		else
		{
			// do nothing
		}
		
		if (turn != lastTurn)
		{
			myControl.addMessage(new CommandMessage(CommandType.TURN, turn));
			lastTurn = turn;
		}
		else
		{
			// do nothing
		}
		
		if (claw != lastClaw)
		{
			myControl.addMessage(new CommandMessage(CommandType.CLAW, claw));
			lastClaw = claw;
		}
		else
		{
			// do nothing
		}
	}
	
	private void clampStick(Point2D.Float stick, float deadZone)
	{
		if (Math.abs(stick.x) < deadZone) stick.x = 0.00f;
		if (Math.abs(stick.y) < deadZone) stick.y = 0.00f;
	}
	
	private void processEvent(Event event)
	{
		
		if (event.getComponent() == lsx)
		{
			lStick.x = event.getValue();
		}
		else if (event.getComponent() == lsy)
		{
			lStick.y = -event.getValue();
		}
		else if (event.getComponent() == rsx)
		{
			rStick.x = event.getValue();
		}
		else if (event.getComponent() == rsy)
		{
			rStick.y = -event.getValue();
		}
		else
		{
			// do nothing
		}
		
		clampStick(lStick, STICK_DEADZONE);
		clampStick(rStick, STICK_DEADZONE);
		
	}
	
	private static int granularize(int value, int max, int steps)
	{
		int stepSize = Math.round((float) max / ((float) steps - 1));
		int numSteps = Math.round((float) value / (float) stepSize);
		return numSteps * stepSize;
	}
	
}
