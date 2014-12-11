/**
 * TCP Example Client Application
 * 
 * @author CS467, Fall 2014
 */
public class ExampleTCPClient {
	
	// -----------------------------------------------------------------
	// Demo program to show how to send a message to a server and receive
	// a response message back from the server using TCP.
	public static void main(String[] args) {
		
    final String serverName = "localhost";
    final int    serverPort = 6789;
    
    // Start the connection to the server.
    TCPClientConnection client = new TCPClientConnection(serverName, serverPort);
    if (! client.connectionValid()) {
    	System.out.println("A TCP connection could not be established to the server.");
    	System.exit(0);
    } else {
    	System.out.println("TCP client: " + client);
    }
    
    // Send a message to the server
    String message = "Example message to server";
    System.out.println("Sending message: " + message);
    client.sendMessage(message);
    
    // Wait for a response message from the server.
    String response = client.receiveMessage();
    
    // Do something with the response.
    System.out.println("Response was: '" + response + "'");

    // Close the TCP connection.
    client.close();
    System.out.println("Session Complete!  Client Terminated.");
	}
}
