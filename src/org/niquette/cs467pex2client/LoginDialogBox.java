package org.niquette.cs467pex2client;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

// This is the first GUI window that gathers the SIGNOUT startup information
// and initiates a connection to the SIGNOUT server.

// Written by; Dr. Wayne Brown, Fall 2014
// Modified by: C2C Justin Niquette, Fall 2014

public class LoginDialogBox implements ActionListener {

	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new LoginDialogBox();
            }
        });
	}
	
	// GUI field names and default values
	private static String [] serverFieldNames = { "URL: ", "Port: " };
	private static String [] serverDefaultInputs = { "cdx.martincarlisle.com", "12345" };
	private static String [] userFieldNames = { "First name: ", "Last name: ", "Password: " };
	private static String [] userDefaultInputs = { "Justin", "Niquette", "1656" };

	// GUI components needed by the actionPerformed method.
	private JFrame frame;
	private JTextField [] serverInputs;
	private JTextField [] userInputs;
	private JButton connectButton;
	private JButton cancelButton;
	
	// -------------------------------------------------------------------
	// Constructor for the GUI window.
	public LoginDialogBox() {
		// Create the application window
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(350, 200));
		frame.setLocation(100, 100);
		frame.setTitle("Login to Cadet SIGNOUT");
		frame.setResizable(false);

		// Create a JPanel for the entire window and set a border around it.
		JPanel windowPanel = new JPanel(new BorderLayout());
		EmptyBorder panelBorder = new EmptyBorder(10,10,10,10);
		windowPanel.setBorder(panelBorder);
		windowPanel.setOpaque(true);
        frame.setContentPane(windowPanel);
        
        // Create panels to organize the GUI components
        JPanel serverPanel = new JPanel(new BorderLayout());
        Border serverNameBorder = BorderFactory.createTitledBorder("Server");
        Border serverBeveledBorder = BorderFactory.createLoweredBevelBorder();
        Border compoundServerBorder = BorderFactory.createCompoundBorder(
        		serverNameBorder, serverBeveledBorder);
        Border compoundServerBorder2 = BorderFactory.createCompoundBorder(
        		compoundServerBorder, panelBorder);
        serverPanel.setBorder( compoundServerBorder2 );
        
        JPanel userPanel = new JPanel(new BorderLayout());
        Border userNameBorder = BorderFactory.createTitledBorder("User");
        Border userBeveledBorder = BorderFactory.createLoweredBevelBorder();
        Border compoundUserBorder = BorderFactory.createCompoundBorder(
        		userNameBorder, userBeveledBorder);
        Border compoundUserBorder2 = BorderFactory.createCompoundBorder(
        		compoundUserBorder, panelBorder);
        userPanel.setBorder( compoundUserBorder2 );

        JPanel serverLabelPanel = new JPanel(new GridLayout(2,1));
        JPanel serverInputPanel = new JPanel(new GridLayout(2,1));
        JPanel userLabelPanel = new JPanel(new GridLayout(3,1));
        JPanel userInputPanel = new JPanel(new GridLayout(3,1));
        JPanel cmdPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
        // Organize the panels
        windowPanel.add(serverPanel, BorderLayout.NORTH);
        windowPanel.add(userPanel, BorderLayout.CENTER);
        windowPanel.add(cmdPanel, BorderLayout.SOUTH);
        
        serverPanel.add(serverLabelPanel, BorderLayout.WEST);
        serverPanel.add(serverInputPanel, BorderLayout.CENTER);
        
        userPanel.add(userLabelPanel, BorderLayout.WEST);
        userPanel.add(userInputPanel, BorderLayout.CENTER);

        // Create an array of inputs for the server information
        serverInputs = new JTextField[2];
        
        // Create the labels and JTextFields for the server information
        for (int j=0; j<2; j++) {
        	JLabel label = new JLabel(serverFieldNames[j]);
        	serverLabelPanel.add(label);
    		
    		serverInputs[j] = new JTextField(10);
    		serverInputs[j].setText(serverDefaultInputs[j]);
    		serverInputPanel.add(serverInputs[j]);
        }
        
        // Create an array of inputs for the user information
        userInputs = new JTextField[3];
        
        // Create the labels and JTextFields for the user information
        for (int j=0; j<3; j++) {
        	JLabel label = new JLabel(userFieldNames[j]);
        	userLabelPanel.add(label);
    		
    		userInputs[j] = new JTextField(10);
    		userInputs[j].setText(userDefaultInputs[j]);
    		userInputPanel.add(userInputs[j]);
        }
        
        // Create an action and cancel button
        connectButton = new JButton("Connect");
        cancelButton = new JButton("Cancel");
		cmdPanel.add(connectButton);
		cmdPanel.add(cancelButton);
		connectButton.addActionListener(this);
		cancelButton.addActionListener(this);

		// Make the window just large enough for all of its internal 
		// components and then make the window visible.
		frame.pack();
		frame.setVisible(true);
	}
	
	// -------------------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == cancelButton) {
			
			// Cancel the login by just closing the current dialog box.
			frame.dispose();
			
		} else if (event.getSource() == connectButton) {
			
			// Perform the connectButton action
			String serverName = serverInputs[0].getText().trim();
			int serverPort;
			try {
				serverPort = Integer.parseInt( serverInputs[1].getText().trim() );
			} catch (NumberFormatException error) {
				serverPort = 12345; // Default port
			}
			
			TCPClientConnection con = new TCPClientConnection(serverName, serverPort);
			
			User user = new User(userInputs[0].getText().trim(),  // firstName 
					             userInputs[1].getText().trim(),  // lastName
					             userInputs[2].getText().trim()); // password
		
			// Close the current dialog box.
			frame.dispose();
		
			// TODO: Start a thread to handle the client communication with the server
			// If the new thread can login to the server, it should start a
			// SignoutWindow.
			
			Thread signoutThread = new Thread((Runnable) new SignoutWindow(new LinkedList<CadetStatus>(), con, user));
			signoutThread.start();
			

		}
	}
}



