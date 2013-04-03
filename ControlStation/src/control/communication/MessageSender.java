package control.communication;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import lejos.pc.comm.NXTComm;
import control.main.Controller;

/**
 * Runnable class to send messages. To be passed to a Thread.
 * @author Andy Barron
 *
 */
public class MessageSender implements Runnable
{
	
	private NXTComm					outputSender;
	private Controller				myController;
	
	private Queue<CommandMessage>	cmdQueue	= new ConcurrentLinkedQueue<CommandMessage>();
	
	public MessageSender(Controller control, NXTComm comm)
	{
		myController = control;
		outputSender = comm;
	}
	
	public void send(CommandMessage message)
	{
		cmdQueue.add(message);
	}
	
	private void transmit(CommandMessage message) throws IOException
	{
		outputSender.write(message.getFormattedMessage().getBytes());
	}
	
	@Override
	public void run()
	{
		
		while (true)
		{
			
			if (!cmdQueue.isEmpty())
			{
				
				try
				{
					transmit(cmdQueue.peek());
				}
				catch (IOException e)
				{
					System.err.println("Failed to transmit message: " + cmdQueue.peek().getFormattedMessage());
					e.printStackTrace();
					continue;
				}
				
				cmdQueue.remove();
				
			}
			
		}
	}
	
}
