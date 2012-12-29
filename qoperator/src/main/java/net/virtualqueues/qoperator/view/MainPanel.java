package net.virtualqueues.qoperator.view;

import java.awt.BorderLayout;
import javax.swing.JPanel;

public class MainPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainPanel(){

		this.setLayout(new BorderLayout());
		this.add(new ControlPanel(), BorderLayout.SOUTH);

		this.add(new ExplorePanel(), BorderLayout.WEST);
		this.add(new TicketsViewPanel(), BorderLayout.CENTER);
		System.out.println("gui should be now visible");
	}
}
