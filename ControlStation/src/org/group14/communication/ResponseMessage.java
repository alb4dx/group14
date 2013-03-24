package org.group14.communication;

public class ResponseMessage extends Message
{
	private String[] fieldArray;
	private Object[] valueArray;
	private Object singleValue;
	private ResponseType response;
	
	private ResponseMessage(ResponseType response, Object value)
	{
		//TODO
	}
	
	private ResponseMessage(ResponseType response, String[] fields, Object[] values)
	{
		//TODO
	}
	
	public static ResponseMessage parse(String responseString)
	{
		//TODO
		return null;
	}
	
	public String[] getFieldArray()
	{
		return fieldArray;
	}

	public Object[] getValueArray()
	{
		return valueArray;
	}

	public Object getSingleValue()
	{
		return singleValue;
	}

	public ResponseType getResponse()
	{
		return response;
	}

	public enum ResponseType
	{
		CONN, ACK, DONE, FAIL, DATA, ERROR, NACK
	}
}
