package net.virtualqueues.qoperator.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import org.joda.time.DateTime;

public class ActionsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ActionsPanel(){
		JButton click = new JButton("click");
		JButton hand = new JButton("hand");
		JButton select = new JButton("select");
		JButton plus = new JButton("plus");
		JButton minus = new JButton("minus");
		JButton del = new JButton("del");
		JButton cut = new JButton("cut");
		JButton paste = new JButton("paste");
		
		plus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// XXX should we check the exact source of the event?
				JButton plus = (JButton) event.getSource();
				ActionsPanel ap = (ActionsPanel) plus.getParent();
				TicketsViewPanel parent = (TicketsViewPanel) ap.getParent();
				parent.getTlw().zoomPlus();
			}
		});
		minus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// XXX should we check the exact source of the event?
				JButton minus = (JButton) event.getSource();
				ActionsPanel ap = (ActionsPanel) minus.getParent();
				TicketsViewPanel parent = (TicketsViewPanel) ap.getParent();
				
				parent.getTlw().zoomMinus();
			}
		});

		add(click);
		click.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// XXX should we check the exact source of the event?
				JButton click = (JButton) event.getSource();
				ActionsPanel ap = (ActionsPanel) click.getParent();
				TicketsViewPanel parent = (TicketsViewPanel) ap.getParent();
				DateTime twelveoclock = new DateTime();
				twelveoclock = twelveoclock.minus(twelveoclock.getMillisOfDay()).plusHours(12);
				parent.getTlw().setTimelinePosition(twelveoclock);

			}
		});
		add(hand);
		add(select);
		add(plus);
		add(minus);
		add(del);
		add(cut);
		add(paste);
	}
}
