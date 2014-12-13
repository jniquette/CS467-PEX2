package org.niquette.cs467pex2client;

public class UpdateThread extends java.util.TimerTask{

	private TCPClientConnection connection;
	
	public UpdateThread(TCPClientConnection connection){
		this.connection = connection;
	}
	
	
	@Override
	public void run() {		
		System.out.println("Sending KEEP_ALIVE message");
		connection.sendMessage("KEEP_ALIVE|");
	}


}
