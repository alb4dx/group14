package control.communication;
/**
 * 
 * The ResponseMessage class inherits the standard functionality of a Message and is specific to 
 * 
 * @version 1.0 - Build 04/01/2013
 * @author Stephanie Colen
 * @author Sarina Padilla
 * @author Hubert Chen
 * @author Andy Barron
 * @author John Zambrotta
 * 
 */
public class ResponseMessage extends Message
{
	private String[] fieldArray;
	private Object[] valueArray;
	private Object singleValue;
	private ResponseType response;
	
	/**
	 * Two variable constructor to create Response Message based on response type and 
	 * value
	 * @param response type of response
	 * @param value *
	 */
	private ResponseMessage(ResponseType response, Object value)
	{
		//TODO
	}
	
	/**
	 * Three parameter constructor to define response message by response type, array of fields
	 * and array of values
	 * @param response 	type of response message
	 * @param fields *
	 * @param values *
	 */
	private ResponseMessage(ResponseType response, String[] fields, Object[] values)
	{
		//TODO
	}
	
	/**
	 * Parses a response String*
	 * @param responseString  string of response message
	 * @return TBD
	 */
	public static ResponseMessage parse(String responseString)
	{
		//TODO
		return null;
	}
	
	/**
	 * Get method for field array
	 * @return fieldArray	array of fields
	 */
	public String[] getFieldArray()
	{
		return fieldArray;
	}

	/**
	 * Get method of value array
	 * @return valueArray	arry of values
	 */
	public Object[] getValueArray()
	{
		return valueArray;
	}
	/**
	 * Get method for single alue
	 * @return singleValue	single value object
	 */
	public Object getSingleValue()
	{
		return singleValue;
	}

	/**
	 * Get method for response type
	 * @return response		type of response message
	 */
	public ResponseType getResponse()
	{
		return response;
	}
	/**
	 * Enumerated response type to restrict types
	 * @author Group 14
	 *
	 */
	public enum ResponseType
	{
		CONN, ACK, DONE, FAIL, DATA, ERROR, NACK
	}
}
