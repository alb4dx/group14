package org.group14.communication;

public abstract class Message
{
	protected String messageString = new String();
	protected String formattedMessage = new String();
	protected int seqNum = -1;
	protected int checksum = -1;
	
	public static int genCheckSum(String messageString)
	{
		// TODO implement check sum generation
		return 0;
	}
	
	public final String getMessageString()
	{
		return messageString;
	}
	
	public final String getFormattedMessage()
	{
		return formattedMessage;
	}
	
	public final int getSeqNum()
	{
		return seqNum;
	}
	
	public final int getChecksum()
	{
		return checksum;
	}
}
