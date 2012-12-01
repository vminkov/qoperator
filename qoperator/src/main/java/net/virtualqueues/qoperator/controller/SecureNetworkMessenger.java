package net.virtualqueues.qoperator.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import net.virtualqueues.qoperator.controller.responders.Responders;
import net.virtualqueues.qoperator.model.TicketType;

/**
 * Should we make it singleton?
 * @author Vicho
 *
 */
public class SecureNetworkMessenger implements Runnable{
	private static ObjectOutputStream outgoingSerial;
	private static ObjectInputStream incomingSerial;
	private static final InetAddress localhost = InetAddress.getLoopbackAddress();
	private static final int PORT = 2343;
	private static LinkedBlockingQueue<NetworkMessage> incomingMessagesQueue = new LinkedBlockingQueue<NetworkMessage>();
	private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private static final SecureNetworkMessenger instance = new SecureNetworkMessenger();
	
	
	public static SecureNetworkMessenger getSecureInstance(){
		return SecureNetworkMessenger.instance;
	}
	private SecureNetworkMessenger(){
		//do nothing.. this is in the main thread - it executes synchronously!
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
    		incomingSerial = new ObjectInputStream(inputstream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sendNewTicketType();
		executor.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				waitForMessages();
			}
		}, 0, 50, TimeUnit.MILLISECONDS);
		
		//in the rest of the lifetime...
		
	}
	private static void sendNewTicketType() {
		TicketType tt = new TicketType("the reason", 5, 3);
		NetworkMessage tobesent = new NetworkMessage(Responders.ADD_TICKET_TYPE, tt);
		try {
			outgoingSerial.writeObject(tobesent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//taken from the messenger:

	private void waitForMessages(){
		Object message = null;
        try {
				if ((message = incomingSerial.readObject()) != null) {
				NetworkMessage incomingMessage = (NetworkMessage) message;
				if(incomingMessage == null || incomingMessage.data == null ||
						incomingMessage.type == null || incomingMessage.type == ""){
					return;
				}
				try {
					incomingMessagesQueue.put( incomingMessage );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException | ClassNotFoundException e) {
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