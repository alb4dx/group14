package control.communication;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.JOptionPane;

import control.main.Controller;


/**
 * Runnable class to listen for messages and pass them to the controller. To be
 * passed into a Thread.
 * 
 * @author Andy Barron
 * 
 */
public class MessageListener implements Runnable
{
	
	private InputStream		inputListener;
	private Controller		myController;
	
	private byte[]			buffer				= new byte[512];
	private StringBuilder	charQueue			= new StringBuilder();
	
	static final int		CHAR_NOT_FOUND		= -1;
	static final int		MAX_QUEUE_LENGTH	= 1024 * 16;
	
	/**
	 * Constructor for MessageListner with required parameters
	 * 
	 * @param control
	 *            program Controller
	 * @param nxtStream
	 *            InputStream for NXT
	 */
	public MessageListener(Controller control, InputStream nxtStream)
	{
		myController = control;
		inputListener = nxtStream;
	}
	
	/**
	 * Method that dictates behavior for when we have received a valid message
	 * 
	 * @param message
	 */
	private void processMessage(ResponseMessage message)
	{
		// TODO what do we do when a message is found in the stream?
	}
	
	/**
	 * Method that dictates behavior for when we have received an invalid
	 * message
	 */
	private void processInvalidMessage()
	{
		
	}
	
	/**
	 * Method that listens for messages via a loop and the blocking method
	 * InputStream.read()
	 * 
	 * @throws IOException
	 */
	private void listen() throws IOException
	{
		
		while (true)
		{
			
			// read from string to buffer, then append buffer to queue
			
			int len = inputListener.read(buffer);
			if (len == -1) break; // if end of stream is reached
				
			for (int i = 0; i < len; i++)
			{
				charQueue.append((char) buffer[i]);
			}
			
			// find { and }
			
			int startBracket = charQueue.indexOf("{");
			int endBracket = (startBracket == CHAR_NOT_FOUND) ? CHAR_NOT_FOUND
					: charQueue.indexOf("}", startBracket);
			
			// if {}'s found, parse message
			
			if (startBracket != CHAR_NOT_FOUND && endBracket != CHAR_NOT_FOUND)
			{
				ResponseMessage response = ResponseMessage.parse(charQueue
						.substring(startBracket, endBracket + 1));
				
				if (response != null) // if valid message, process
				{
					processMessage(response);
				}
				else
				// if invalid message, do stuff
				{
					processInvalidMessage();
				}
				
				charQueue.delete(startBracket, endBracket + 1);
			}
			
			// empty queue if it's huge, since we obviously have a problem with
			// our messages
			
			if (charQueue.length() > MAX_QUEUE_LENGTH)
			{
				charQueue.delete(0, charQueue.length());
			}
			
		}
		
	}
	
	/**
	 * Initializes MessageListener can calls listen(). DON'T CALL THIS METHOD -
	 * it should only be invoked by passing this Runnable MessageListener to a
	 * java.lang.Thread!
	 */
	@Override
	public void run()
	{
		
		try
		{
			
			listen();
			
		}
		catch (IOException e)
		{
			
			String msg = "Error listening for messages: IOException";
			for (StackTraceElement element : e.getStackTrace())
			{
				msg += "\n     " + element;
			}
			
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, msg, "Fatal error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// getters and setters.
	// TODO do we actually need these...?
	
	public InputStream getInputListener()
	{
		return inputListener;
	}
	
	public void setInputListener(InputStream inputListener)
	{
		this.inputListener = inputListener;
	}
	
	public Controller getMyController()
	{
		return myController;
	}
	
	public void setMyController(Controller myController)
	{
		this.myController = myController;
	}
	
}
