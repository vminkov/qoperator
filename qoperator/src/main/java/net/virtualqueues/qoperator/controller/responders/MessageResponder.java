package net.virtualqueues.qoperator.controller.responders;

import java.io.Serializable;

public interface MessageResponder {
	String getType();
	void handleMessage(Serializable data);
	
}
