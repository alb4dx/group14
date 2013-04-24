package control.input;

import java.awt.KeyEventDispatcher;

import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.TreeSet;

import control.communication.CommandMessage;
import control.communication.CommandMessage.CommandType;
import control.main.Controller;

public class ControllerInputHandler implements KeyEventDispatcher
{
	
	private Controller				controller;
	private KeyboardFocusManager	manager;
	private Set<Integer>			keysDown	= new TreeSet<Integer>();
	
	/**
	 * The constructor for the ControllerInputHandler - sets fields 
	 * 
	 * @param control the Controller that uses this InputHandler
	 * @param manager the KeyboardFocusManager that handles all keystrokes
	 */
	public ControllerInputHandler(Controller control,
			KeyboardFocusManager manager)
	{
		this.controller = control;
		this.manager = manager;
	}
	
	private CommandMessage messageOnKeyPress(int keyCode)
	{
		CommandMessage cmd = null;
		
		switch (keyCode)
		{
			case KeyEvent.VK_UP:
				cmd = new CommandMessage(CommandType.MOVE, controller.SPEED);
			break;
			case KeyEvent.VK_LEFT:
				cmd = new CommandMessage(CommandType.TURN, controller.TURNLEFT);
			break;
			case KeyEvent.VK_RIGHT:
				cmd = new CommandMessage(CommandType.TURN, controller.TURNRIGHT);
			break;
			case KeyEvent.VK_SPACE:
				cmd = new CommandMessage(CommandType.CLAW, 360);
			break;
			case KeyEvent.VK_1:
				controller.SPEED = controller.MAXSPEED/10;
				System.out.println(controller.SPEED);
				if(!keysDown.contains(KeyEvent.VK_UP)) return null;
				cmd = new CommandMessage(CommandType.MOVE, controller.SPEED);
			break;
			case KeyEvent.VK_2:
				controller.SPEED = 2*controller.MAXSPEED/10;
				System.out.println(controller.SPEED);
				if(!keysDown.contains(KeyEvent.VK_UP)) return null;
				cmd = new CommandMessage(CommandType.MOVE, controller.SPEED);
			break;
			case KeyEvent.VK_3:
				controller.SPEED = 3*controller.MAXSPEED/10;
				System.out.println(controller.SPEED);
				if(!keysDown.contains(KeyEvent.VK_UP)) return null;
				cmd = new CommandMessage(CommandType.MOVE, controller.SPEED);
			break;
			case KeyEvent.VK_4:
				controller.SPEED = 4*controller.MAXSPEED/10;
				System.out.println(controller.SPEED);
				if(!keysDown.contains(KeyEvent.VK_UP)) return null;
				cmd = new CommandMessage(CommandType.MOVE, controller.SPEED);
			break;
			case KeyEvent.VK_5:
				controller.SPEED = 5*controller.MAXSPEED/10;
				System.out.println(controller.SPEED);
				if(!keysDown.contains(KeyEvent.VK_UP)) return null;
				cmd = new CommandMessage(CommandType.MOVE, controller.SPEED);
			break;
			case KeyEvent.VK_6:
				controller.SPEED = 6*controller.MAXSPEED/10;
				System.out.println(controller.SPEED);
				if(!keysDown.contains(KeyEvent.VK_UP)) return null;
				cmd = new CommandMessage(CommandType.MOVE, controller.SPEED);
			break;
			case KeyEvent.VK_7:
				controller.SPEED = 7*controller.MAXSPEED/10;
				System.out.println(controller.SPEED);
				if(!keysDown.contains(KeyEvent.VK_UP)) return null;
				cmd = new CommandMessage(CommandType.MOVE, controller.SPEED);
			break;
			case KeyEvent.VK_8:
				controller.SPEED = 8*controller.MAXSPEED/10;
				System.out.println(controller.SPEED);
				if(!keysDown.contains(KeyEvent.VK_UP)) return null;
				cmd = new CommandMessage(CommandType.MOVE, controller.SPEED);
			break;
			case KeyEvent.VK_9:
				controller.SPEED = 9*controller.MAXSPEED/10;
				System.out.println(controller.SPEED);
				if(!keysDown.contains(KeyEvent.VK_UP)) return null;
				cmd = new CommandMessage(CommandType.MOVE, controller.SPEED);
			break;
			case KeyEvent.VK_0:
				controller.SPEED = controller.MAXSPEED;
				System.out.println(controller.SPEED);
				if(!keysDown.contains(KeyEvent.VK_UP)) return null;
				cmd = new CommandMessage(CommandType.MOVE, controller.SPEED);
			break;
			case KeyEvent.VK_ESCAPE:
				cmd = new CommandMessage(CommandType.QUIT);
			break;
		}
		
		return cmd;
	}
	
	private CommandMessage messageOnKeyRelease(int keyCode)
	{
		CommandMessage cmd = null;
		
		switch (keyCode)
		{
			case KeyEvent.VK_UP:
				cmd = new CommandMessage(CommandType.STOP);
			break;
			case KeyEvent.VK_SPACE:
				cmd = new CommandMessage(CommandType.CLAW, 0);
			break;
		}
		
		return cmd;
	}
	
	private void sendAndWait(CommandMessage cmd)
	{
		controller.addMessage(cmd);
	}
	
	private void keyPressed(KeyEvent e)
	{
		CommandMessage cmd = messageOnKeyPress(e.getKeyCode());
		if (cmd == null) {
			return;
		}
		else {
			sendAndWait(cmd);
		}	
	}
	
	private void keyReleased(KeyEvent e)
	{
		CommandMessage cmd = messageOnKeyRelease(e.getKeyCode());
		if (cmd == null) {
			return;
		}
		else {
			sendAndWait(cmd);
		}
	}
	
	/**
	 * An interface method that captures KeyEvents associated with commands and calls 
	 * relevant control methods or sends relevant command messages.
	 */
	public boolean dispatchKeyEvent(KeyEvent e)
	{
		
		if (manager.getCurrentFocusCycleRoot() != controller.getInterface()
				.getMyFrame()) {
			return false;
		}
		if (e.getID() == KeyEvent.KEY_PRESSED) {
			if (keysDown.add(e.getKeyCode())) {
				keyPressed(e);
			}
			return true;
		}
		else if (e.getID() == KeyEvent.KEY_RELEASED) {
			if (keysDown.remove(e.getKeyCode())) {
				keyReleased(e);
			}
				return true;
		}
		return false;
	}
	
}
