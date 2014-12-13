package org.niquette.cs467pex2client;

public class ListenerThread implements Runnable {

	private TCPClientConnection connection;
	
	public ListenerThread(TCPClientConnection connection){
		this.connection = connection;
	}
	
	@Override
	public void run() {
		do{
			String response = connection.receiveMessage();
			System.out.println("[Response] "+response);		
		} while (true);
		
	}



}
