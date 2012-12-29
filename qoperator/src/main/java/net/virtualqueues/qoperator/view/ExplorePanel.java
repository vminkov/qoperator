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
		setPreferredSize(new Dimension(200, 150));
		setBackground(new Color(255,200,0));
		add(new JLabel("explore panel is... west"));
	}
}
