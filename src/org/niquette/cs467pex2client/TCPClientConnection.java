package org.niquette.cs467pex2client;
import java.net.*;
import java.nio.charset.Charset;
import java.io.*;

/**
 * TCP Client which allows messages to be sent to a server and the server's
 * response retrieved.
 * 
 * @author Dr. Wayne Brown, Fall 2014
 */
public class TCPClientConnection {
	private Socket		   socket;
	private OutputStream   out;
	private InputStream	   in;
	public int serverPort;
	public String serverName;

	// -------------------------------------------------------------------
	/**
	 * Constructor: Create an instance of the TCP client class.
	 * 
	 * @param serverName
	 *          A string that is the name or IP address of the server
	 * @param serverPort
	 *          The port number of the server
	 */
	public TCPClientConnection(String serverName, int serverPort) {

		socket = null;
		out = null;
		in = null;

		// Create the TCP socket
		try {
			// Create a socket to connect to the server.
			socket = new Socket(serverName, serverPort);
			
		} catch (UnknownHostException error) {
			System.err.println("UnknownHostException when trying to create a TCPClient");
			error.printStackTrace();
		} catch (IOException error) {
			System.err.println("Socket Exception when trying to create a TCPClient");
			error.printStackTrace();
		}

		if (socket != null) {
			// Get the socket's TCP output stream -- which includes the TCP buffers
			try {
				out = socket.getOutputStream();
			} catch (IOException error) {
				System.err.println("IOException when trying to get the socket's output stream.");
				System.err.println("The IOException error was '" + error.getMessage() + "'");
				error.printStackTrace();
				return;
			}
			
			try {
				in = socket.getInputStream();
			} catch (IOException error) {
				System.err.println("IOException when trying to get the socket's input stream.");
				System.err.println("The IOException error was '" + error.getMessage() + "'");
				error.printStackTrace();
				return;
			}
		}

	}

	// -------------------------------------------------------------------
	public boolean connectionValid() {
		return socket != null;
	}
	
	// -------------------------------------------------------------------
	public void setTimeoutLength(int milliseconds) {
		try {
			// The read() method of the input stream will block, waiting for an
			// incoming message. If you set a timeout value, the read() method
			// will not wait forever, but only wait the amount of time you specify.
			socket.setSoTimeout(milliseconds);
		} catch (SocketException error) {
			System.err.print("A socket exception occurred when the timeout length was set.");
			error.printStackTrace();
		}	
	}
	
	// -------------------------------------------------------------------
	/**
	 * Send a message to the server.
	 * 
	 * @param message
	 *          The string message to send to the server
	 */
	public boolean sendMessage(String message) {
		boolean messageSentSuccessfully = false;

		if (socket != null && !socket.isClosed()) {
			try {
				if (out != null) {
					// Convert the message into an array of bytes
					byte [] byteArray = message.getBytes();
					
					// Send the array of bytes to the server
					out.write(byteArray);
				}
				messageSentSuccessfully = true;

			} catch (IOException error) {
				System.err.println("IO exception in sendRequest");
				error.printStackTrace();
			}
		}

		return messageSentSuccessfully;
	}

	// -------------------------------------------------------------------
	/**
	 * Get a response from the server. If a timeout occurs, a null is returned.
	 * 
	 * @return A ChatMessage that contains the response from the server, or a NULL
	 *         if the request timed out.
	 */
	public String receiveMessage() {
		String response = null;
		byte [] buffer = new byte[300];
		
		if (socket != null && !socket.isClosed()) {
			try {
				if (in != null) {
					// Get a response message from the server.
					int numBytes = in.read(buffer);
					
					if (numBytes > 0) {
						response = new String(buffer,  0, numBytes, Charset.defaultCharset());
					} else {
						response = "";
					}
				}

			} catch (IOException error) {
				System.err.println("IO error in getResponse: " + error.getMessage());
				// error.printStackTrace();
			}
		}

		return response;
	}

	// -------------------------------------------------------------------
	/**
	 * Close a TCP connection
	 */
	public void close() {
		// Close the output stream.
		try {
			out.close();
		} catch (Exception error) {
			System.err.println("For " + this);
			System.err.println("IO exception when closing the TCP output stream.");
			error.printStackTrace();
		}

		// Close the input stream.
		try {
			in.close();
		} catch (Exception error) {
			System.err.println("For " + this);
			System.err.println("IO exception when closing the TCP input stream.");
			error.printStackTrace();
		}

		// Close the TCP connection
		try {
			socket.close();
		} catch (Exception error) {
			System.err.println("For " + this);
			System.err.println("IO exception when closing the TCP socket.");
			error.printStackTrace();
		}
	}

	// -------------------------------------------------------------------
	public String toString() {
		return "Socket from " + socket.getLocalAddress().getHostAddress() + ":"
				+ socket.getLocalPort() + " to "
				+ socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
	}

}
