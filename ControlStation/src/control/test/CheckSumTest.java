package control.test;

import java.util.ArrayList;

public class CheckSumTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> list = new ArrayList<String>();
		list.add("0ack");
		list.add("1ack");
		list.add("0nack");
		list.add("1nack");
		list.add("0done:init");
		list.add("1done:init");
		list.add("0fail:init");
		list.add("1fail:init");
		list.add("0done:move");
		list.add("1done:move");
		list.add("0fail:move");
		list.add("1fail:move");
		for(int i =0; i<list.size(); ++i){
			int sum = 0;
			for(int j=0; j<list.get(i).length(); ++j){
				sum  += list.get(i).charAt(j);
			}
			System.out.println(list.get(i)+":"+~sum);
		}
		
	}

}
