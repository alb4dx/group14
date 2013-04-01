package control.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.*;


/**
 * TODO:
 * action listeners
 * 
 * @author Steph
 *
 */
public class OtherCommands extends JPanel{

	private JLabel otherLabel;
	private JCheckBox autonomous;
	private JButton halt;
	private JButton power;
	private JButton reset;
	
	public OtherCommands() {
		
		setPreferredSize(new Dimension(250, 160));
		setLayout(new FlowLayout(FlowLayout.LEADING, 8, 3));
		
		otherLabel = new JLabel("Other Commands");
		otherLabel.setFont(new Font("Arial", Font.BOLD, 15));
		
		autonomous = new JCheckBox("Autonomous Mode");
		autonomous.setFont(new Font("Arial", Font.PLAIN, 14));
		
		halt = new JButton("Halt All Actions");
		halt.setFont(new Font("Arial", Font.PLAIN, 14));
		halt.setPreferredSize(new Dimension(235, 33));
		
		power = new JButton("Power Down");
		power.setFont(new Font("Arial", Font.PLAIN, 14));
		power.setPreferredSize(new Dimension(235, 33));
		
		reset = new JButton("Reset");
		reset.setFont(new Font("Arial", Font.PLAIN, 14));
		reset.setPreferredSize(new Dimension(235, 33));
		
		
		add(otherLabel);
		add(autonomous);
		add(halt);
		add(power);
		add(reset);
		
	}
	
	public boolean getAutonomous() {
		return autonomous.isSelected();
	}
	
	public JButton getHalt() {
		return halt;
	}
	
	public JButton getPower() {
		return power;
	}
	
	public JButton getReset() {
		return reset;
	}
}
