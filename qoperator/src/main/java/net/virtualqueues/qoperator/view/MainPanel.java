package net.virtualqueues.qoperator.view;

import java.awt.BorderLayout;
import javax.swing.JPanel;

public class MainPanel extends JPanel {

	/**
	 * XXX make it singleton?
	 */
	private static final long serialVersionUID = 1L;
	private static final TicketsViewPanel tvp = new TicketsViewPanel();
	private static final ExplorePanel ep = new ExplorePanel();
	private static final ControlPanel cp = new ControlPanel();
	
	public MainPanel(){

		this.setLayout(new BorderLayout());
		this.add(cp, BorderLayout.SOUTH);

		this.add(ep, BorderLayout.WEST);
		this.add(tvp, BorderLayout.CENTER);
	}

	public static TicketsViewPanel getTvp() {
		return tvp;
	}

	public static ExplorePanel getEp() {
		return ep;
	}

	public static ControlPanel getCp() {
		return cp;
	}
	
	
}
