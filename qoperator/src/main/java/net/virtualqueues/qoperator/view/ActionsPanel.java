package net.virtualqueues.qoperator.view;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ActionsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ActionsPanel(){
		add(new JButton("click"));
		add(new JButton("hand"));
		add(new JButton("select"));
		add(new JButton("plus"));
		add(new JButton("minus"));
		add(new JButton("del"));
		add(new JButton("cut"));
		add(new JButton("paste"));
	}
}
