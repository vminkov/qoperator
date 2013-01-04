package net.virtualqueues.qoperator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

import org.joda.time.DateTime;

import net.virtualqueues.qoperator.model.Ticket;
import net.virtualqueues.qoperator.model.TicketType;
import net.virtualqueues.qoperator.model.TicketsFactory;

public class TicketObject extends JLabel implements Comparator<TicketObject> {

	/**
	 * default
	 */
	private static final long serialVersionUID = 1L;
	private int INITIAL_MOUSE_POSITION = 0;
	public final Ticket ticketModel;
	private static int DEFAULT_DURATION = 10;
	private int X_BEFORE_MOVE = 0;

	public TicketObject(String label, int uniqueID, DateTime startDate, int position){
		super(label + uniqueID);
		
		ticketModel = new Ticket(label, DEFAULT_DURATION, uniqueID, startDate, TicketsFactory.DEFAULT_TICKET_TYPE);
		
		this.setLocation(position, this.getY());
		this.setBorder(BorderFactory.createEtchedBorder());
		this.setPreferredSize(new Dimension(240, 90));
		this.setBackground(new Color(0,255,0));		
		this.setOpaque(true);
		this.add(new JButton("click here!"));
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent event) {

			}
			
			@Override
			public void mouseDragged(MouseEvent event) {
				TicketObject source = (TicketObject) event.getSource() ;
				int newPosition = source.getX() + event.getX() - INITIAL_MOUSE_POSITION;
				source.setLocation(
						newPosition,//- source.getWidth()/2,
						source.getY()// + event.getY() - source.getHeight()/2 //uncomment to move in the Y direction, too :)
				);
			}
		});
		
		addMouseListener(new MouseListener() {
			

			@Override
			public void mouseReleased(MouseEvent event) {
				TicketObject source = (TicketObject) event.getSource() ;
				int deltaX = event.getXOnScreen() - X_BEFORE_MOVE;
				TimelineWrapper parent = (TimelineWrapper) source.getParent();
				ticketModel.moveInTime(parent.getTimeDeltaMillis(deltaX, ticketModel.getStartDate()));
			}
			
			@Override
			public void mousePressed(MouseEvent event) {
				TicketObject source = (TicketObject) event.getSource() ;
				INITIAL_MOUSE_POSITION = event.getX(); 
				X_BEFORE_MOVE = event.getXOnScreen();
				TimelineWrapper parent = (TimelineWrapper) source.getParent();
				parent.moveToFront(source);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	
	
	/**
	 * Note: this comparator imposes orderings that are inconsistent with equals.
	 */
	@Override
	public int compare(TicketObject tick1, TicketObject tick2) {
		int pos1 = tick1.getX();
		int pos2 = tick2.getX();
		
		if(pos1 < pos2) return -1;
		else 
			if(pos1 > pos2) return 1;
		return 0;
	}
}
