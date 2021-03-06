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
	 * @param control program Controller
	 * @param nxtStream InputStream for NXT
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
	protected void processMessage(ResponseMessage message)
	{
		myController.onMessageReceive(message);
	}
	
	/**
	 * Message that dictates what to do with a corrupted/invalid message
	 * @param str The invalid character string 
	 */
	protected void processInvalidMessage(String str)
	{
		myController.onInvalidMessage(str);
	}
	
	/**
	 * Method that listens for messages via a loop and the blocking method
	 * InputStream.read()
	 * 
	 * @throws IOException
	 */
	private void listen() throws IOException {
		while (true) {
			int len = inputListener.read(buffer);
			if (len == -1) break; // if end of stream is reached
				
			for (int i = 0; i < len; i++) {
				charQueue.append((char) buffer[i]);
			}
			
			// find { and }
			int startBracket = charQueue.indexOf("{");
			int endBracket = (startBracket == CHAR_NOT_FOUND) ? CHAR_NOT_FOUND
					: charQueue.indexOf("}", startBracket);
			
			// if {}'s found, parse message
			if (startBracket != CHAR_NOT_FOUND && endBracket != CHAR_NOT_FOUND) {
				String str = charQueue
						.substring(startBracket, endBracket + 1);
				ResponseMessage response = ResponseMessage.parse(str);
				
				// if valid message, process
				if (response != null) {
					processMessage(response);
				}
				// if invalid message, do stuff
				else {
					processInvalidMessage(str);
				}
				
				charQueue.delete(startBracket, endBracket + 1);
			}
			
			// empty queue if it's huge, since we obviously have a problem with
			// our messages
			if (charQueue.length() > MAX_QUEUE_LENGTH) {
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
		try {
			listen();	
		} catch (IOException e) {
			String msg = "Error listening for messages: IOException";
			for (StackTraceElement element : e.getStackTrace()) {
				msg += "\n     " + element;
			}
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, msg, "Fatal error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
