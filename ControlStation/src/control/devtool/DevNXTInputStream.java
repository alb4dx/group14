package control.devtool;

import java.io.IOException;
import java.io.InputStream;



public class DevNXTInputStream extends InputStream
{
	
	private RobotSimulator	robotSim;
	
	public DevNXTInputStream(RobotSimulator tool)
	{
		this.robotSim = tool;
	}
	
	@Override
	public int read() throws IOException
	{
		
		synchronized (robotSim.lock)
		{
			while (robotSim.byteQueue.isEmpty())
			{
				try
				{
					robotSim.lock.wait();
				}
				catch (InterruptedException e)
				{
				}
			}
		}
		
		return robotSim.byteQueue.remove();
	}
	
	@Override
	public int read(byte[] buf)
	{
		synchronized (robotSim.lock)
		{
			while (robotSim.byteQueue.isEmpty())
			{
				try
				{
					robotSim.lock.wait();
				}
				catch (InterruptedException e)
				{
				}
			}
		}
		
		int i = 0;
		for (byte b : robotSim.byteQueue)
		{
			buf[i]=b;
			i++;
		}
		
		int size = robotSim.byteQueue.size();
		robotSim.byteQueue.clear();
		return size;
		
	}
	
}
