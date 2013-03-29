package control.communication;
/**
 * CommandMessage is a class that encapsulates the required fields for
 * a command message and extends Message
 * @version 1.0 - Build 04/01/2013
 * @author Stephanie Colen
 * @author Sarina Padilla
 * @author Hubert Chen
 * @author Andy Barron
 * @author John Zambrotta
 *
 */
public class CommandMessage extends Message
{
	
	private Object		param;
	private CommandType	command;
	
	/**
	 * One parameter constructor to instantiate CommandMessage based on a CommandType
	 * @param command			Type of command to create
	 */
	public CommandMessage(CommandType command)
	{
		// TODO
	}
	
	/**
	 * Two parameter constructor to instantiate CommandMessage based on a CommandType
	 * and an object base the CommandMessage on
	 * @param command			Type of command to create
	 * @param param				Object to base the CommandMessage on
	 */
	public CommandMessage(CommandType command, Object param)
	{
		// TODO
	}
	
	/**
	 * Getter method for Object param
	 * @return					Returns object that CommandMessage is based on
	 */
	public Object getParam()
	{
		return param;
	}
	
	/**
	 * Getter method for CommandType
	 * @return					Returns type of command in CommandType enum
	 */
	public CommandType getCommand()
	{
		return command;
	}
	
	/**
	 * CommandType is an enumerated list of Command Types
	 * Commands include: INIT, MOVE, TURN, CLAW, STOP, QUERY, QUIT, ACK	
	 * @author Group14
	 *
	 */
	public enum CommandType
	{
		INIT, MOVE, TURN, CLAW, STOP, QUERY, QUIT, ACK
	}
}