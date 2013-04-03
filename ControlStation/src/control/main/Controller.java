package control.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import control.gui.DebugInterface;
import control.gui.GraphicsInterface;
import control.communication.CommandMessage;
import control.communication.ResponseMessage;
import control.communication.CommandMessage.CommandType;

// LOGIC FOR DISPLAYING DEBUGGER
// make a timer to send query command - don't send if query already in messageQueue

/* *
 * 
 * extract (graphics interface) - takes all info from info handler and displays on GUI (refresh calls extract)
 * goals for this prototype - movement, claw, follows safety protocol / disconnects, 
 * movement commands with WAD (forward, backward, turning) press and hold says go, letting go says stop
 * W - move at 1 rev / s (360 degrees) 
 * A/D - turns by 30 degrees 
 * controller sends received messages from messageListener to private DebugInterface
 * controller sends command messages from DebugInterface and 
 *
 * */

public class Controller {
	
	private static GraphicsInterface myGraphics;
	private static ArrayList<CommandMessage> messageQueue;
	private static DebugInterface myDebug;
	public int seq = 0;

	public static void main(String[] args) {
		
		Controller controller = new Controller();
		
	}
	
	public Controller() {
		myGraphics = new GraphicsInterface();
		messageQueue = new ArrayList<CommandMessage>();
		myDebug = new DebugInterface(this);
		
		ActionListener querySender = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {			
				boolean queried = false;
				for(int x = 0; x < messageQueue.size(); x++){
					if (messageQueue.get(x).getCommand() == CommandType.QUERY) {
						queried = true;
					}
				}
				if(!queried) {
					messageQueue.add(new CommandMessage(CommandType.QUERY));
				}
			}	
		};
		
		Timer queryTimer = new Timer(5000, querySender);
		queryTimer.setInitialDelay(1000);
		queryTimer.start();
	}
	
	public void addMessage(CommandMessage s) {
		// add string to queue as message
		if(s.getCommand() == CommandType.UPDT) {
			messageQueue.add(0, s);
		} else {
			messageQueue.add(s);
		}
		myDebug.getQueue().addMessage(s);
	}
	
	public void onMessageReceive(ResponseMessage r) {
		// message listener calls whenever a message receives (hands controller 
		// received message
		
		// all robot responses given to debugger, which needs to send to robot
		// response pane
		
		// FIRST update information handler through myGraphics (method needs to be written)
		// call extract in myGraphics (should refresh)
		// if error message, print to error log in graphics interface
	}
	
	// if a response is received, send to debug interface, debug interface
	// will then update robot response scroll pane

}
