package net.virtualqueues.qoperator.controller;

import java.io.Serializable;

public class NetworkMessage implements Serializable {
	/**
	 * generated
	 */
	private static final long serialVersionUID = -6138556437643363586L;
	
	public String type;
	public Serializable data;
	public NetworkMessage(String type, Serializable data){
		this.type = type;
		this.data = data;
	}
}
