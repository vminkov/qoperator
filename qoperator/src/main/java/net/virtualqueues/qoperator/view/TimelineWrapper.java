package net.virtualqueues.qoperator.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import net.virtualqueues.model.Ticket;
import net.virtualqueues.model.TicketsFactory;
import net.virtualqueues.qoperator.controller.DailyTicketsManager;
import net.virtualqueues.qoperator.model.DailyTickets;

import org.joda.time.DateTime;

public class TimelineWrapper extends JLayeredPane {
	private static final int DEFAULT_TICKET_HEIGHT = 120;
	private static final long serialVersionUID = 1L;
	private static final String timelineBackgroundPath = "./res/timeline.png";
	private static BufferedImage image;
	private final DateTime day;
	private final DailyTicketsManager dtm = new DailyTicketsManager(new DailyTickets());
	private final List<TicketObject> ticketLabels = new ArrayList<TicketObject>();
	private static int CURRENT_CENTER_POSITION = 0;
	
	private static int MAX_WIDTH = 0;
	private static int MAX_HEIGHT = 0;
	private static int MIN_WIDTH = 0;
	private static int MIN_HEIGHT = 0;
	private static int IMG_WIDTH = 0;
	private static int IMG_HEIGHT = 0;
	private static int WIDTH = 0;
	private static int HEIGHT = 0;
	private static int START_HEIGHT = 0;
	private static int AREA_HEIGHT = 0;
	private static int AREA_WIDTH = 0;
	private static double ZOOM_FACTOR = 1;
	private static double ZOOM_PLUS = 1.25;
	private static double ZOOM_MINUS = 0.8;
	private static int[] LAST_X = new int[1024];
	private static int LAST_POINTER_POSITION = 0;
	
	private static double IMAGE_TIMESPAN = 1.0;
	private static double PIXELS_PER_HOUR = 0;
	public TimelineWrapper(DateTime day_arg){
		day = day_arg;

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
		
		setLayout(null);
		
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
				CURRENT_CENTER_POSITION -= (event.getX() - LAST_POINTER_POSITION);
				
				i=0;
				for(Component comp : components){
					LAST_X[i] = comp.getX() - event.getX();
					i++;
				}
				LAST_POINTER_POSITION = event.getX();
				
				
				source.repaint();//XXX isn't this too heavy?
			}
		});
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				if(event.getWheelRotation()  < 0){
					zoomPlus();
				}
				else{
					zoomMinus();
				}
			}
		});
		
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent event) {}
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

		ticketLabels.add(new TicketObject(TicketsFactory.getTicketFromType(null, twelveoclock(9), dtm.getUniqueOrderID())));
		ticketLabels.add(new TicketObject(TicketsFactory.getTicketFromType(null, twelveoclock(35), dtm.getUniqueOrderID())));

		add(ticketLabels.get(0), JLayeredPane.DRAG_LAYER.intValue());
		
		ticketLabels.get(1).setForeground(new Color(255,122,0));
		ticketLabels.get(1).setBackground(new Color(122,0,255));
		add(ticketLabels.get(1), JLayeredPane.DRAG_LAYER.intValue());

		JLabel greetings = new JLabel("Hi, please click me!");
		greetings.setPreferredSize(new Dimension(300,400));
		greetings.setBounds(0, 0,720 , 300);
		greetings.setLocation(0, 0);
		//greetings.setBackground(new Color(70,70,70));
		greetings.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		greetings.setOpaque(true);
		greetings.addMouseListener(new MouseListener() {			
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent event) {
				Component source = (Component) event.getSource();
				Container parent = source.getParent();
				parent.remove(source);
				rearrange();
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
		});
		add(greetings);
		moveToFront(greetings);
		
		
		//TODO: put component listener that arranges the tickets on resize
		addComponentListener(new ComponentListener() {
				@Override
			public void componentShown(ComponentEvent e) {
				rearrange();
			}
			@Override
			public void componentResized(ComponentEvent e) {
				rearrange();
			}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {		}
		});
		
		
		
		setTimelinePosition(new DateTime(2013, 1, 12, 6, 00));
	}
	
	private void rearrange(){
		computeDimensions();
		for(TicketObject tickObj : ticketLabels){
			int newPosition = getPosition(tickObj.ticketModel.getStartDate()) + WIDTH/2;
			int newEnd = getPosition(tickObj.ticketModel.getEndDate()) + WIDTH/2;
			int newWidth = newEnd - newPosition;
			//tickObj.setLocation(newPosition, tickObj.getY());
			//tickObj.setSize(newWidth, tickObj.getHeight());
			
			tickObj.setBounds(newPosition, (HEIGHT - DEFAULT_TICKET_HEIGHT)/2, newWidth, DEFAULT_TICKET_HEIGHT);

		}
		repaint();
	}
	public int getPosition(final DateTime time) {
		//computeDimensions();
		long dayMillis = time.getMillisOfDay();

		//offset in accordance with 0 o'clock's position
		double midnightOffset = ((double) dayMillis / (60 * 60 * 1000)) * (double) PIXELS_PER_HOUR;

		return (int) midnightOffset - CURRENT_CENTER_POSITION ; 
	}
	public int getPositionToMidnight(final DateTime time) {
		//computeDimensions();
		long dayMillis = time.getMillisOfDay();

		//offset in accordance with 0 o'clock's position
		double midnightOffset = ((double) dayMillis / (60 * 60 * 1000)) * (double) PIXELS_PER_HOUR;

		return (int) midnightOffset; 
	}
	private DateTime twelveoclock(int minutesOffset){
//		//AREA_WIDTH equals 1.5 hours (1:30), see IMAGE_TIMESPAN
//		final long MINS_IN_AN_HOUR = 60;
//		final long SECS_IN_A_MIN = 60;
//		final long MILLIS_IN_A_SEC = 1000;

		DateTime newTicketDate = new DateTime(day.getMillis() - day.getMillisOfDay() +
				(((6) * 60) + minutesOffset) * 60 * 1000);

		return newTicketDate;
	}
	
	private DateTime getTime(int absoluteOffset){
		long millis = day.getMillis() - day.getMillisOfDay() + ( (long) ( ((double) (((long) absoluteOffset)* 60 * 60 * 1000)) / PIXELS_PER_HOUR));
		DateTime centerTime = new DateTime(millis);
		//System.out.println(millis + "   time(millis) " + centerTime);
		return centerTime;
	}

	public int getPixelDeltaInMillis(int xDelta){
		//AREA_WIDTH equals 1.5 hours (1:30), see IMAGE_TIMESPAN
		final long MINS_IN_AN_HOUR = 60;
		final long SECS_IN_A_MIN = 60;
		final long MILLIS_IN_A_SEC = 1000;
		int timeDeltaMillis = 0;
		
		if(xDelta != 0)
			timeDeltaMillis = ((int) ((double)( xDelta  * MINS_IN_AN_HOUR * SECS_IN_A_MIN * MILLIS_IN_A_SEC) / PIXELS_PER_HOUR));
		
		return timeDeltaMillis;
	}
	
	private void computeDimensions()
	{
		int oldAreaWidth = AREA_WIDTH;
		
		WIDTH = getWidth();
		HEIGHT = getHeight();
		
		MIN_WIDTH =  image.getWidth()/2;
		MIN_HEIGHT = image.getHeight()/2;
		MAX_WIDTH = image.getWidth();
		MAX_HEIGHT = image.getHeight();
		
		MIN_WIDTH *= ZOOM_FACTOR;
		MIN_HEIGHT *= ZOOM_FACTOR;
		MAX_WIDTH *= ZOOM_FACTOR;
		MAX_HEIGHT *= ZOOM_FACTOR;
		
		if(image != null)
		{
			if(WIDTH < MIN_WIDTH || HEIGHT < MIN_HEIGHT){//the area is too small to fit a usable timeline - make it paint outside
				AREA_WIDTH = MIN_WIDTH;
				AREA_HEIGHT = MIN_HEIGHT;
				START_HEIGHT = 0;
				if((HEIGHT - AREA_HEIGHT) / 2 > 0)
					START_HEIGHT = (HEIGHT - AREA_HEIGHT) / 2;
				if((WIDTH - AREA_WIDTH) / 2 > 0) {
				}				
			}
			else if(WIDTH < MAX_WIDTH || HEIGHT < MAX_HEIGHT)//the area is still not large enough to fit the timeline, so we adjust
			{
				if(HEIGHT * IMG_WIDTH  > IMG_HEIGHT * WIDTH){//equivalent to height/width > img.height / img.width ; tall rectangle area
					AREA_WIDTH = WIDTH;
					AREA_HEIGHT = (int) (((float)WIDTH/(float)IMG_WIDTH)*(float)IMG_HEIGHT);
					START_HEIGHT = 0;
					if((HEIGHT - AREA_HEIGHT) / 2 > 0)
						START_HEIGHT = (HEIGHT - AREA_HEIGHT) / 2;					
				}
				else{//height/width <= img.height / img.width ; wide rectangle area
					AREA_WIDTH = (int) (((float)HEIGHT/(float)IMG_HEIGHT)*(float)IMG_WIDTH);
					AREA_HEIGHT = HEIGHT;
					if((WIDTH - AREA_WIDTH) / 2 > 0) {
					}
					START_HEIGHT = 0;
				}
			}else{//the area is large enough - put the timeline in the middle
				AREA_WIDTH = MAX_WIDTH;
				AREA_HEIGHT = MAX_HEIGHT;
				if((WIDTH - AREA_WIDTH) / 2 > 0) {
				}
				START_HEIGHT = 0;
				if((HEIGHT - AREA_HEIGHT) / 2 > 0)
					START_HEIGHT = (HEIGHT - AREA_HEIGHT) / 2;
			}
			PIXELS_PER_HOUR = ((double) AREA_WIDTH / IMAGE_TIMESPAN);
		}
		
		CURRENT_CENTER_POSITION *= ((float)AREA_WIDTH/(float)oldAreaWidth);
	}
	
	public void zoomPlus(){
		MIN_WIDTH =  image.getWidth()/2;
		MIN_HEIGHT = image.getHeight()/2;
		MAX_WIDTH = image.getWidth();
		MAX_HEIGHT = image.getHeight();
	
		if(MAX_WIDTH * ZOOM_FACTOR * ZOOM_PLUS< WIDTH || MAX_HEIGHT * ZOOM_FACTOR * ZOOM_PLUS < HEIGHT){
			ZOOM_FACTOR *= ZOOM_PLUS;
			//CURRENT_CENTER_POSITION *= ZOOM_PLUS;
		}
		rearrange();
	}
	
	public void zoomMinus(){
		if(ZOOM_FACTOR > 0.2){
			ZOOM_FACTOR *= ZOOM_MINUS;
			//CURRENT_CENTER_POSITION *= ZOOM_MINUS;

		}
		rearrange();
	}
	
	public void setTimelinePosition(final DateTime dt){
		CURRENT_CENTER_POSITION = 0;
		CURRENT_CENTER_POSITION = getPosition(dt);
		rearrange();
	}
	/**
	 * 
	 * @param t the Ticket to add
	 * @return true if successful, or false if not possible?
	 */
	public boolean addTicket(final Ticket t){
		TicketObject newTicket = new TicketObject(t);
		ticketLabels.add(newTicket);
		this.add(newTicket);
		
		return true;
	}
	/**
	 * Makes the timeline background resize in accordance with the timeline area size.
	 * Use the properties MAX/MIN_TILE_WIDTH/HEIGHT in the constructor for adjustments
	 */
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		computeDimensions();
		
		if(image != null)
		{
			float columns = (((float) getWidth()) / ((float) AREA_WIDTH)) + 1;
			int i = (int) ((- columns - 2)/2);
			int initial_i = i;
			
			int pixelsFor15MinutesInterval = (int) (PIXELS_PER_HOUR / 4);
			int firstZeroX = (- CURRENT_CENTER_POSITION % AREA_WIDTH) + CURRENT_CENTER_POSITION + i*AREA_WIDTH ;
			DateTime firstZeroTime = getTime(firstZeroX);
			//System.out.println(ZOOM_FACTOR + "      " + CURRENT_CENTER_POSITION);
			
			try{
				for(TicketObject t : ticketLabels){
					System.out.println(t.ticketModel);

				}
			}catch(IndexOutOfBoundsException e){System.out.println("no 3rd ticket");}

			
			for(;i <= (columns + 2)/2; i++){
				g.drawImage(image, i*AREA_WIDTH + (getWidth()/2) + (- CURRENT_CENTER_POSITION % AREA_WIDTH), START_HEIGHT, AREA_WIDTH, AREA_HEIGHT, this );
								
				for(int j=0; j < (int)(IMAGE_TIMESPAN/0.25); j++){
					int hour = firstZeroTime.plusMinutes(15*j + (i-initial_i)*60).getHourOfDay();
					int minuteOfDay = firstZeroTime.plusMinutes(15*j + (i-initial_i)*60).getMinuteOfDay();
					int minute = (minuteOfDay - hour*60);
					g.drawString(((hour < 10)?("0" + hour):hour)+":"+ ((minute < 10)?("0" + minute):minute), i*AREA_WIDTH + (getWidth()/2) + (- CURRENT_CENTER_POSITION % AREA_WIDTH) + j*pixelsFor15MinutesInterval, HEIGHT-20);
					
					//System.out.println(hour+":"+ (minute - hour*60) + " j" + j);
				}
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

