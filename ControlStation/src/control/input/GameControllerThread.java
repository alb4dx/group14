package control.input;

import java.awt.geom.Point2D;

import control.communication.CommandMessage;
import control.communication.CommandMessage.CommandType;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;


public class GameControllerThread extends Thread
{
	
	private final Event		event				= new Event();
	static final int		EVENT_QUEUE_SIZE	= 12;
	private Point2D.Float	lStick				= new Point2D.Float();
	private Point2D.Float	rStick				= new Point2D.Float();
	
	private Component		lsx;
	private Component		lsy;
	private Component		rsx;
	private Component		rsy;
	
	static final String		LS_X_ID				= "x";
	static final String		LS_Y_ID				= "y";
	static final String		RS_X_ID				= "rx";
	static final String		RS_Y_ID				= "ry";
	
	static final float		STICK_DEADZONE		= .3f;
	
	int move,turn,claw;
	int lastMove,lastTurn,lastClaw;
	
	private control.main.Controller	myControl;
	
	static final int MAX_MOVE = control.main.Controller.MAXSPEED;
	static final int MAX_TURN = control.main.Controller.MAXTURN;
	private static final int	MAX_CLAW	= 360;
	
	static final long POLLS_PER_SEC = 20;
	
	/**
	 * Constructor for the GameController 
	 * 
	 * @param control the Controller that uses this GameControllerThread
	 */
	public GameControllerThread(control.main.Controller control)
	{
		myControl = control;
	}
	
	/**
	 * Continually polls the game pad for input events and processes
	 * them if necessary.
	 */
	public void run()
	{
		ControllerEnvironment en = ControllerEnvironment
				.getDefaultEnvironment();
		
		Controller pad = null;
		
		for (Controller c : en.getControllers()) {
			if (c.getType() == Controller.Type.GAMEPAD) {
				pad = c;
				break;
			}
		}
		
		if (pad == null) {
			System.out.println("No controller found");
			return;
		}
		else {
			System.out.println("Found controller: " + pad);
		}
		
		pad.setEventQueueSize(EVENT_QUEUE_SIZE);
		
		for (Component c : pad.getComponents()) {
			String id = c.getIdentifier().getName();
			if (id.equals(LS_X_ID)) {
				lsx = c;
			}
			else if (id.equals(LS_Y_ID)) {
				lsy = c;
			}
			else if (id.equals(RS_X_ID)) {
				rsx = c;
			}
			else if (id.equals(RS_Y_ID)) {
				rsy = c;
			}
		}
		
		while (true) {
			try {
				Thread.sleep(1000 / POLLS_PER_SEC);
			} catch (InterruptedException e1){ }
			pad.poll();
			while (pad.getEventQueue().getNextEvent(event)) {
				processEvent(event);
			}
			sendMessageIfRequired();
		}
	}

	private void sendMessageIfRequired()
	{
		
		if(Math.abs(lStick.x) > Math.abs(lStick.y)){ // turn
			move = 0;
			turn = (int) (lStick.x*(float)MAX_TURN);
		} else { // move
			turn = 0;
			move = (int) (lStick.y*(float)MAX_MOVE);
		}
		
		claw = (int)(rStick.x*(float)MAX_CLAW);
		
		if(move != lastMove) {
			myControl.addMessage(new CommandMessage(CommandType.MOVE, move));
			lastMove = move;
		}
		
		if(turn != lastTurn) {
			myControl.addMessage(new CommandMessage(CommandType.TURN, turn));
			lastTurn = turn;
		}
		
		if(claw != lastClaw) {
			myControl.addMessage(new CommandMessage(CommandType.CLAW, claw));
			lastClaw = claw;
		}
	}

	private void clampStick(Point2D.Float stick, float deadZone)
	{
		if (Math.abs(stick.x) < deadZone) stick.x = 0.00f;
		if (Math.abs(stick.y) < deadZone) stick.y = 0.00f;
	}
	
	private void processEvent(Event event)
	{
				
		if (event.getComponent() == lsx) {
			lStick.x = event.getValue();
		}
		else if (event.getComponent() == lsy) {
			lStick.y = -event.getValue();
		}
		else if (event.getComponent() == rsx) {
			rStick.x = event.getValue();
		}
		else if (event.getComponent() == rsy) {
			rStick.y = -event.getValue();
		}
		
		clampStick(lStick,STICK_DEADZONE);
		clampStick(rStick,STICK_DEADZONE);
		
		System.out.println("LS: " + lStick);
		System.out.println("RS: " + rStick);	
	}
}
