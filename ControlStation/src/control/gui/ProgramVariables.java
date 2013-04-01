package control.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * TODO: 
 * action listeners - update table
 * 
 * @author slc4ga
 *
 */

public class ProgramVariables extends JPanel{
	
	private JLabel variableLabel;
	private JTable myTable;
	private Object[][] myVariables;
	private JButton requestUpdate;
	
	public ProgramVariables() {
		
		setLayout(new FlowLayout(FlowLayout.LEADING, 8, 5));
		setPreferredSize(new Dimension(250, 410));
		
		variableLabel = new JLabel("Program Variables");
		variableLabel.setFont(new Font("Arial", Font.BOLD, 15));
		
		String[] cols = {"Variable", "Value"};
		
		Object[][] variables = {{"Speed", new Integer(5)}, 
				{"Connection", new Boolean(true)}, {"Orientation", "N"},
				{"Location", new Integer(5)}, {"Claw Position", new Double(.5)},
				{"Touch", new Boolean(false)}, {"Ultrasonic", new Integer(12)}, 
				{"Light", "50%"}, {"Sound", "15%"}, {"Motor A", new Integer(37)},
				{"Motor B", new Integer(89)}, {"Motor C", new Integer(45)}};
		myVariables = new Object[variables.length][variables[0].length];
		for(int x = 0; x < variables.length; x++) {
			for(int y = 0; y < variables[0].length; y++) {
				myVariables[x][y] = variables[x][y];
			}
		}
		
		myTable = new JTable(myVariables, cols);
		myTable.setFont(new Font("Arial", Font.PLAIN, 13));
		myTable.setPreferredSize(new Dimension(125, 310));
		
		for(int i = 0; i < myVariables[0].length; i++){
			TableColumn col = myTable.getColumnModel().getColumn(i);
			col.setPreferredWidth(20);
		}
		myTable.setRowHeight(26);
		
		DefaultTableModel tableModel = new DefaultTableModel(myVariables, cols) {
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};

		myTable.setModel(tableModel);
		
		JScrollPane sp = new JScrollPane(myTable);
		sp.setPreferredSize(new Dimension(235, 335));
		sp.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		
		requestUpdate = new JButton("Request Update");
		requestUpdate.setFont(new Font("Arial", Font.PLAIN, 13));
		requestUpdate.setPreferredSize(new Dimension(235, 35));
		
		add(variableLabel);
		add(sp);
		add(requestUpdate);
		
	}
	
	public JLabel getVariableLabel() {
		return variableLabel;
	}
	
	public JTable getMyTable() {
		return myTable;
	}
	
	public Object[] getMyVariables() {
		return myVariables;
	}
	
	public JButton getRequestUpdate() {
		return requestUpdate;
	}

}
