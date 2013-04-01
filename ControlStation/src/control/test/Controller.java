package control.test;

import java.util.ArrayList;

import control.gui.DebugInterface;
import control.gui.GraphicsInterface;
import control.communication.CommandMessage;

public class Controller {
	
	private static GraphicsInterface myGraphics;
	private ArrayList<CommandMessage> messageQueue = new ArrayList<CommandMessage>();
	//private DebugInterface myDebug = new DebugInterface(this);

	public static void main(String[] args) {
		myGraphics = new GraphicsInterface();
	}

}
