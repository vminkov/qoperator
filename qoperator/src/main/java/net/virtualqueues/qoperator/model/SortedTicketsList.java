package net.virtualqueues.qoperator.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.joda.time.DateTime;

/**
 * Initially designed to be a linked list , sorted by the start or end time of the tickets
 *	overlapping tickets are not allowed.
 * Singleton?
 * @author Vicho
 *
 */
public class SortedTicketsList {
	
	private static final LinkedList<Ticket> timeline = (LinkedList<Ticket>) Collections.synchronizedList(new LinkedList<Ticket>());
	//should this be synchronized, too?
	private static ArrayList<Ticket> timelineArray = (ArrayList<Ticket>) Collections.synchronizedList(new ArrayList<Ticket>());
	//used to determine if auto-save-in-array should be called
	private static boolean timelineChanged = false;
	
	private static final SortedTicketsList instance = new SortedTicketsList();
	private static final Ticket ticketAtTheEndOfTime = new Ticket("", 0, Integer.MAX_VALUE, new DateTime(Long.MAX_VALUE), TicketsFactory.DEFAULT_TICKET_TYPE);
	private SortedTicketsList(){		
	}
	
	public static SortedTicketsList getInstance(){
		return instance;
	}
	/**
	 * 
	 * @param tick
	 * @param simulate if true, the ticket will not be actually inserted, the result will rather tell if it is possible
	 * @return
	 */
	public boolean insert(final Ticket tick, boolean simulate){
		
		//starting in reverse order because tickets will most often be added on the last positions
		ListIterator<Ticket> i = timeline.listIterator(timeline.size());
		//we name the first ticket after this one the 'right' ticket on the 'timeline' and the one before this - the 'left'
		int index = timeline.size();
		
		Ticket left = null;
		Ticket right = ticketAtTheEndOfTime;
		
		while(i.hasPrevious()){
			left = i.previous();

			if(tick.overlaps(left)){
				return false;
			}
			if(tick.isBefore(right) && tick.isAfter(left)){
					if(!simulate){
						this.addAtIndex(index, tick);
					}
					return true;
			}
			--index;

			right = left;
		}
		
		if(!simulate){
			this.addAtIndex(index, tick);
		}
		return true;
	}
	/**
	 * Synchronized add at index, DO NOT USE W/O CHECKING BOUNDS
	 * TODO revise concurrency
	 * @param index
	 * @param tick
	 */
	private void addAtIndex(int index, Ticket tick) {
		synchronized(timeline){
			timelineChanged = true;
			timeline.add(index, tick);
		}
	}
	
	/**
	 * pseudo-linear operation?
	 * @param fromIndex
	 * @param toStartTime
	 * @return
	 */
	public boolean moveTicket(int fromIndex, DateTime toStartTime){
		if(fromIndex < 0 || fromIndex >= timeline.size())
			return false;
		
		Ticket toMove = timeline.get(fromIndex);
		if(insert(toMove, true)){
			timeline.remove(fromIndex);
			insert(toMove, false);
			return true;
		}
		return false;
	}
	/**
	 * we add this method so the timeline can be saved in other 
	 * form in that operations like searching are much faster
	 * This operation should not be called too often 
	 * @return
	 */
	private ArrayList<Ticket> timelineInArray(){
		Ticket[] result = SortedTicketsList.timeline.toArray(new Ticket[0]);
		 	
		return (ArrayList<Ticket>) Arrays.asList(result);
	}
	/**
	 * synchronized conversion from linked list to array, should be called rarely!
	 * TODO the output has to be checked that it's always sorted
	 */
	public void saveTimelineInArray(){
		ArrayList<Ticket> converted = timelineInArray();
		if(converted != null)
			synchronized(timelineArray){
				timelineArray = converted;
				timelineChanged = false;
			}
	}
	/**
	 * Binary search implementation
	 * @param time - the time may be between startDate and endDate of the ticket
	 * @return - the index of the ticket in the tickets array
	 * TODO check if it works
	 */
	public int searchTicket(DateTime time){
		//assure that the array of tickets is up to date:
		//
		if(SortedTicketsList.timelineChanged) 
			saveTimelineInArray();
		
		//binary search, interval left closed, right closed
		int left = 0;
		int right = timelineArray.size()-1;
		int middle = 0;
		
		while(left < right){
			middle = (right - left) / 2;
			Ticket middleTicket = timelineArray.get(middle);
			DateTime mTicketStartTime = middleTicket.getStartDate();
			DateTime mTicketEndTime = middleTicket.getEndDate();
			
			if(mTicketStartTime.isAfter(time)){
				right = middle;
			}
			else if(mTicketEndTime.isBefore(time)){
				left = middle;
			}
			else{
				//we are in the middle of the ticket
				return middle;
			}
		}
		
		//no tickets at this time
		return -1;
	}
	
}
