package control.devtool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

import javax.swing.Timer;

import control.gui.GraphicsInterface;
import control.communication.*;
import control.communication.ResponseMessage.ResponseType;

public class RobotSimulator {

	public final Object lock = new Object();
	public final Queue<Byte> byteQueue = new LinkedList<Byte>();
	public final DevNXTComm testNXTComm = new DevNXTComm(this);
	public final InputStream testInputStream = new DevNXTInputStream(this);
	public DevToolWindow myWindow = null;
	private Timer msgTimer;
	private String responseMessage;
	private RobotState myState;
	private boolean automate = true;

	public RobotSimulator(DevToolWindow window) {
		myWindow = window;
		ActionListener timeOut = new ActionListener() {
			public void actionPerformed(ActionEvent arg) {
				System.out.println("Robot timeout occured");
				simulateResponse(responseMessage);
			}
		};
		msgTimer = new Timer(10000, timeOut);
		this.responseMessage = null;
		myState = RobotState.WAITCOMMAND;
	}

	// private final Scanner scan = new Scanner(System.in);

	// public static void main(String[] args)
	// {
	// new GraphicsInterface();
	// RobotSimulator r = new RobotSimulator();
	// r.launchCommandLine();
	// }

	// public void launchCommandLine()
	// {
	// System.out.println("Command line test tool now active.");
	// String input = new String();
	// do
	// {
	// System.out.println("Enter text to send from simulated robot, or \"exit\" to quit.");
	// input = scan.nextLine();
	//
	//
	// simulateResponse(input);
	//
	//
	// } while (!input.equals("exit"));
	// System.out.println("Toodles!");
	// }

	public Timer getMsgTimer() {
		return msgTimer;
	}

	public void setMsgTimer(Timer msgTimer) {
		this.msgTimer = msgTimer;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public void messageFromStation(String msg) {

		System.out.println(msg);
		// what type of message do we have?
		// what type of response do we send back?
		// compose command into a string
		// call simulateResponse(String response)
		myWindow.getComm().getMessageArea()
				.append("\n" + "Message Received:" + msg);
		if (myState == RobotState.WAITCOMMAND) {
			if (msgTimer.isRunning()) {
				msgTimer.stop();
			}
			if (!msg.substring(2, 5).equals("ack")) {
				// System.out.println("not ack so robot state is"+RobotState.SENDINGRESPONSE1);
				myState = RobotState.SENDINGRESPONSE1;
			}
		}
		if (automate && !msg.substring(2, 5).equals("ack"))
			automateResponse(msg);
	}

	public void simulateResponse(String response) {
		System.out.println("RobotState:" + myState);
		int sum = 0;
		for (int i = 0; i < response.length(); ++i) {
			sum += response.charAt(i);
		}
		response = "{" + response + "|" + ~sum + "}";
		ResponseMessage msg = ResponseMessage.parse(response);
		myWindow.getComm().getMessageArea()
				.append("\n" + "Message Sent:" + msg.getFormattedMessage());
		if (myState == RobotState.SENDINGRESPONSE1) {
			// System.out.println("state is"+RobotState.SENDINGRESPONSE2);
			if (msg.getResponse() != ResponseType.NACK)
				myState = RobotState.SENDINGRESPONSE2;

		} else if (myState == RobotState.SENDINGRESPONSE2) {
			// System.out.println("state is"+RobotState.WAITCOMMAND);
			myState = RobotState.WAITCOMMAND;
			msgTimer.start();
		} else if (myState == RobotState.WAITCOMMAND) {
			msgTimer.restart();
		}
		synchronized (lock) {
			for (byte b : msg.getFormattedMessage().getBytes()) {
				//System.out.print(b);
				byteQueue.add(b);
			}

			lock.notifyAll();
		}
	}

	public enum RobotState {
		WAITCOMMAND, SENDINGRESPONSE1, SENDINGDATA, SENDINGRESPONSE2
	}

	private void automateResponse(String msg) {
		int seqNum = Integer.parseInt(msg.substring(1, 2));
		String fillerMsg = seqNum + "ack";
		System.out.println("auto ack" + fillerMsg);
		responseMessage = fillerMsg;
		simulateResponse(fillerMsg);
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int locColon = msg.indexOf(":");
		int locPipe = msg.indexOf("|");
		String fillerMsg2 = "";
		if (locColon < 0) {
			fillerMsg2 = msg.substring(2, locPipe);
		} else {
			fillerMsg2 = msg.substring(2, locColon);
		}
		if (!fillerMsg2.equals("query")) {
			fillerMsg2 = seqNum + "done:" + fillerMsg2;
			responseMessage = fillerMsg2;
			System.out.println("auto message:" + fillerMsg2);
			simulateResponse(fillerMsg2);
		} else {
			Random randomNum = new Random();
			int distance = randomNum.nextInt(301);
			int ultra = randomNum.nextInt(256);
			int sound = randomNum.nextInt(101);
			int speed = randomNum.nextInt(721);
			int heading = randomNum.nextInt(360) - 180;
			int touch = randomNum.nextInt(2);
			int light = randomNum.nextInt(101);
			int claw = randomNum.nextInt(1440) - 720;

			String data = seqNum + "data&distance:" + distance + "&light:"
					+ light + "&sound:" + sound + "&touch:" + touch + "&claw:"
					+ claw + "&heading:" + heading + "&speed:" + speed
					+ "&ultrasonic:" + ultra;
			System.out.println("auto:" + data);
			simulateResponse(data);
		}
	}
}
