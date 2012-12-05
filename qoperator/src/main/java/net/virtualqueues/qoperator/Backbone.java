package net.virtualqueues.qoperator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.virtualqueues.qoperator.controller.SecureNetworkMessenger;
import net.virtualqueues.qoperator.controller.TasksManager;
import net.virtualqueues.qoperator.view.QOpGUI;

public class Backbone {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
        System.setProperty("java.net.useSystemProxies", "false");	
        TasksManager tm = TasksManager.getInstance();
		
		executor.scheduleAtFixedRate(tm, 0, 200, TimeUnit.MILLISECONDS );
		System.out.println("task manager running");	
        
		//we want dependency injection here
		executor.execute((Runnable) SecureNetworkMessenger.getSecureInstance());
		System.out.println("netowork messenger running");
		
		//we want dependency injection here
		executor.execute(new QOpGUI());
		System.out.println("gui running");
				
	}
}
