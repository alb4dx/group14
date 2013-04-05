package control.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import javax.swing.Timer;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;

import control.gui.DebugInterface;
import control.gui.GraphicsInterface;
import control.communication.CommandMessage;
import control.communication.MessageListener;
import control.communication.MessageSender;
import control.communication.ResponseMessage;
import control.communication.CommandMessage.CommandType;
import control.communication.ResponseMessage.ResponseType;

// LOGIC FOR DISPLAYING DEBUGGER
// make a timer to send query command - don't send if query already in messageQueue

/* *
 * 
 * extract (graphics interface) - takes all info from info handler and displays on GUI (refresh calls extract)
 * goals for this prototype - movement, claw, follows safety protocol / disconnects, 
 * keylistener in controller - hard code message - movement commands with WAD (forward, backward, turning) press and hold says go, letting go says stop
 * W - move at 1 rev / s (360 degrees) 
 * A/D - turns by 30 degrees 
 * 
 * controller sends received messages from messageListener to private DebugInterface
 * controller sends command messages from DebugInterface and 
 *
 * */

public class Controller {
	
	private static GraphicsInterface myGraphics;
	private static Queue<CommandMessage> messageQueue;
	private static DebugInterface myDebug;
	private MessageSender messageSender;
	private MessageListener messageListener;
	private ControllerState myState;
	private static Timer msgTimer;
	public int seq = 0;
	private final int DISTANCEINDEX = 0;
	private final int LIGHTINDEX = 1;
	private final int SOUNDINDEX = 2;
	private final int TOUCHINDEX = 3;
	private final int CLAWINDEX = 4;
	private final int HEADINGINDEX = 5;
	private final int SPEEDINDEX = 6;
	private final int ULTRAINDEX = 7;

	public static void main(String[] args) {
		
		Controller controller = new Controller();
		
	}
	
	public Controller() {
		myGraphics = new GraphicsInterface();
		messageQueue = new LinkedList<CommandMessage>();
		myDebug = new DebugInterface(this);
		myState = ControllerState.CANSEND;
		NXTComm nxtComm = null;
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
		messageSender = new MessageSender(this, nxtComm);
		messageListener = new MessageListener(this, nxtComm.getInputStream());
		Thread sender = new Thread(messageSender);
		Thread listener = new Thread(messageListener);
		sender.start();
		listener.start();
		ActionListener querySender = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {			
				boolean queried = false;
				for(CommandMessage cmd : messageQueue){
					if (cmd.getCommand() == CommandType.QUERY) {
						queried = true;
					}
				}
				if(!queried) {
					messageQueue.add(new CommandMessage(CommandType.QUERY));
				}
			}	
		};
		//TODO magic number!!
		Timer queryTimer = new Timer(5000, querySender);
		queryTimer.setInitialDelay(1000);
		//queryTimer.start();
		
		ActionListener timeOut = new ActionListener(){
			public void actionPerformed(ActionEvent arg){
				System.out.println("timeout occured");
				resend();
			}
		};
		 msgTimer= new Timer(3000, timeOut);
		while(true){
			System.out.println("Command:");
			String command = scan.nextLine();
			int endCommand = command.length();
			CommandMessage cmd = null;
			if(command.substring(0,4).equals("init")){
				cmd = new CommandMessage(CommandType.INIT);
			}
			else if(command.substring(0,4).equals("stop")){
				cmd = new CommandMessage(CommandType.STOP);
			}
			else if(command.substring(0,4).equals("query")){
				cmd = new CommandMessage(CommandType.QUERY);
			}
			else if(command.substring(0,4).equals("powd")){
				cmd = new CommandMessage(CommandType.POWD);
			}
			else if(command.substring(0,4).equals("halt")){
				cmd = new CommandMessage(CommandType.HALT);
			}
			else if(command.substring(0,4).equals("quit")){
				cmd = new CommandMessage(CommandType.QUIT);
			}
			else if(command.substring(0,4).equals("ack")){
				cmd = new CommandMessage(CommandType.ACK);
			}
			else if(command.substring(0,4).equals("auto")){
				cmd = new CommandMessage(CommandType.AUTO);
			}
			else if(command.substring(0,4).equals("rset")){
				cmd = new CommandMessage(CommandType.RSET);
			}
			else if(command.substring(0,4).equals("updt")){
				cmd = new CommandMessage(CommandType.UPDT);
			}
			else if(command.substring(0,4).equals("move")){
				endCommand = command.indexOf(":");
				cmd = new CommandMessage(CommandType.MOVE, command.substring(endCommand+1, command.length()));
			}
			else if(command.substring(0,4).equals("turn")){
				endCommand = command.indexOf(":");
				cmd = new CommandMessage(CommandType.TURN, command.substring(endCommand+1, command.length()));
			}
			else if(command.substring(0,4).equals("claw")){
				endCommand = command.indexOf(":");
				cmd = new CommandMessage(CommandType.CLAW, command.substring(endCommand+1, command.length()));
			}
			messageQueue.add(cmd);
			if(myState == ControllerState.CANSEND){
				myState = ControllerState.WAITACK1;
				//msgTimer.start();
				messageSender.send(cmd);
			}
			String response1 = scan.nextLine();
			ResponseMessage ack1 = ResponseMessage.parse(response1);
			onMessageReceive(ack1);
			String response2 = scan.nextLine();
			ResponseMessage ack2 = ResponseMessage.parse(response2);
			onMessageReceive(ack2);
		}
		
	}

	//do we need to give UPDT priority??
	public void addMessage(CommandMessage s) {
		// add string to queue as message
		if(s.getCommand() == CommandType.UPDT) {
			messageQueue.add(s);
		} else {
			messageQueue.add(s);
		}
		myDebug.getQueue().addMessage(s);
	}
	
	public void onMessageReceive(ResponseMessage r) {
		// message listener calls whenever a message receives (hands controller 
		// received message
		this.myDebug.getMyResponse().getMyResponses().append(r.getMessageString());
		switch(myState){
			case WAITACK1:
				if (r.getResponse() == ResponseType.ACK && r.getSeqNum() == this.seq){
					//msgTimer.stop();
					myState = ControllerState.WAITACK2;
				}
				else if (r.getResponse() == ResponseType.NACK && r.getSeqNum() == this.seq){
					resend();
				}
				break;
				// i need to let the user know a command failed Where do i put the message
			case WAITACK2:
				if((r.getResponse() == ResponseType.DONE  || r.getResponse() == ResponseType.FAIL) && r.getSeqNum() == this.seq){
					System.out.println("I will remove this message from queue");
					messageQueue.remove();
					System.out.println(messageQueue.size());
					if (r.getResponse() == ResponseType.FAIL){
						System.out.println(r.getMessageString());
					}
					if(this.seq==0){
						this.seq=1;
					}
					else{
						this.seq=0;
					}
					if(messageQueue.peek() == null){
						CommandMessage filler = new CommandMessage(CommandType.ACK);
						CommandMessage ack = new CommandMessage(CommandType.ACK);
						//messageQueue.add(ack);
						myState = ControllerState.CANSEND;
						messageSender.send(ack);
					}
					else{
						myState = ControllerState.WAITACK1;
						//msgTimer.restart();
						messageSender.send(messageQueue.peek());
					}	
				}
				//this still uses the messageQueue in controller to send messages
				else if (r.getResponse() == ResponseType.ERROR && r.getSeqNum() == this.seq){
					this.myDebug.getMyFrame().setVisible(true);
					this.myDebug.getMyFrame().invalidate();
					this.myDebug.getMyFrame().validate();
					this.myDebug.getMyFrame().repaint();
					myState = ControllerState.DEBUG;
				}
				
				else if(r.getResponse() == ResponseType.DATA  && r.getSeqNum() == this.seq){
					int light = (Integer) (r.getValueArray()[LIGHTINDEX]);
					int sound = (Integer) (r.getValueArray()[SOUNDINDEX]);
					boolean touch = (Boolean) (r.getValueArray()[TOUCHINDEX]);
					int ultra = (Integer) (r.getValueArray()[ULTRAINDEX]);
					int distance = (Integer) (r.getValueArray()[DISTANCEINDEX]);
					float claw = (Float) (r.getValueArray()[CLAWINDEX]);
					int heading = (Integer) (r.getValueArray()[HEADINGINDEX]);
					int speed = (Integer) (r.getValueArray()[SPEEDINDEX]);
					
					myGraphics.getMyInfo().addData(light,sound,ultra,touch);
					myGraphics.getMyInfo().setGraph(myGraphics.getMyInfo().updateGraph());
					myGraphics.getMyInfo().setDistance(distance);
					myGraphics.getMyInfo().setClaw(claw);
					myGraphics.getMyInfo().setHeading(heading);
					myGraphics.getMyInfo().setSpeed(speed);
					myGraphics.extract();
				}
				break;
				//TODO im too tired to think
			case DEBUG: 
				break;
		}
		// all robot responses given to debugger, which needs to send to robot
		// response pane
		
		// FIRST update information handler through myGraphics (method needs to be written)
		// call extract in myGraphics (should refresh)
		// if error message, print to error log in graphics interface
	}
	
	// if a response is received, send to debug interface, debug interface
	// will then update robot response scroll pane
	public void resend(){
		//msgTimer.stop();
		//msgTimer.restart();
		//will crash if front of queue empty
		messageSender.send(messageQueue.element());
	}
	
	public enum ControllerState{
		CANSEND, WAITACK1, WAITACK2, DEBUG
	}
}
