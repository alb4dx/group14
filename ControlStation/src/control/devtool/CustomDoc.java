package control.devtool;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;


@SuppressWarnings("serial")
public class CustomDoc extends DefaultStyledDocument
{
	JTextPane	myText;
	int			myTag;
	CommPanel	myComm;
	boolean		sendCommand;
	
	public CustomDoc(JTextPane text, int tag, CommPanel comm, boolean b)
	{
		myText = text;
		myTag = tag;
		myComm = comm;
		sendCommand = b;
	}
	
	public void insertString(int offs, String str, AttributeSet a)
	{
		if (myText.getFont().getSize() == 15 && getLength() < 25 // custom
																	// message
				&& str.length() == 1 && str.charAt(0) != 13)
		{
			try
			{
				super.insertString(offs, str, a); // Insert the string
			}
			catch (BadLocationException e)
			{ // Catch the bad location
			// exception
				System.out.println("Bad location exception - exiting");
				System.exit(0);
			}
		}
		else if (myText.getFont().getSize() == 12 && getLength() < 15 // telemetry
																		// update
				&& str.length() == 1)
		{
			try
			{
				super.insertString(offs, str, a); // Insert the string
				if (sendCommand)
				{
					myComm.updateDataCommand(myTag, myText.getText());
				}
			}
			catch (BadLocationException e)
			{ // Catch the bad location
			// exception
				System.out.println("Bad location exception - exiting");
				System.exit(0);
			}
		}
	}
	
	public void changeCommand()
	{
		sendCommand = true;
	}
}
