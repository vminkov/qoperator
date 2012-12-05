package net.virtualqueues.qoperator.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ControlPanel() {
		setPreferredSize(new Dimension(100, 150));
		setBackground(new Color(0,255,0));
		add(new JLabel("control panel is... south"));
	}
}
