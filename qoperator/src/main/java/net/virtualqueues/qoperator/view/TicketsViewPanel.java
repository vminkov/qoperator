package net.virtualqueues.qoperator.view;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TicketsViewPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TicketsViewPanel() {
		setPreferredSize(new Dimension(600, 450));
		setBackground(new Color(255,0,0));
		add(new JLabel("Timeline!"));
	}
}
