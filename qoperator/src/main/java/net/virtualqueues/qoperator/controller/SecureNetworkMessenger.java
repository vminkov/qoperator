package net.virtualqueues.qoperator.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import net.virtualqueues.controller.MessageResponder;
import net.virtualqueues.controller.Messages;
import net.virtualqueues.controller.NetworkMessage;
import net.virtualqueues.model.TicketType;
import net.virtualqueues.qoperator.controller.responders.AddTicketResponder;

/**
 * Should we make it singleton?
 * @author Vicho
 *
 */
public class SecureNetworkMessenger implements Runnable{
	private static Map<String, MessageResponder> responders = new HashMap<String, MessageResponder>();
	private static ObjectOutputStream outgoingSerial;
	private static ObjectInputStream incomingSerial;
	private static final InetAddress localhost = InetAddress.getLoopbackAddress();
	private static final int PORT = 2343;
	private static MessageQueue incomingMessagesQueue = MessageQueue.getInstance();
	private static final SecureNetworkMessenger instance = new SecureNetworkMessenger();
	
	
	public static SecureNetworkMessenger getSecureInstance(){
		return SecureNetworkMessenger.instance;
	}
	private SecureNetworkMessenger(){
        /*
         * Here we should add the various message responders?
         */
        
		registerResponder(new AddTicketResponder());
		
	}
	
	private void registerResponder(MessageResponder responder) {
		responders.put(responder.getType(), responder);
	}
	public static MessageResponder getResponder(String msgType){
		return responders.get(msgType);
	}
	@Override 
	public void run(){	
		SSLServerSocketFactory sslserversocketfactory =
                    (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslserversocket;
		try {
			System.out.println("Desktop starting at port " + PORT);
			sslserversocket = (SSLServerSocket) sslserversocketfactory.createServerSocket();
			//certificate is made with: "keytool -genkey keystore -keyalg RSA"
			//this below is a 'hack' (the hack was adding all cipher suites, this is the only one we need
			sslserversocket.setEnabledCipherSuites(new String[]{"SSL_DH_anon_WITH_3DES_EDE_CBC_SHA"});
			
			sslserversocket.bind(new InetSocketAddress(localhost, PORT));
			
			SSLSocket sslsocket = (SSLSocket) sslserversocket.accept();

            InputStream inputstream = sslsocket.getInputStream();
            OutputStream outputstream  = sslsocket.getOutputStream();
    	    
			outgoingSerial = new ObjectOutputStream(outputstream);
    		incomingSerial = new ObjectInputStream(inputstream){
//    			@Override
//				public Object readObject(){
//    				
//    			}
    		};
		} catch (IOException e) {
			e.printStackTrace();
		}
		sendNewTicketType();
//		executor.scheduleAtFixedRate(new Runnable() {
//			
//			@Override
//			public void run() {
		while(true){
				waitForMessages();
		}
//				System.out.println("are we finished?");
//
//			}
//		}, 0, 50, TimeUnit.MILLISECONDS);
		
		//in the rest of the lifetime...
		
	}
	private static void sendNewTicketType() {
		TicketType tt = new TicketType("the reason", 5, 3);
		NetworkMessage tobesent = new NetworkMessage(Messages.ADD_TICKET_TYPE, tt);
		try {
			outgoingSerial.writeObject(tobesent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//taken from the messenger:

	private void waitForMessages(){
		Object message = null;
		System.out.println("x");
        try {
			if ((message = incomingSerial.readObject()) != null) {
				NetworkMessage incomingMessage = (NetworkMessage) message;
				System.out.println("recieved it!");
				if(incomingMessage == null || incomingMessage.getData() == null ||
						incomingMessage.getType() == null || incomingMessage.getType() == ""){
					System.out.println("it is corrupt!");

					return;
				}
				try {
					System.out.println("getting on board..");
					incomingMessagesQueue.put( incomingMessage );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
        }
	}
	
	public boolean sendMessage(String msgType, Serializable data){
		NetworkMessage message = new NetworkMessage(msgType,data);
		try {
			outgoingSerial.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}