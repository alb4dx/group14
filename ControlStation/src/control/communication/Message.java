package control.communication;
/**
 * The Message class is an abstract class that defines standard functionality among all message types. Messages
 * are used for robot on-board communication.
 * @version 1.0 - Build 04/01/2013
 * @author Stephanie Colen
 * @author Sarina Padilla
 * @author Hubert Chen
 * @author Andy Barron
 * @author John Zambrotta
 * 
 */
public abstract class Message
{
	protected static int sequenceNumber = 0;
	
	protected String messageString = new String();
	protected String formattedMessage = new String();
	protected int seqNum = -1;
	protected int checksum = -1;
	
	/**
	 * calculates the checksum by adding the character
	 * values of the messageString and returning the calculated value
	 * @param messageString			String message to find checksum for
	 * @return	TBD					Checksum integer
	 */
	public static int genCheckSum(String messageString)
	{
		// TODO implement check sum generation
		return 0;
	}
	/**
	 * Get method for string of message
	 * @return	string of messageString
	 */
	public final String getMessageString()
	{
		return messageString;
	}
	
	/**
	 * Returns message in proper format
	 * @return formattedMessage		 Formatted message
	 */
	public final String getFormattedMessage()
	{
		return formattedMessage;
	}
	
	/**
	 * Get method for current Message sequence number
	 * @return seqNum int in message sequence
	 */
	public final int getSeqNum()
	{
		return seqNum;
	}
	
	/**
	 * Get method for checksum. See genCheckSum() method for generating checksum value
	 * @return checksum integar value of checksum
	 */
	public final int getChecksum()
	{
		return checksum;
	}
}
