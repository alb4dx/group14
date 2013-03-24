package org.group14.communication;

public class CommandMessage extends Message
{
	
	private Object		param;
	private CommandType	command;
	
	public CommandMessage(CommandType command)
	{
		// TODO
	}
	
	public CommandMessage(CommandType command, Object param)
	{
		// TODO
	}
	
	public Object getParam()
	{
		return param;
	}
	
	public CommandType getCommand()
	{
		return command;
	}
	
	public enum CommandType
	{
		INIT, MOVE, TURN, CLAW, STOP, QUERY, QUIT, ACK
	}
}