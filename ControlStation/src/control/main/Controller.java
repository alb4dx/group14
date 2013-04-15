package control.main;

/**
 * This class contains all of the logic of our control station. It handles all of the sending
 * and receiving of messages between the control station and the robot.
 * @version 1.0 - Build 04/01/2013
 * @author Stephanie Colen
 * @author Sarina Padilla
 * @author Hubert Chen
 * @author Andy Barron
 * @author John Zambrotta
 * 
 */
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
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
import control.input.GameControllerThread;
import control.communication.CommandMessage;
import control.communication.MessageListener;
import control.communication.MessageSender;
import control.communication.ResponseMessage;
import control.communication.CommandMessage.CommandType;
import control.communication.ResponseMessage.ResponseType;
import control.devtool.DevNXTComm;
import control.devtool.DevToolWindow;

//TODO
/*
 * java docs
 * update design doc
 */

public class Controller {
	/** Reference to the GUI of this controller */
	private static GraphicsInterface myGraphics;
	/** the queue containing messages to be sent to the robot */
	private static Queue<CommandMessage> messageQueue;
	/** Reference to the debugger */
	private static DebugInterface myDebug;
	/** Reference to the thread that will send messages to the robot */
	private MessageSender messageSender;
	/** Reference to the thread that will listen to messages from the robot */
	private MessageListener messageListener;
	/** Reference to the thread that will listen to the xbox controller */
	private Thread xboxThread;
	/** The current state of the controller */
	private ControllerState myState;
	/** Reference to the msgTimer which will resend timed out messages */
	private static Timer msgTimer;
	/** Reference to the queryTimer that automatically sends query messages */
	private Timer queryTimer;
	/** The expected seqence number of the received message */
	public int seq = 0;
	/** The index of the distance value in a data response message */
	public final int DISTANCEINDEX = 0;
	/** The index of the light value in a data response message */
	public final int LIGHTINDEX = 1;
	/** The index of the sound value in a data response message */
	public final int SOUNDINDEX = 2;
	/** The index of the touch value in a data response message */
	public final int TOUCHINDEX = 3;
	/** The index of the claw value in a data response message */
	public final int CLAWINDEX = 4;
	/** The index of the heading value in a data response message */
	public final int HEADINGINDEX = 5;
	/** The index of the speed value in a data response message */
	public final int SPEEDINDEX = 6;
	/** The index of the ultrasonic value in a data response message */
	public final int ULTRAINDEX = 7;
	/** The speed the robot will move at */
	public int SPEED = 360;
	/** The max speed of the robot */
	public final static int MAXSPEED = 720;
	/** The max turn speed of the robot */
	public final static int MAXTURN = 360;
	/** The current turn speed of the robot */
	public final int TURNSPEED = 30;
	/** The value that will make the robot turn left */
	public final int TURNLEFT = -TURNSPEED;
	/** The value that will make the robot turn right */
	public final int TURNRIGHT = TURNSPEED;
	/** Boolean determining if the devtool will be used */
	private boolean debugMode = true;

	public static void main(String[] args) {

		boolean debug = true;
		new Controller(debug);

	}

	/**
	 * This creates a bluetooth connection to our NXT brick with the specified
	 * address. It will exit the program if a bluetooth NXTComm cannot be made
	 * or if a connection to a brick cannot be made.
	 * 
	 * @return comm the NXTComm object with the bluetooth connection to the NXT
	 *         brick
	 */
	private NXTComm connectToNXT() {

		NXTComm comm = null;
		try {
			comm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		} catch (NXTCommException e) {

			e.printStackTrace();
			System.err.println("Couldn't create NXT comm. Exiting.");
			System.exit(-1);
		}

		// Connect
		NXTInfo info = new NXTInfo();
		info.protocol = NXTCommFactory.BLUETOOTH;
		info.name = "LEAD7";
		info.deviceAddress = "00165313E6DB";

		try {
			comm.open(info, NXTComm.PACKET);
		} catch (NXTCommException e) {
			e.printStackTrace();
			System.err.println("Couldn't connect to NXT " + info.name
					+ ". Exiting.");
			System.exit(-2);
		}
		return comm;

	}

	/**
	 * 
	 * @param debug
	 */
	public Controller(boolean debug) {

		debugMode = debug;
		myGraphics = new GraphicsInterface();
		messageQueue = new LinkedList<CommandMessage>();
		myDebug = new DebugInterface(this);
		myState = ControllerState.CANSEND;

		NXTComm nxtComm = null;

		if (!debugMode) {
			nxtComm = connectToNXT();
		} else {
			DevToolWindow dev = new DevToolWindow();
			nxtComm = new DevNXTComm(dev.robotSim);
		}

		// Open NXTComm

		messageSender = new MessageSender(this, nxtComm);
		messageListener = new MessageListener(this, nxtComm.getInputStream());
		Thread sender = new Thread(messageSender);
		Thread listener = new Thread(messageListener);

		sender.setName("Message Sender Thread");
		listener.setName("Message Listener Thread");

		sender.start();
		listener.start();

		ActionListener querySender = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("query timeout");
				boolean queried = false;
				for (CommandMessage cmd : messageQueue) {
					if (cmd.getCommand() == CommandType.QUERY) {
						queried = true;
					}
				}
				if (!queried) {
					CommandMessage query = new CommandMessage(CommandType.QUERY);
					addMessage(query);
				}
				queryTimer.stop();
			}
		};
		// TODO magic number!!
		queryTimer = new Timer(5000, querySender);
		// queryTimer.setInitialDelay(1000);
		queryTimer.start();

		ActionListener timeOut = new ActionListener() {
			public void actionPerformed(ActionEvent arg) {
				System.out.println("timeout occured");
				resend();
			}
		};
		// TODO change this back to 3000 10 secs just for testing purposes
		msgTimer = new Timer(3000, timeOut);

		initInputHandlers();

		running: while (true) {
			if (nxtComm == null) {
				System.out.println("connection lost attempting to reconnect");
				for (int i = 0; i < 5; ++i) {
					nxtComm = connectToNXT();
					if (nxtComm != null)
						break;
					if (nxtComm == null && i == 4)
						break running;
				}
			}
			// System.out.println("start while");
			if (myState == ControllerState.CANSEND) {
				if (messageQueue.peek() != null) {
					if (messageQueue.peek().getCommand() == CommandType.QUIT)
						myState = ControllerState.QUITTING;
					else
						myState = ControllerState.WAITACK1;
					myGraphics.updateMessageLog(messageQueue.peek(), true);
					msgTimer.start();
					messageSender.send(messageQueue.peek());
					System.out.println("Message sent:"
							+ messageQueue.peek().getMessageString());
				}
			}
			if (myState == ControllerState.DISCONNECT)
				break;
			// System.out.println("Command:");
			// String command = scan.nextLine();
			//
			// int endCommand = command.length();
			// CommandMessage cmd = null;
			// if (command.substring(0, command.length()).equals("init"))
			// {
			// cmd = new CommandMessage(CommandType.INIT);
			// }
			// else if (command.substring(0, command.length()).equals("stop"))
			// {
			// cmd = new CommandMessage(CommandType.STOP);
			// }
			// else if (command.substring(0, command.length()).equals("query"))
			// {
			// cmd = new CommandMessage(CommandType.QUERY);
			// }
			// else if (command.substring(0, command.length()).equals("powd"))
			// {
			// cmd = new CommandMessage(CommandType.POWD);
			// }
			// else if (command.substring(0, command.length()).equals("halt"))
			// {
			// cmd = new CommandMessage(CommandType.HALT);
			// }
			// else if (command.substring(0, command.length()).equals("quit"))
			// {
			// cmd = new CommandMessage(CommandType.QUIT);
			// }
			// else if (command.substring(0, command.length()).equals("ack"))
			// {
			// cmd = new CommandMessage(CommandType.ACK);
			// }
			// else if (command.substring(0, command.length()).equals("auto"))
			// {
			// cmd = new CommandMessage(CommandType.AUTO);
			// }
			// else if (command.substring(0, command.length()).equals("rset"))
			// {
			// cmd = new CommandMessage(CommandType.RSET);
			// }
			// else if (command.substring(0, command.length()).equals("updt"))
			// {
			// cmd = new CommandMessage(CommandType.UPDT);
			// }
			// else if (command.substring(0,
			// command.indexOf(":")).equals("move"))
			// {
			// endCommand = command.indexOf(":");
			// cmd = new CommandMessage(CommandType.MOVE, command.substring(
			// endCommand + 1, command.length()));
			// }
			// else if (command.substring(0,
			// command.indexOf(":")).equals("turn"))
			// {
			// endCommand = command.indexOf(":");
			// cmd = new CommandMessage(CommandType.TURN, command.substring(
			// endCommand + 1, command.length()));
			// }
			// else if (command.substring(0,
			// command.indexOf(":")).equals("claw"))
			// {
			// endCommand = command.indexOf(":");
			// cmd = new CommandMessage(CommandType.CLAW, command.substring(
			// endCommand + 1, command.length()));
			// }
			// addMessage(cmd);
			// if (myState == ControllerState.CANSEND)
			// {
			// if(cmd.getCommand() == CommandType.INIT){
			// myState = ControllerState.CONNECTING;
			// }
			// else{
			// myState = ControllerState.WAITACK1;
			// }
			// msgTimer.start();
			// //System.out.println(cmd.getMessageString());
			// myGraphics.updateMessageLog(cmd, true);
			// messageSender.send(cmd);
			//
			// }
		}
		try {
			nxtComm.close();
		} catch (IOException e) {
			System.err
					.println("Could not close NXT Comm. How did this happen??");
			e.printStackTrace();
		}

		System.out.println("Connection closed. Bye!");
		System.exit(0);

	}

	/**
	 * Adds a message to the controller's messageQueue
	 * 
	 * @param msg
	 *            the new message to be sent to the robot
	 */
	public void addMessage(CommandMessage msg) {
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

	/**
	 * Generates an appropriate message according to our message protocol and
	 * the state of the controller.
	 * 
	 * @param r
	 *            the response message received from the robot
	 */
	public void onMessageReceive(ResponseMessage r) {
		System.out.println("in state:" + myState);
		System.out.println("Message received:" + r.getResponse());
		// message listener calls whenever a message receives (hands controller
		// received message
		myGraphics.updateMessageLog(r, false);
		this.myDebug.getMyResponse().getMyResponses()
				.append(r.getMessageString());
		// this still uses the messageQueue in controller to send
		// messages
		if (r.getResponse() == ResponseType.ERROR && r.getSeqNum() == this.seq) {
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
		switch (myState) {
		// TODO what do we do when we get a failure for init
		case CONNECTING:
			if (r.getResponse() == ResponseType.ACK
					&& r.getSeqNum() == this.seq) {
				msgTimer.stop();
			} else if (r.getResponse() == ResponseType.CONN
					&& r.getSeqNum() == this.seq) {
				messageQueue.remove();
				respondToDoneFail();
			} else if (r.getResponse() == ResponseType.NACK
					&& r.getSeqNum() == this.seq) {
				resend();
			}
			break;
		case WAITACK1:
			if (r.getResponse() == ResponseType.ACK
					&& r.getSeqNum() == this.seq) {
				msgTimer.stop();
				if (messageQueue.peek().getCommand() == CommandType.QUERY) {
					myState = ControllerState.WAITDATA;
				} else {
					myState = ControllerState.WAITACK2;
				}
			} else if (r.getResponse() == ResponseType.NACK
					&& r.getSeqNum() == this.seq) {
				resend();
			}
			break;
		// i need to let the user know a command failed Where do i put the
		// message
		// TODO check casting booleans in java
		case WAITDATA:
			if (r.getResponse() == ResponseType.DATA
					&& r.getSeqNum() == this.seq) {
				msgTimer.stop();
				int light = (Integer) (r.getValueArray()[LIGHTINDEX]);
				int sound = (Integer) (r.getValueArray()[SOUNDINDEX]);
				boolean touch = (Boolean) (r.getValueArray()[TOUCHINDEX]);
				int ultra = (Integer) (r.getValueArray()[ULTRAINDEX]);
				int distance = (Integer) (r.getValueArray()[DISTANCEINDEX]);
				int claw = (Integer) (r.getValueArray()[CLAWINDEX]);
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
				queryTimer.restart();
			}
			break;
		case WAITACK2:
			if ((r.getResponse() == ResponseType.DONE || r.getResponse() == ResponseType.FAIL)
					&& r.getSeqNum() == this.seq) {
				messageQueue.remove();
				if (r.getResponse() == ResponseType.FAIL) {
					System.out.println(r.getMessageString());
				}
				respondToDoneFail();
			}

			break;
		case QUITTING:
			if (r.getResponse() == ResponseType.ACK
					&& r.getSeqNum() == this.seq) {
				msgTimer.stop();
				myState = ControllerState.DISCONNECT;
			}
			break;
		}
	}

	/**
	 * This resends the current message to the robot as a result of a timeout.
	 * It will also restart the msgTimer
	 */
	public void resend() {
		msgTimer.stop();
		msgTimer.restart();
		System.out.println("Resending:"
				+ messageQueue.element().getMessageString());
		messageSender.send(messageQueue.element());
	}

	/**
	 * The list of the Controller States is an enum with the values CANSEND,
	 * CONNECTING, WAITACK1, WAITACK2, WAITDATA, DEBUG, QUITTING, DISCONNECT.
	 * 
	 * @author Hubert
	 * 
	 */
	public enum ControllerState {
		CANSEND, CONNECTING, WAITACK1, WAITDATA, WAITACK2, DEBUG, QUITTING, DISCONNECT
	}

	/**
	 * Sends the next message in the queue after receiving a confirmation from
	 * the robot that the current command has been executed. If there isn't a
	 * message in the queue, and ACK is sent.
	 */
	public void respondToDoneFail() {
		if (this.seq == 0) {
			this.seq = 1;
		} else {
			this.seq = 0;
		}
		if (messageQueue.peek() == null) {
			CommandMessage filler = new CommandMessage(CommandType.ACK);
			CommandMessage ack = new CommandMessage(CommandType.ACK);
			myState = ControllerState.CANSEND;
			myGraphics.updateMessageLog(ack, true);
			messageSender.send(ack);

		} else {
			if (messageQueue.peek().getCommand() == CommandType.QUERY)
				myState = ControllerState.WAITDATA;
			else if (messageQueue.peek().getCommand() == CommandType.QUIT)
				myState = ControllerState.QUITTING;
			else
				myState = ControllerState.WAITACK1;
			myGraphics.updateMessageLog(messageQueue.peek(), true);
			msgTimer.restart();
			messageSender.send(messageQueue.peek());
		}
	}

	/**
	 * Resends the current message if a corrupted message is received
	 * 
	 * @param str
	 *            the corrupted message
	 */
	public void onInvalidMessage(String str) {
		System.err.println("Corrupted message received: " + str);
		if (myState != ControllerState.WAITACK2
				|| myState != ControllerState.WAITDATA)
			resend();
	}

	/**
	 * Adds a keyboard input handler to the controller and start a thread for
	 * listening to an xbox controller.
	 */
	public void initInputHandlers() {
		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new ControllerInputHandler(this, manager));
		xboxThread = new GameControllerThread(this);
		xboxThread.start();
	}

	/**
	 * Returns the current state of the controller
	 * 
	 * @return myState the current state
	 */
	public ControllerState getState() {
		return myState;
	}

	/**
	 * Sets the current state of the controller to state
	 * 
	 * @param state
	 *            the new state of the controller
	 */
	public void setState(ControllerState state) {
		myState = state;
	}

	/**
	 * Returns the messageSender thread
	 * 
	 * @return messageSender the thread sending messages to the robot
	 */
	public MessageSender getMessageSender() {
		return messageSender;
	}

	/**
	 * Returns the message timer
	 * 
	 * @return msgTimer the message timer
	 */
	public Timer getMessageTimer() {
		return msgTimer;
	}

	/**
	 * Returns the GUI associated with controller
	 * 
	 * @return myGraphics the GUI of the controller
	 */
	public GraphicsInterface getInterface() {
		return myGraphics;
	}

}
