package control.test;

import java.io.InputStream;

import control.communication.MessageListener;
import control.communication.ResponseMessage;
import control.main.Controller;

public class TestToolMessageListener extends MessageListener
{

	public TestToolMessageListener(TestTool tool)
	{
		super(null, tool.testInputStream);
	}
	
	@Override
	protected void processMessage(ResponseMessage rsp)
	{
		System.out.println("CONTROL STATION received: " + rsp);
	}
	
}
