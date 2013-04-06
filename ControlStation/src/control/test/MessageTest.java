package control.test;

import control.communication.CommandMessage;
import control.communication.ResponseMessage;
import control.communication.CommandMessage.CommandType;

public class MessageTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CommandMessage msg = new CommandMessage(CommandType.QUERY);
		/*CommandMessage msg = new CommandMessage(CommandType.CLAW, 0.25);
		//int msgCheck=0;
		//for(int i=0; i<msg.messageString.length();++i){
			//msgCheck +=msg.messageString.charAt(i);
		//}
		//System.out.println("command Message checksum(recieving side) is:"+~msgCheck);
		System.out.println("messageString:"+msg.getMessageString());
		System.out.println("formattedString:"+msg.getFormattedMessage());
		System.out.println("SeqNum:"+msg.getSeqNum());
		System.out.println("command:"+msg.getCommand());
		CommandMessage msg2 = new CommandMessage(CommandType.TURN, 90);
		System.out.println("messageString:"+msg2.getMessageString());
		System.out.println("formattedString:"+msg2.getFormattedMessage());
		System.out.println("SeqNum:"+msg2.getSeqNum());
		System.out.println("command:"+msg2.getCommand());*/
		System.out.println("messageString:"+msg.getMessageString());
		System.out.println("formattedString:"+msg.getFormattedMessage());
		String responseTest ="1data&distance:5&light:10&sound:15&touch:1&claw:0.0&heading:30&speed:360&ultrasonic:10";
		int sum2=0;
		for(int i=0; i<responseTest.length();++i){
			sum2 +=responseTest.charAt(i);
		}
		System.out.println("Response Checksum:"+~sum2);
		responseTest +="|"+~sum2;
		responseTest = "{"+responseTest+"}";
		ResponseMessage test = new ResponseMessage();
		test = ResponseMessage.parse(responseTest);
		//if(test!=null)
		System.out.println("message string:"+test.getMessageString()+ " "+"formattedString:"+test.getFormattedMessage());
		
		for(int j = 0; j < test.getFieldArray().length; ++j){
			System.out.println(test.getFieldArray()[j] + test.getValueArray()[j]);
		}
	}
}
