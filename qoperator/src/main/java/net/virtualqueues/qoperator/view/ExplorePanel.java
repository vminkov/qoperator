package net.virtualqueues.qoperator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.color.ColorSpace;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ExplorePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Color status = new Color(255,0,0);//new Color(ColorSpace.TYPE_HSV);
	private static final int COLOR_SPEED = 8;
	public ExplorePanel() {
		setPreferredSize(new Dimension(200, 150));
		setBackground(new Color(255,200,0));
		JLabel connectivity = new JLabel("connection status");
		connectivity.setBackground(status);
		connectivity.setOpaque(true);
		connectivity.setPreferredSize(new Dimension(90, 50));
		
		connectivity.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				JLabel source = (JLabel) event.getSource();
				int red = status.getRed();
				int green = status.getGreen();
			
				if(event.getWheelRotation()  > 0){
					if(red<255 && green<255){
						red+=COLOR_SPEED;
					}else if(green<255){
						green+=COLOR_SPEED;
					}else if(red>0){
						red-=COLOR_SPEED;
					}
				}
				else{
					if(red<255 && green<255){
						green+=COLOR_SPEED;
					}else if(red<255){
						red+=COLOR_SPEED;
					}else if(green>0){
						green-=COLOR_SPEED;
					}
				}
				if(red>255){red=255;}
				if(green>255){green=255;}
				if(red<0){red=0;}
				if(green<0){green=0;}
				
				source.setBackground(status);
			}
		});
		
		add(connectivity);
		add(new JLabel("explore panel is... west"));
	}
}
