package net.virtualqueues.qoperator.model;

import java.io.Serializable;

public class TicketType implements Serializable {
	/**
	 * generated
	 */
	private static final long serialVersionUID = -5558035346929155305L;
		
	// the reason for the client's visit
	public String reason;
	
	// the expected duration of the visit (in seconds)
	public int duration;
	
	// a number for categorizing the tickets by same reasons
	// types will change by clients' wishes, so no enumerations
	public int type;
	
	/*
	 * Tickets will be created by the desktop and then sent over to 
	 * the queue board
	 */
	public TicketType(String reason_arg, int duration_arg, int type_arg){
		this.reason = reason_arg;
		this.duration = duration_arg;
		this.type = type_arg;
	}
}
