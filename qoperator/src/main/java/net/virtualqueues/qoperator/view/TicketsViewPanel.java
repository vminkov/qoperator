package net.virtualqueues.qoperator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

import org.joda.time.DateTime;

public class TicketsViewPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final TimelineWrapper tlw = new TimelineWrapper(new DateTime());
	public final ActionsPanel ap = new ActionsPanel();
	public TicketsViewPanel() {
		setPreferredSize(new Dimension(600, 450));
		setBackground(new Color(255,255,255));//127,127,127));
		setLayout(new BorderLayout());
		System.out.println(new DateTime(1234567).getMillis());
		//Timeline for today
		add(tlw);
		
		add(ap, BorderLayout.SOUTH);
	}
	

	public static TimelineWrapper getTlw() {
		return tlw;
	}
}
