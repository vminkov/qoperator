package net.virtualqueues.qoperator.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ExplorePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExplorePanel() {
		setPreferredSize(new Dimension(300, 150));
		setBackground(new Color(0,0,255));
		add(new JLabel("explore panel is... west"));
	}
}
