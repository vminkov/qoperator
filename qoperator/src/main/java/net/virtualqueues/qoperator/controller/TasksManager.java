package net.virtualqueues.qoperator.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import net.virtualqueues.qoperator.controller.MessageQueue;
import net.virtualqueues.qoperator.controller.responders.*;

/** 
 * @author Vicho
 * This should be singleton, as multiple instances might be on different threads
 * and read/write the same data objects.
 * 
 **/
public class TasksManager implements Runnable {
	private Map<String, MessageResponder> responders = new HashMap<String, MessageResponder>();
	public static final MessageQueue messageQueue = MessageQueue.getInstance();
	private static final TasksManager instance = new TasksManager();
		
	public static TasksManager getInstance(){
		return instance;
	}
	
	private TasksManager(){
		//THE CONSTRUCTORS ARE EXECUTED IN THE MAIN THREAD! (THIS MEANS IN SINGLE THREAD MODE)
        /*
         * Here we should add the various message responders?
         */
        
		registerResponder(new MessageResponder() {
			
			@Override
			public void handleMessage(Serializable data) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public String getType() {
				// TODO Auto-generated method stub
				return null;
			}
		});
		
	}
	
	private void registerResponder(MessageResponder responder) {
		responders.put(responder.getType(), responder);
	}

	@Override
	public void run() {
		if(!messageQueue.isEmpty()){
			try {
				parseMessageTask(messageQueue.take());
			} catch (InterruptedException | MessageParsingException e) {
				e.printStackTrace();
			}
		}
	}

	private void parseMessageTask(final NetworkMessage messageTask) throws MessageParsingException {
		String msgType = messageTask.type;
		MessageResponder responder = this.responders.get(msgType);
		responder.handleMessage(messageTask.data);		
	}

}
