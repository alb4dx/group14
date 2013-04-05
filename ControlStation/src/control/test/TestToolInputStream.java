package control.test;

import java.io.IOException;
import java.io.InputStream;


public class TestToolInputStream extends InputStream
{
	
	private TestComm	testTool;
	
	public TestToolInputStream(TestComm tool)
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
	
	@Override
	public int read(byte[] buf)
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
		
		int i = 0;
		for (byte b : testTool.byteQueue)
		{
			buf[i]=b;
			i++;
		}
		
		int size = testTool.byteQueue.size();
		testTool.byteQueue.clear();
		return size;
		
	}
	
}