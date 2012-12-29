package net.virtualqueues.qoperator.view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLayeredPane;

public class TimelineWrapper extends JLayeredPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String timelineBackgroundPath = "./res/timeline.png";
	private static BufferedImage image;

	private static int MAX_WIDTH = 0;
	private static int MAX_HEIGHT = 0;
	private static int MIN_WIDTH = 0;
	private static int MIN_HEIGHT = 0;
	private static int IMG_WIDTH = 0;
	private static int IMG_HEIGHT = 0;
	private static int WIDTH = 0;
	private static int HEIGHT = 0;
	private static int START_HEIGHT = 0;
	private static int START_WIDTH = 0;
	private static int AREA_HEIGHT = 0;
	private static int AREA_WIDTH = 0;

	private static int[] LAST_X = new int[100];
	private static int LAST_POINTER_POSITION = 0;
	private static int OFFSET = 0;
	
	public TimelineWrapper(){
		File imgFile = new File(timelineBackgroundPath);
		try{
			TimelineWrapper.image = ImageIO.read(imgFile);
			MAX_WIDTH = image.getWidth();
			MAX_HEIGHT = image.getHeight();
			MIN_WIDTH = image.getWidth()/2;
			MIN_HEIGHT = image.getHeight()/2;
			IMG_WIDTH = image.getWidth();
			IMG_HEIGHT = image.getHeight();
		}catch(IOException e){
			e.printStackTrace();
		}
		computeDimensions();
		
		setLayout(new GridBagLayout());
		
		//GridBagConstraints c = new GridBagConstraints();
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent event) {}
			@Override
			public void mouseDragged(MouseEvent event) {
				TimelineWrapper source = (TimelineWrapper) event.getSource();
				Component[] components = getComponents();

				int i = 0;
				
				for(Component comp : components){
					comp.setLocation(LAST_X[i] + event.getX(), comp.getY());
					i++;
				}
				OFFSET += event.getX() - LAST_POINTER_POSITION ;
				
				i=0;
				for(Component comp : components){
					LAST_X[i] = comp.getX() - event.getX();
					i++;
				}
				LAST_POINTER_POSITION = event.getX();
				source.repaint();//XXX isn't this too heavy?
			}
		});
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent event) {
				/*TimelineWrapper source = (TimelineWrapper) event.getSource();
				Component[] components = source.getComponents();
				int i=0;
				for(Component comp : components){	
					comp.setLocation( LAST_X[i] + event.getX(), comp.getY());
					i++;
				}
				OFFSET +=  event.getX() - LAST_POINTER_POSITION ;
				source.repaint();*/
			}
			@Override
			public void mousePressed(MouseEvent event) {
				Component[] comps = getComponents();
				int i = 0;
				for(Component comp : comps){
					LAST_X[i] = comp.getX() - event.getX();
					i++;
				}
				LAST_POINTER_POSITION = event.getX();

			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
		});
		
		TicketObject tickObj = new TicketObject("Label is label");
		add(tickObj, JLayeredPane.DRAG_LAYER.intValue());
		
		TicketObject tickObj2 = new TicketObject("Another label");
		tickObj2.setForeground(new Color(255,122,0));
		tickObj2.setBackground(new Color(122,0,255));
		add(tickObj2, JLayeredPane.DRAG_LAYER.intValue());
		
		Graphics g = image.getGraphics();
		
		//g.drawString("wtf is this", 50 , 50);
		
		add(new Canvas());
	}
	
	private void computeDimensions()
	{
		WIDTH = getWidth();
		HEIGHT = getHeight();
	
		if(image != null)
		{
			if(WIDTH < MIN_WIDTH || HEIGHT < MIN_HEIGHT){//the area is too small to fit a usable timeline - make it paint outside
				AREA_WIDTH = MIN_WIDTH;
				AREA_HEIGHT = MIN_HEIGHT;
				START_HEIGHT = 0;
				if((HEIGHT - AREA_HEIGHT) / 2 > 0)
					START_HEIGHT = (HEIGHT - AREA_HEIGHT) / 2;		
				START_WIDTH = 0;
				if((WIDTH - AREA_WIDTH) / 2 > 0)
					START_WIDTH = (WIDTH - AREA_WIDTH) / 2;				
			}
			else if(WIDTH < MAX_WIDTH || HEIGHT < MAX_HEIGHT)//the area is still not large enough to fit the timeline, so we adjust
			{
				if(HEIGHT * IMG_WIDTH  > IMG_HEIGHT * WIDTH){//equivalent to height/width > img.height / img.width ; tall rectangle area
					AREA_WIDTH = WIDTH;
					AREA_HEIGHT = (int) (((float)WIDTH/(float)IMG_WIDTH)*(float)IMG_HEIGHT);
					START_HEIGHT = 0;
					if((HEIGHT - AREA_HEIGHT) / 2 > 0)
						START_HEIGHT = (HEIGHT - AREA_HEIGHT) / 2;
					START_WIDTH = 0;					
				}
				else{//height/width <= img.height / img.width ; wide rectangle area
					AREA_WIDTH = (int) (((float)HEIGHT/(float)IMG_HEIGHT)*(float)IMG_WIDTH);
					AREA_HEIGHT = HEIGHT;
					START_WIDTH = 0;
					if((WIDTH - AREA_WIDTH) / 2 > 0)
						START_WIDTH = (WIDTH - AREA_WIDTH) / 2;
					START_HEIGHT = 0;
				}
			}else{//the area is large enough - put the timeline in the middle
				AREA_WIDTH = MAX_WIDTH;
				AREA_HEIGHT = MAX_HEIGHT;
				START_WIDTH = 0;
				if((WIDTH - AREA_WIDTH) / 2 > 0)
					START_WIDTH = (WIDTH - AREA_WIDTH) / 2;
				START_HEIGHT = 0;
				if((HEIGHT - AREA_HEIGHT) / 2 > 0)
					START_HEIGHT = (HEIGHT - AREA_HEIGHT) / 2;
			}
		}
	}
	
	
	/**
	 * Makes the timeline background resize in accordance with the timeline area size.
	 * Use the properties MAX/MIN_TILE_WIDTH/HEIGHT in the constructor for adjustments
	 */
	/*@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		computeDimensions();
		
		g.drawImage(image, START_WIDTH,  START_HEIGHT,
					AREA_WIDTH, AREA_HEIGHT, this );
	}
	*/	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		computeDimensions();

		if(image != null)
		{
			float columns = (((float) getWidth()) / ((float) AREA_WIDTH));
			for(int i=-1; i <= columns + 1; i++){
				OFFSET %= AREA_WIDTH;
				g.drawImage(image, i*AREA_WIDTH + OFFSET , START_HEIGHT, AREA_WIDTH, AREA_HEIGHT, this );
				//System.out.println(columns + " " + i*START_WIDTH  + " " + OFFSET);
			}
			
		}
	}
	/* 
	 * Nice way to fill the area with tiled background 
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		computeDimensions();

		if(image != null)
		{
			int imageWidth = image.getWidth();
			int imageHeight = image.getHeight();
			
			int columns = (getWidth() / imageWidth);
			int rows = (getHeight() / imageHeight);
			for(int i=0; i <= columns; i++){
				for(int j=0; j <= rows; j++){
					g.drawImage(image, i*imageWidth, j*imageHeight, imageWidth, imageHeight, this );
				}
			}
		}
	}
	*/
}

