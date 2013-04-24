package control.communication;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import lejos.pc.comm.NXTComm;
import control.main.Controller;

/**
 * Runnable class to send messages. To be passed to an instance of java.lang.Thread
 * 
 * @author Andy Barron
 */
public class MessageSender implements Runnable
{
	
	private NXTComm					outputSender;
	private Controller				myController;
	private Queue<CommandMessage>	cmdQueue	= new ConcurrentLinkedQueue<CommandMessage>();
	
	/**
	 * The constructor for this MessageSender: sets appropriate fields
	 * 
	 * @param control the Controller associated with this Sender
	 * @param comm the NXTComm using this sender
	 */
	public MessageSender(Controller control, NXTComm comm)
	{
		myController = control;
		outputSender = comm;
	}
	
	/**
	 * Sends a command message by adding it to the MessageQueue
	 * 
	 * @param message
	 */
	public void send(CommandMessage message)
	{
		cmdQueue.add(message);
	}
	
	/**
	 * Transmits a CommandMessage on the connection
	 * 
	 * @param message the CommandMessage to send to the robot
	 * @throws IOException if the connection cannot be found
	 */
	private void transmit(CommandMessage message) throws IOException
	{
		outputSender.write(message.getFormattedMessage().getBytes());
	}
	
	/**
	 * Continually checks to see if the messages in the queue can be sent 
	 * by calling transmit
	 */
	@Override
	public void run()
	{
		while (true) {
			if (!cmdQueue.isEmpty()) {	
				try {
					transmit(cmdQueue.peek());
				} catch (IOException e) {
					System.err.println("Failed to transmit message: " + cmdQueue.peek().getFormattedMessage());
					e.printStackTrace();
					continue;
				}
				cmdQueue.remove();	
			}
		}
	}
}
