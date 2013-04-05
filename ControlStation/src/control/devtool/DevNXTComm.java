package control.devtool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTInfo;

public class DevNXTComm implements NXTComm
{

	
	private RobotSimulator	testTool;
	private DevNXTInputStream stream;

	public DevNXTComm(RobotSimulator tool)
	{
		this.testTool = tool;
		this.stream = new DevNXTInputStream(tool);
	}
	
	@Override // one of two methods we care about here
	public void write(byte[] data) throws IOException
	{
		// TODO pass byte array to dev tool
		testTool.messageFromStation("RECEIVED: " + new String(data));
	}
	
	@Override // the other one
	public InputStream getInputStream()
	{
		return stream;
	}
	
	
	@Override
	public void close() throws IOException
	{
		// empty
		
	}

	@Override
	public byte[] sendRequest(byte[] arg0, int arg1) throws IOException
	{
		// empty
		return null;
	}

	@Override
	public int available() throws IOException
	{
		// empty
		return 0;
	}

	@Override
	public OutputStream getOutputStream()
	{
		// empty
		return null;
	}

	@Override
	public boolean open(NXTInfo arg0) throws NXTCommException
	{
		// empty
		return false;
	}

	@Override
	public boolean open(NXTInfo arg0, int arg1) throws NXTCommException
	{
		// empty
		return false;
	}

	@Override
	public byte[] read() throws IOException
	{
		// empty
		return null;
	}

	@Override
	public NXTInfo[] search(String arg0, int arg1) throws NXTCommException
	{
		// empty
		return null;
	}
	
}
