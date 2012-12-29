package net.virtualqueues.qoperator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

public class TicketObject extends JLabel {

	/**
	 * default
	 */
	private static final long serialVersionUID = 1L;
	private int INITIAL_MOUSE_POSITION = 0;

	public TicketObject(String label){
		super(label);
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
				source.setLocation(
						source.getX() + event.getX() - INITIAL_MOUSE_POSITION,//- source.getWidth()/2,
						source.getY()// + event.getY() - source.getHeight()/2 //uncomment to move in the other direction, too :)
						);
			}
		});
		
		addMouseListener(new MouseListener() {
			

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent event) {
				TicketObject source = (TicketObject) event.getSource() ;
				INITIAL_MOUSE_POSITION = event.getX();
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
}
