package control.communication;

/**
 * CommandMessage is a class that encapsulates the required fields for a command
 * message and extends Message
 * 
 * @version 1.0 - Build 04/01/2013
 * @author Stephanie Colen
 * @author Sarina Padilla
 * @author Hubert Chen
 * @author Andy Barron
 * @author John Zambrotta
 * 
 */
public class CommandMessage extends Message {
	/** Parameter needed for certain command messages */
	private Object param;
	/** The command type of the message */
	private CommandType command;
	/** The sequence number of the message */
	private static int sequenceNumber = 0;

	/**
	 * One parameter constructor to instantiate CommandMessage based on a
	 * CommandType
	 * 
	 * @param command
	 *            Type of command to create
	 */
	public CommandMessage(CommandType command) {
		this.command = command;
		this.seqNum = sequenceNumber;
		if (sequenceNumber == 0) {
			sequenceNumber = 1;
		} else {
			sequenceNumber = 0;
		}
		this.messageString = "" + this.seqNum;
		if (this.command == CommandType.INIT) {
			this.messageString += "init";
		} else if (this.command == CommandType.STOP) {
			this.messageString += "stop";
		} else if (this.command == CommandType.QUERY) {
			this.messageString += "query";
		} else if (this.command == CommandType.QUIT) {
			this.messageString += "quit";
		} else if (this.command == CommandType.ACK) {
			this.messageString += "ack";
		} else if (this.command == CommandType.AUTO) {
			this.messageString += "auto";
		} else if (this.command == CommandType.HALT) {
			this.messageString += "halt";
		} else if (this.command == CommandType.POWD) {
			this.messageString += "powd";
		} else if (this.command == CommandType.RSET) {
			this.messageString += "rset";
		} else if (this.command == CommandType.UPDT) {
			this.messageString += "updt";
		}
		int checkSum = genCheckSum(this.messageString);
		this.checksum = checkSum;
		this.formattedMessage = "{" + this.messageString + "|" + checkSum + "}";
	}

	/**
	 * Two parameter constructor to instantiate CommandMessage based on a
	 * CommandType and an object base the CommandMessage on
	 * 
	 * @param command
	 *            Type of command to create
	 * @param param
	 *            Object to base the CommandMessage on
	 */
	public CommandMessage(CommandType command, Object param) {
		this.command = command;
		this.param = param;
		this.seqNum = sequenceNumber;
		if (sequenceNumber == 0) {
			sequenceNumber = 1;
		} else {
			sequenceNumber = 0;
		}
		this.messageString = "" + this.seqNum;
		if (this.command == CommandType.MOVE) {
			this.messageString += "move";
		} else if (this.command == CommandType.TURN) {
			this.messageString += "turn";
		} else if (this.command == CommandType.CLAW) {
			this.messageString += "claw";
		}
		this.messageString = this.messageString + ":" + param;
		int checkSum = genCheckSum(this.messageString);
		this.checksum = checkSum;
		this.formattedMessage = "{" + this.messageString + "|" + checkSum + "}";
	}

	/**
	 * Set param field to param
	 * 
	 * @param param
	 *            value param field is set to
	 */
	public void setParam(Object param) {
		this.param = param;
	}

	/**
	 * Set command field to command
	 * 
	 * @param command
	 *            value command field is set to
	 */
	public void setCommand(CommandType command) {
		this.command = command;
	}

	/**
	 * Getter method for Object param
	 * 
	 * @return Returns object that CommandMessage is based on
	 */
	public Object getParam() {
		return param;
	}

	/**
	 * Getter method for CommandType
	 * 
	 * @return Returns type of command in CommandType enum
	 */
	public CommandType getCommand() {
		return command;
	}

	/**
	 * CommandType is an enumerated list of Command Types Commands include:
	 * INIT, MOVE, TURN, CLAW, STOP, QUERY, QUIT, ACK, AUTO, HALT, POWD, RSET,
	 * UP
	 * 
	 * @author Group14
	 * 
	 */
	public enum CommandType {
		INIT, MOVE, TURN, CLAW, STOP, QUERY, QUIT, ACK, AUTO, HALT, POWD, RSET, UPDT
	}
}