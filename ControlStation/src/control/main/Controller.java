package control.main;

import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import javax.swing.Timer;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

import control.gui.DebugInterface;
import control.gui.GraphicsInterface;
import control.input.ControllerInputHandler;
import control.communication.CommandMessage;
import control.communication.MessageListener;
import control.communication.MessageSender;
import control.communication.ResponseMessage;
import control.communication.CommandMessage.CommandType;
import control.communication.ResponseMessage.ResponseType;
import control.devtool.DevNXTComm;
import control.devtool.DevToolWindow;


// LOGIC FOR DISPLAYING DEBUGGER
// make a timer to send query command - don't send if query already in
// messageQueue

/* *
 * 
 * extract (graphics interface) - takes all info from info handler and displays
 * on GUI (refresh calls extract)
 * goals for this prototype - movement, claw, follows safety protocol /
 * disconnects,
 * keylistener in controller - hard code message - movement commands with WAD
 * (forward, backward, turning) press and hold says go, letting go says stop
 * W - move at 1 rev / s (360 degrees)
 * A/D - turns by 30 degrees
 * 
 * controller sends received messages from messageListener to private
 * DebugInterface
 * controller sends command messages from DebugInterface and
 */

//TODO
/*
 * Extract(): does not update the graph, and the error log
 * DebugInterface: Finish this
 * DebugMessages: make controller be able to handle this
 * Xbox Controller: everything
 * -Only movement messages have key listeners as of now
 * -javadoc the new methods
 * Controller: implement the Query Timer
 * -solidify the control scheme
 * Handle loss of communication - try to reconnect 5 times then shut off
 * java docs
 * update design doc
 *
 * Suggestion:
 * Generalize the error log in Graphics Interface to show all response messages to the user
 */

// TODO extract doesnt update the graph, figure out how to bring up the debug
// interface on error message
public class Controller
{
	
	private static GraphicsInterface		myGraphics;
	private static Queue<CommandMessage>	messageQueue;
	private static DebugInterface			myDebug;
	private MessageSender					messageSender;
	private MessageListener					messageListener;
	private ControllerState					myState;
	private static Timer					msgTimer;
	public int								seq				= 0;
	public final int						DISTANCEINDEX	= 0;
	public final int						LIGHTINDEX		= 1;
	public final int						SOUNDINDEX		= 2;
	public final int						TOUCHINDEX		= 3;
	public final int						CLAWINDEX		= 4;
	public final int						HEADINGINDEX	= 5;
	public final int						SPEEDINDEX		= 6;
	public final int						ULTRAINDEX		= 7;
	public final int						SPEED			= 360;
	public final int						TURNSPEED		= 30;
	public final int						TURNLEFT		= -TURNSPEED;
	public final int						TURNRIGHT		= TURNSPEED;
	
	private boolean							debugMode		= false;

	public static void main(String[] args)
	{
		
		boolean debug = true;
		new Controller(debug);
		
	}
	
	private NXTComm connectToNXT()
	{
		
		NXTComm comm = null;
		try
		{
			comm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		}
		catch (NXTCommException e)
		{
			
			e.printStackTrace();
			System.err.println("Couldn't create NXT comm. Exiting.");
			System.exit(-1);
		}
		
		// Connect
		NXTInfo info = new NXTInfo();
		info.protocol = NXTCommFactory.BLUETOOTH;
		info.name = "LEAD7";
		info.deviceAddress = "00165313E6DB";
		
		try
		{
			comm.open(info, NXTComm.PACKET);
		}
		catch (NXTCommException e)
		{
			e.printStackTrace();
			System.err.println("Couldn't connect to NXT " + info.name
					+ ". Exiting.");
			System.exit(-2);
		}
		return comm;
		
	}
	
	public Controller(boolean debug)
	{
		
		debugMode = debug;
		myGraphics = new GraphicsInterface();
		messageQueue = new LinkedList<CommandMessage>();
		myDebug = new DebugInterface(this);
		myState = ControllerState.CANSEND;
		
		NXTComm nxtComm = null;
		
		if (!debugMode)
		{
			nxtComm = connectToNXT();
		}
		else
		{
			DevToolWindow dev = new DevToolWindow();
			nxtComm = new DevNXTComm(dev.robotSim);
		}
		
		Scanner scan = new Scanner(System.in);
		
		// Open NXTComm
		
		messageSender = new MessageSender(this, nxtComm);
		messageListener = new MessageListener(this, nxtComm.getInputStream());
		Thread sender = new Thread(messageSender);
		Thread listener = new Thread(messageListener);
		
		sender.setName("Message Sender Thread");
		listener.setName("Message Listener Thread");
		
		sender.start();
		listener.start();
		ActionListener querySender = new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				boolean queried = false;
				for (CommandMessage cmd : messageQueue)
				{
					if (cmd.getCommand() == CommandType.QUERY)
					{
						queried = true;
					}
				}
				if (!queried)
				{
					messageQueue.add(new CommandMessage(CommandType.QUERY));
				}
			}
		};
		// TODO magic number!!
		Timer queryTimer = new Timer(5000, querySender);
		queryTimer.setInitialDelay(1000);
		// queryTimer.start();
		
		ActionListener timeOut = new ActionListener()
		{
			public void actionPerformed(ActionEvent arg)
			{
				System.out.println("timeout occured");
				resend();
			}
		};
		// TODO change this back to 3000 10 secs just for testing purposes
		msgTimer = new Timer(10000, timeOut);
		
		initInputHandler();
		
		while (true)
		{
			System.out.println("Command:");
			String command = scan.nextLine();
			int endCommand = command.length();
			CommandMessage cmd = null;
			if (command.substring(0, command.length()).equals("init"))
			{
				cmd = new CommandMessage(CommandType.INIT);
			}
			else if (command.substring(0, command.length()).equals("stop"))
			{
				cmd = new CommandMessage(CommandType.STOP);
			}
			else if (command.substring(0, command.length()).equals("query"))
			{
				cmd = new CommandMessage(CommandType.QUERY);
			}
			else if (command.substring(0, command.length()).equals("powd"))
			{
				cmd = new CommandMessage(CommandType.POWD);
			}
			else if (command.substring(0, command.length()).equals("halt"))
			{
				cmd = new CommandMessage(CommandType.HALT);
			}
			else if (command.substring(0, command.length()).equals("quit"))
			{
				cmd = new CommandMessage(CommandType.QUIT);
			}
			else if (command.substring(0, command.length()).equals("ack"))
			{
				cmd = new CommandMessage(CommandType.ACK);
			}
			else if (command.substring(0, command.length()).equals("auto"))
			{
				cmd = new CommandMessage(CommandType.AUTO);
			}
			else if (command.substring(0, command.length()).equals("rset"))
			{
				cmd = new CommandMessage(CommandType.RSET);
			}
			else if (command.substring(0, command.length()).equals("updt"))
			{
				cmd = new CommandMessage(CommandType.UPDT);
			}
			else if (command.substring(0, command.indexOf(":")).equals("move"))
			{
				endCommand = command.indexOf(":");
				cmd = new CommandMessage(CommandType.MOVE, command.substring(
						endCommand + 1, command.length()));
			}
			else if (command.substring(0, command.indexOf(":")).equals("turn"))
			{
				endCommand = command.indexOf(":");
				cmd = new CommandMessage(CommandType.TURN, command.substring(
						endCommand + 1, command.length()));
			}
			else if (command.substring(0, command.indexOf(":")).equals("claw"))
			{
				endCommand = command.indexOf(":");
				cmd = new CommandMessage(CommandType.CLAW, command.substring(
						endCommand + 1, command.length()));
			}
			addMessage(cmd);
			if (myState == ControllerState.CANSEND)
			{	
				if(cmd.getCommand() == CommandType.INIT){
					myState = ControllerState.CONNECTING;
				}
				else{
				myState = ControllerState.WAITACK1;
				}
				msgTimer.start();
				System.out.println(cmd.getMessageString());
				messageSender.send(cmd);
			}
		}
		
	}
	
	// do we need to give UPDT priority??
	public void addMessage(CommandMessage msg)
	{
		// add string to queue as message
		// if (s.getCommand() == CommandType.UPDT)
		// {
		// messageQueue.add(s);
		// }
		// else
		// {
		// messageQueue.add(s);
		// }
		messageQueue.add(msg);
		myDebug.getQueue().addMessage(msg);
	}
	
	public void onMessageReceive(ResponseMessage r)
	{
		// message listener calls whenever a message receives (hands controller
		// received message
		System.out.println("Message Received:" + r.getFormattedMessage());
		this.myDebug.getMyResponse().getMyResponses()
				.append(r.getMessageString());
		// this still uses the messageQueue in controller to send
		// messages
		if (r.getResponse() == ResponseType.ERROR
				&& r.getSeqNum() == this.seq)
		{
			CommandMessage filler = new CommandMessage(CommandType.ACK);
			CommandMessage ack = new CommandMessage(CommandType.ACK);
			// messageQueue.add(ack);
			messageSender.send(ack);
			myDebug.display();
			myDebug.getMyFrame().invalidate();
			myDebug.getMyFrame().validate();
			myDebug.getMyFrame().repaint();
			myState = ControllerState.DEBUG;
		}
		switch (myState)
		{
			case CONNECTING:
				if (r.getResponse() == ResponseType.ACK
				&& r.getSeqNum() == this.seq)
				{
					msgTimer.stop();
				}
				else if (r.getResponse() == ResponseType.CONN && r.getSeqNum() == this.seq){
					messageQueue.remove();
					respondToDoneFail();
				}
				else if (r.getResponse() == ResponseType.NACK && r.getSeqNum() == this.seq){
					resend();
				}
				break;
			case WAITACK1:
				if (r.getResponse() == ResponseType.ACK
						&& r.getSeqNum() == this.seq)
				{
					msgTimer.stop();
					if (messageQueue.peek().getCommand() == CommandType.QUERY)
					{
						myState = ControllerState.WAITDATA;
					}
					else
					{
						myState = ControllerState.WAITACK2;
					}
				}
				else if (r.getResponse() == ResponseType.NACK
						&& r.getSeqNum() == this.seq)
				{
					resend();
				}
			break;
			// i need to let the user know a command failed Where do i put the
			// message
			case WAITDATA:
				if (r.getResponse() == ResponseType.DATA
						&& r.getSeqNum() == this.seq)
				{
					msgTimer.stop();
					int light = (Integer) (r.getValueArray()[LIGHTINDEX]);
					int sound = (Integer) (r.getValueArray()[SOUNDINDEX]);
					boolean touch = (Boolean) (r.getValueArray()[TOUCHINDEX]);
					int ultra = (Integer) (r.getValueArray()[ULTRAINDEX]);
					int distance = (Integer) (r.getValueArray()[DISTANCEINDEX]);
					float claw = (Float) (r.getValueArray()[CLAWINDEX]);
					int heading = (Integer) (r.getValueArray()[HEADINGINDEX]);
					int speed = (Integer) (r.getValueArray()[SPEEDINDEX]);
					
					myGraphics.getMyInfo().addData(light, sound, ultra, touch);
					myGraphics.getMyInfo().setGraph(
							myGraphics.getMyInfo().getGraph());
					myGraphics.getMyInfo().setDistance(distance);
					myGraphics.getMyInfo().setClaw(claw);
					myGraphics.getMyInfo().setHeading(heading);
					myGraphics.getMyInfo().setSpeed(speed);
					myGraphics.extract();
					messageQueue.remove();
					respondToDoneFail();
				}
			break;
			case WAITACK2:
				if ((r.getResponse() == ResponseType.DONE || r.getResponse() == ResponseType.FAIL)
						&& r.getSeqNum() == this.seq)
				{
					messageQueue.remove();
					if (r.getResponse() == ResponseType.FAIL)
					{
						System.out.println(r.getMessageString());
					}
					respondToDoneFail();
				}
				
			break;
			// TODO im too tired to think
			case DEBUG:
			break;
		}
		// all robot responses given to debugger, which needs to send to robot
		// response pane
		
		// FIRST update information handler through myGraphics (method needs to
		// be written)
		// call extract in myGraphics (should refresh)
		// if error message, print to error log in graphics interface
	}
	
	// if a response is received, send to debug interface, debug interface
	// will then update robot response scroll pane
	public void resend()
	{
		msgTimer.stop();
		msgTimer.restart();
		// will crash if front of queue empty
		System.out.println("Resending:"
				+ messageQueue.element().getMessageString());
		messageSender.send(messageQueue.element());
	}
	
	public enum ControllerState
	{
		CANSEND, CONNECTING, WAITACK1, WAITDATA, WAITACK2, DEBUG
	}
	
	public void respondToDoneFail(){
		if (this.seq == 0)
		{
			this.seq = 1;
		}
		else
		{
			this.seq = 0;
		}
		if (messageQueue.peek() == null)
		{
			CommandMessage filler = new CommandMessage(
					CommandType.ACK);
			CommandMessage ack = new CommandMessage(CommandType.ACK);
			myState = ControllerState.CANSEND;
			messageSender.send(ack);
			
		}
		else
		{
			myState = ControllerState.WAITACK1;
			msgTimer.restart();
			messageSender.send(messageQueue.peek());
		}
	}
	public void onInvalidMessage(String str)
	{
		System.err.println("Corrupted message received: " + str);
		resend();
	}
	
	public void initInputHandler()
	{
//		KeyListener upKey = new KeyListener()
//		{
//			@Override
//			public void keyTyped(KeyEvent e)
//			{
//			}
//			
//			@Override
//			public void keyPressed(KeyEvent e)
//			{
//				// TODO Auto-generated method stub
//				if (e.getKeyCode() == KeyEvent.VK_UP)
//				{
//					CommandMessage cmd = new CommandMessage(CommandType.MOVE,
//							SPEED);
//					messageQueue.add(cmd);
//					if (myState == ControllerState.CANSEND)
//					{
//						myState = ControllerState.WAITACK1;
//						msgTimer.start();
//						System.out.println(cmd.getMessageString());
//						messageSender.send(cmd);
//					}
//				}
//			}
//			
//			@Override
//			public void keyReleased(KeyEvent e)
//			{
//				// TODO Auto-generated method stub
//				if (e.getKeyCode() == KeyEvent.VK_UP)
//				{
//					CommandMessage cmd = new CommandMessage(CommandType.STOP);
//					messageQueue.add(cmd);
//					if (myState == ControllerState.CANSEND)
//					{
//						myState = ControllerState.WAITACK1;
//						msgTimer.start();
//						System.out.println(cmd.getMessageString());
//						messageSender.send(cmd);
//					}
//				}
//			}
//			
//		};
//		KeyListener leftKey = new KeyListener()
//		{
//			
//			@Override
//			public void keyTyped(KeyEvent e)
//			{
//				// TODO Auto-generated method stub
//			}
//			
//			@Override
//			public void keyPressed(KeyEvent e)
//			{
//				// TODO Auto-generated method stub
//			}
//			
//			@Override
//			public void keyReleased(KeyEvent e)
//			{
//				// TODO Auto-generated method stub
//				if (e.getKeyCode() == KeyEvent.VK_LEFT)
//				{
//					CommandMessage cmd = new CommandMessage(CommandType.TURN,
//							TURNLEFT);
//					messageQueue.add(cmd);
//					if (myState == ControllerState.CANSEND)
//					{
//						myState = ControllerState.WAITACK1;
//						msgTimer.start();
//						System.out.println(cmd.getMessageString());
//						messageSender.send(cmd);
//					}
//				}
//			}
//			
//		};
//		
//		KeyListener rightKey = new KeyListener()
//		{
//			
//			@Override
//			public void keyTyped(KeyEvent e)
//			{
//				// TODO Auto-generated method stub
//			}
//			
//			@Override
//			public void keyPressed(KeyEvent e)
//			{
//				// TODO Auto-generated method stub
//			}
//			
//			@Override
//			public void keyReleased(KeyEvent e)
//			{
//				// TODO Auto-generated method stub
//				if (e.getKeyCode() == KeyEvent.VK_RIGHT)
//				{
//					CommandMessage cmd = new CommandMessage(CommandType.TURN,
//							TURNRIGHT);
//					messageQueue.add(cmd);
//					if (myState == ControllerState.CANSEND)
//					{
//						myState = ControllerState.WAITACK1;
//						msgTimer.start();
//						System.out.println(cmd.getMessageString());
//						messageSender.send(cmd);
//					}
//				}
//			}
//			
//		};
		
		// myGraphics.getMyFrame().getContentPane().addKeyListener(upKey);
		// myGraphics.getMyFrame().getContentPane().addKeyListener(leftKey);
		// myGraphics.getMyFrame().getContentPane().addKeyListener(rightKey);
		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new ControllerInputHandler(this, manager));
	}
	
	public ControllerState getState()
	{
		return myState;
	}
	
	public void setState(ControllerState state)
	{
		myState = state;
	}
	
	public MessageSender getMessageSender()
	{
		return messageSender;
	}
	
	public Timer getMessageTimer()
	{
		return msgTimer;
	}
	
	public GraphicsInterface getInterface()
	{
		return myGraphics;
	}
	
}
