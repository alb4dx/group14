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
		int sum=0;
		for(int i=0; i < messageString.length(); ++i){
				sum += messageString.charAt(i);
		}
		//System.out.println("in genCheckSum:"+ ~sum);
		return ~sum;
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
	 * @return checksum integer value of checksum
	 */
	public final int getChecksum()
	{
		return checksum;
	}
	/**
	 *Sets the messageString field to value
	 * @param value			string messageString field will be set to
	 */
	public void setMessageString(String value) {
		this.messageString = value;
	}
	/**
	 *Sets the formattedMessage field to value
	 * @param value			string formattedMessage field will be set to
	 */
	public void setFormattedMessage(String value) {
		this.formattedMessage = value;
	}
	/**
	 *Sets the seqNum field to 
	 * @param value			string seqNum field will be set to
	 */
	public void setSeqNum(int value) {
		this.seqNum = value;
	}
	/**
	 *Sets the checksum field to value
	 * @param value			string checksum field will be set to
	 */
	public void setChecksum(int value) {
		this.checksum = value;
	}
}
