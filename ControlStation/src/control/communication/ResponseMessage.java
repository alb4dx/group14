package control.communication;

/**
 * 
 * The ResponseMessage class inherits the standard functionality of a Message
 * and is specific to a message that is sent as a response to the robot
 * 
 * @version 1.0 - Build 04/01/2013
 * 
 * @author Stephanie Colen
 * @author Sarina Padilla
 * @author Hubert Chen
 * @author Andy Barron
 * @author John Zambrotta
 * 
 */
public class ResponseMessage extends Message {
	/** The array of descriptions of the values in valueArray in a data response */
	private String[] fieldArray;
	/** The values of a data response */
	private Object[] valueArray;
	/** A value of a response message */
	private Object singleValue;
	/** The response type of the message */
	private ResponseType response;

	/**
	 * Constructor to create Response Message based on response
	 */
	public ResponseMessage() {
		this.singleValue = "";
		this.response = null;
	}

	/**
	 * Parses a response String*
	 * 
	 * @param responseString
	 *            string of response message
	 * @return null if message is corrupted
	 * @return msg if message is valid
	 */
	public static ResponseMessage parse(String responseString) {
		int startCheck = responseString.indexOf("|");
		int endCheck = responseString.indexOf("}");
		int checkSum = 0;

		try {
			checkSum = Integer.parseInt(responseString.substring(
					startCheck + 1, endCheck));
		} catch (NumberFormatException e) // if we can't even parse the
											// checksum,
											// return null
		{
			return null;
		}

		int sum = 0;
		for (int i = 1; i < startCheck; ++i) {
			sum += responseString.charAt(i);
		}
		ResponseMessage msg = new ResponseMessage();
		if (checkSum + sum != -1) {
			return null;
		} else {
			int endCommand = responseString.indexOf(":");
			int ampersand = responseString.indexOf("&");
			String response = "";
			if (ampersand != -1) {
				response = responseString.substring(2, ampersand);
			} else {
				if (endCommand != -1) {
					response = responseString.substring(2, endCommand);
				} else {
					response = responseString.substring(2,
							responseString.indexOf("|"));
				}
			}
			if (response.compareTo("conn") == 0) {
				msg.response = ResponseType.CONN;
				msg.checksum = checkSum;
				msg.seqNum = Integer.parseInt(responseString.substring(1, 2));
				msg.messageString = responseString.substring(1,
						responseString.indexOf("|"));
				msg.formattedMessage = responseString;
			} else {
				//do nothing
			}
			if (response.compareTo("nack") == 0) {
				msg.response = ResponseType.NACK;
				msg.checksum = checkSum;
				msg.seqNum = Integer.parseInt(responseString.substring(1, 2));
				msg.messageString = responseString.substring(1,
						responseString.indexOf("|"));
				msg.formattedMessage = responseString;
			} else if (response.compareTo("ack") == 0) {
				msg.response = ResponseType.ACK;
				msg.singleValue = responseString.substring(endCommand + 1,
						startCheck);
				msg.checksum = checkSum;
				msg.seqNum = Integer.parseInt(responseString.substring(1, 2));
				msg.messageString = responseString.substring(1,
						responseString.indexOf("|"));
				msg.formattedMessage = responseString;
			} else if (response.compareTo("done") == 0) {
				msg.response = ResponseType.DONE;
				msg.singleValue = responseString.substring(endCommand + 1,
						startCheck);
				msg.checksum = checkSum;
				msg.seqNum = Integer.parseInt(responseString.substring(1, 2));
				msg.messageString = responseString.substring(1,
						responseString.indexOf("|"));
				msg.formattedMessage = responseString;
			} else if (response.compareTo("fail") == 0) {
				msg.response = ResponseType.FAIL;
				msg.singleValue = responseString.substring(endCommand + 1,
						startCheck - 1);
				msg.checksum = checkSum;
				msg.seqNum = Integer.parseInt(responseString.substring(1, 2));
				msg.messageString = responseString.substring(1,
						responseString.indexOf("|"));
				msg.formattedMessage = responseString;
			} else if (response.compareTo("error") == 0) {
				msg.response = ResponseType.ERROR;
				msg.singleValue = responseString.substring(endCommand + 1,
						startCheck);
				msg.checksum = checkSum;
				msg.seqNum = Integer.parseInt(responseString.substring(1, 2));
				msg.messageString = responseString.substring(1,
						responseString.indexOf("|"));
				msg.formattedMessage = responseString;
			} else if (response.compareTo("data") == 0) {
				msg.response = ResponseType.DATA;
				msg.checksum = checkSum;
				msg.seqNum = Integer.parseInt(responseString.substring(1, 2));
				msg.messageString = responseString.substring(1,
						responseString.indexOf("|"));
				msg.fieldArray = new String[] { "distance:", "light:",
						"sound:", "touch:", "claw:", "heading:", "speed:",
						"ultrasonic:" };
				int distance = (int)Double.parseDouble(responseString.substring(endCommand + 1,
								responseString.indexOf("&", endCommand)));
				int endLight = responseString.indexOf(":", endCommand + 1);
				int light = Integer.parseInt(responseString.substring(
						endLight + 1, responseString.indexOf("&", endLight)));
				int endSound = responseString.indexOf(":", endLight + 1);
				int sound = Integer.parseInt(responseString.substring(
						endSound + 1, responseString.indexOf("&", endSound)));
				int endTouch = responseString.indexOf(":", endSound + 1);
				int touch = Integer.parseInt(responseString.substring(
						endTouch + 1, responseString.indexOf("&", endTouch)));
				boolean touching = false;
				if (touch == 0) {
					touching = false;
				} else {
					touching = true;
				}
				int endClaw = responseString.indexOf(":", endTouch + 1);
				int claw = Integer.parseInt(responseString.substring(
						endClaw + 1, responseString.indexOf("&", endClaw)));
				int endHeading = responseString.indexOf(":", endClaw + 1);
				int heading = (int)Double
						.parseDouble(responseString.substring(endHeading + 1,
								responseString.indexOf("&", endHeading)));
				int endSpeed = responseString.indexOf(":", endHeading + 1);
				int speed = Integer.parseInt(responseString.substring(
						endSpeed + 1, responseString.indexOf("&", endSpeed)));
				int endUltra = responseString.indexOf(":", endSpeed + 1);
				int ultrasonic = Integer.parseInt(responseString.substring(
						endUltra + 1, responseString.indexOf("|", endUltra)));
				msg.valueArray = new Object[] { distance, light, sound,
						touching, claw, heading, speed, ultrasonic };
				msg.formattedMessage = responseString;
			} else if (response.compareTo("updr") == 0) {
				msg.response = ResponseType.UPDR;
				msg.checksum = checkSum;
				msg.seqNum = Integer.parseInt(responseString.substring(1, 2));
				msg.messageString = responseString.substring(1,
						responseString.indexOf("|"));
				msg.fieldArray = new String[] { "distance:", "light:",
						"sound:", "touch:", "claw:", "heading:", "speed:",
						"ultrasonic:", "connectionStatus:", "motorA:",
						"motorB:", "motorC:" };
				int distance = Integer
						.parseInt(responseString.substring(endCommand + 1,
								responseString.indexOf("&", endCommand)));
				int endLight = responseString.indexOf(":", endCommand + 1);
				int light = Integer.parseInt(responseString.substring(
						endLight + 1, responseString.indexOf("&", endLight)));
				int endSound = responseString.indexOf(":", endLight + 1);
				int sound = Integer.parseInt(responseString.substring(
						endSound + 1, responseString.indexOf("&", endSound)));
				int endTouch = responseString.indexOf(":", endSound + 1);
				String touch = responseString.substring(endTouch + 1,
						responseString.indexOf("&", endTouch));
				boolean touching = false;
				if (touch.compareTo("0") == 0) {
					touching = false;
				} else {
					touching = true;
				}
				int endClaw = responseString.indexOf(":", endTouch + 1);
				int claw = Integer.parseInt(responseString.substring(
						endClaw + 1, responseString.indexOf("&", endClaw)));
				int endHeading = responseString.indexOf(":", endClaw + 1);
				int heading = Integer
						.parseInt(responseString.substring(endHeading + 1,
								responseString.indexOf("&", endHeading)));
				int endSpeed = responseString.indexOf(":", endHeading + 1);
				int speed = Integer.parseInt(responseString.substring(
						endSpeed + 1, responseString.indexOf("&", endSpeed)));
				int endUltra = responseString.indexOf(":", endSpeed + 1);
				int ultrasonic = Integer.parseInt(responseString.substring(
						endUltra + 1, responseString.indexOf("&", endUltra)));
				int endConnStat = responseString.indexOf(":", endUltra + 1);
				int connStat = Integer.parseInt(responseString.substring(
						endConnStat + 1,
						responseString.indexOf("&", endConnStat)));
				boolean status = false;
				if (connStat == 0) {
					status = false;
				} else {
					status = true;
				}
				int endMotorA = responseString.indexOf(":", endConnStat + 1);
				int motorA = Integer.parseInt(responseString.substring(
						endMotorA + 1, responseString.indexOf("&", endMotorA)));
				int endMotorB = responseString.indexOf(":", endMotorA + 1);
				int motorB = Integer.parseInt(responseString.substring(
						endMotorB + 1, responseString.indexOf("&", endMotorB)));
				int endMotorC = responseString.indexOf(":", endMotorB + 1);
				int motorC = Integer.parseInt(responseString.substring(
						endMotorC + 1, responseString.indexOf("|", endMotorC)));
				msg.valueArray = new Object[] { distance, light, sound,
						touching, claw, heading, speed, ultrasonic, status,
						motorA, motorB, motorC };
				msg.formattedMessage = responseString;
			} else {
				//do nothing
			}
		}
		return msg;
	}

	/**
	 * Get method for field array
	 * 
	 * @return fieldArray array of fields
	 */
	public String[] getFieldArray() {
		return fieldArray;
	}

	/**
	 * Get method of value array
	 * 
	 * @return valueArray array of values
	 */
	public Object[] getValueArray() {
		return valueArray;
	}

	/**
	 * Get method for single value
	 * 
	 * @return singleValue single value object
	 */
	public Object getSingleValue() {
		return singleValue;
	}

	/**
	 * Get method for response type
	 * 
	 * @return response type of response message
	 */
	public ResponseType getResponse() {
		return response;
	}

	/**
	 * Set a specific index in fieldArray to field
	 * 
	 * @param index
	 *            index in fieldArray
	 * @param field
	 *            name of the field in fieldArray
	 */
	public void setFieldArray(int index, String field) {
		this.fieldArray[index] = field;
	}

	/**
	 * Set a specific index in valueArray to field
	 * 
	 * @param index
	 *            index in valueArray
	 * @param field
	 *            name of the field in valueArray
	 */
	public void setValueArray(int index, String field) {
		this.valueArray[index] = field;
	}

	/**
	 * Set singleValue to value
	 * 
	 * @param value
	 *            a message clarifying the response
	 */
	public void setSingleValue(Object value) {
		this.singleValue = value;
	}

	/**
	 * Enumerated response type to restrict types
	 * 
	 * @author Group 14
	 */
	public enum ResponseType {
		CONN, ACK, DONE, FAIL, DATA, ERROR, NACK, UPDR
	}
}
