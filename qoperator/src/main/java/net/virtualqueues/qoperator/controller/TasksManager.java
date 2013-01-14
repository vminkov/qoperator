package net.virtualqueues.qoperator.controller;

import net.virtualqueues.controller.MessageParsingException;
import net.virtualqueues.controller.MessageResponder;
import net.virtualqueues.controller.NetworkMessage;
import net.virtualqueues.qoperator.controller.MessageQueue;

/** 
 * @author Vicho
 * This should be singleton, as multiple instances might be on different threads
 * and read/write the same data objects.
 * 
 **/
public class TasksManager implements Runnable {
	private static final MessageQueue messageQueue = MessageQueue.getInstance();
	private static final TasksManager instance = new TasksManager();
		
	public static TasksManager getInstance(){
		return instance;
	}
	
	private TasksManager(){
		//THE CONSTRUCTORS ARE EXECUTED IN THE MAIN THREAD! (THIS MEANS IN SINGLE THREAD MODE)
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
		String msgType = messageTask.getType();
		System.out.println(msgType);
		MessageResponder responder = SecureNetworkMessenger.getResponder(msgType);
		responder.handleMessage(messageTask.getData());		
	}

}
