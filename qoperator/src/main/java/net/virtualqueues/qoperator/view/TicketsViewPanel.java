package net.virtualqueues.qoperator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.joda.time.DateTime;

public class TicketsViewPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TicketsViewPanel() {
		setPreferredSize(new Dimension(600, 450));
		setBackground(new Color(255,255,255));//127,127,127));
		setLayout(new BorderLayout());
		
		//Timeline for today
		add(new TimelineWrapper(new DateTime()));
		
		add(new ActionsPanel(), BorderLayout.SOUTH);
	}
}
