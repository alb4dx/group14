package control.test;

import java.io.IOException;
import java.io.InputStream;


public class TestToolInputStream extends InputStream
{
	
	private TestTool	testTool;
	
	public TestToolInputStream(TestTool tool)
	{
		this.testTool = tool;
	}
	
	@Override
	public int read() throws IOException
	{
		
		synchronized (testTool.lock)
		{
			while (testTool.byteQueue.isEmpty())
			{
				try
				{
					testTool.lock.wait();
				}
				catch (InterruptedException e)
				{
				}
			}
		}
		
		return testTool.byteQueue.remove();
	}
	
}
