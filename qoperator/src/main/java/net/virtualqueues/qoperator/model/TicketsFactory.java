package net.virtualqueues.qoperator.model;

import java.util.LinkedList;
import java.util.List;

public class TicketsFactory {
	/*
	 * Singleton
	 */
	private final static TicketsFactory instance = new TicketsFactory();
	private final static List<TicketType> types = new LinkedList<TicketType>();
	
	public final static int DEFAULT_TICKET_TYPE = 0;
	
	private TicketsFactory(){			
	}
	
	public static TicketsFactory getInstance(){
		return instance;
	}
	
	public boolean addTicketType(String reason_arg, int duration_arg, int type_arg){
		for(TicketType tt : types){
			if(tt.type == type_arg)
				return false;
		}
		
		types.add(new TicketType(reason_arg, duration_arg, type_arg));
		return true;
	}
	
	public boolean removeTicketType(int type_arg){
		for(TicketType tt : types){
			if(tt.type == type_arg)
				types.remove(tt);
				return true;
		}
		return false;
	}
	
	public TicketType getTicketByType(int type){
		for(TicketType tt : types){
			if(tt.type == type)
				return tt;
		}
		for(TicketType tt : types){
			if(tt.type == DEFAULT_TICKET_TYPE)
				return tt;
		}
		TicketType defaultTicketType = new TicketType("default message", 0, DEFAULT_TICKET_TYPE);
		types.add(defaultTicketType);
		return defaultTicketType;
	}
	
}
