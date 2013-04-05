package control.test;

import control.communication.MessageListener;
import control.communication.ResponseMessage;

public class TestToolMessageListener extends MessageListener
{

	public TestToolMessageListener(TestComm tool)
	{
		super(null, tool.testInputStream);
	}
	
	@Override
	protected void processMessage(ResponseMessage rsp)
	{
		System.out.println("CONTROL STATION received: " + rsp);
	}
	
	@Override
	protected void processInvalidMessage(String str)
	{
		System.out.println("CONTROL STATION rcv INVALID: " + str);
	}
	
}
