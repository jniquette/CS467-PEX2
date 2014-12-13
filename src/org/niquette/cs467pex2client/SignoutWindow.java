package org.niquette.cs467pex2client;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.TimerTask;
import java.util.Vector;
import java.util.Timer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

// This is the main window of the SIGNOUT client. It displays the table of
// status records and contains a menu for user actions.

// Written by: Dr. Wayne Brown, Fall 2014
// Modified by: C2C Justin Niquette, Fall 2014

public class SignoutWindow implements ActionListener, Runnable {

	// Uncomment this main program if you want to run the GUI by itself
	
//	public static void main(String[] args) {
//        //Schedule a job for the event-dispatching thread:
//        //creating and showing this application's GUI.
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//        		// Add bogus data to the statusList for debugging.
//            	LinkedList<CadetStatus> statusList = new LinkedList<CadetStatus>();
//        		statusList.add( new CadetStatus("Fred", "Smith", "2014/09/10 @ 1245 Wed", "2014/09/11 @ 1500 Thu", "2014/09/12 @ 0930 Fri", "Home") );
//        		statusList.add( new CadetStatus("Wayne", "Brown", "2014/09/10 @ 1245 Wed", "2014/09/10 @ 1245 Wed", "2014/09/10 @ 1245 Wed", "TDY Football") );
//        		statusList.add( new CadetStatus("Sandy", "Willis", "2014/09/10 @ 1245 Wed", "2014/09/10 @ 1245 Wed", "2014/09/10 @ 1245 Wed", "Soccor game") );
//        		statusList.add( new CadetStatus("Mary", "Santhouse", "2014/09/10 @ 1245 Wed", "2014/09/10 @ 1245 Wed", "2014/09/10 @ 1245 Wed", "To sponsers house") );
//
//        		new SignoutWindow(statusList, null, new User("Fred", "Smith", "1234"));
//            }
//        });
//	}

	public static boolean isAlive;
	
	private JFrame frame;
	private JLabel statusLabel;
	private JTable table;
	private DefaultTableModel tableModel;
	
	private JMenuItem saveLog;
	private JMenuItem exit;
	private JMenuItem newStatus;
	private JMenuItem deleteStatus;
	
	private TCPClientConnection connection;
	
	private User user;
	
	private Thread listenerThread;
	

	@Override
	public void run() {
		//Nothing required here since SignoutWindow is already created
	}


	public SignoutWindow(LinkedList<CadetStatus> statusList, TCPClientConnection serverConnection, User user ) {
		// Create the application window
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(840, 480));
		frame.setLocation(100,100);
		frame.setTitle("Cadet Sign Out: " + user.firstName+" "+user.lastName);
		frame.setLayout(new BorderLayout());

		// Create a JPanel for the entire window and set a border around it.
		JPanel windowPanel = new JPanel(new BorderLayout(10,10));
		EmptyBorder panelBorder = new EmptyBorder(10,10,10,10);
		windowPanel.setBorder(panelBorder);
		windowPanel.setOpaque(true);
        frame.setContentPane(windowPanel);
		
        // Create a status line at the top of the window to display connectivity info
		statusLabel = new JLabel();
		String status = "User: ";
		if (user != null) {
			status += user.firstName + " " + user.lastName;
		}
		if (serverConnection != null) {
			status += "       Connected to "
                    + serverConnection.serverName 
                    + " on port " + serverConnection.serverPort;
		}
		statusLabel.setText(status);
		windowPanel.add(statusLabel, BorderLayout.NORTH);

		// Initialize data for the display table
		String[] columnNames = { "Post Time", "Name", "Depart Time",
				                 "Return Time", "Activity" };
		Object[][] data = null;

		// Make a tableModel that does not allow any cells to be edited
		tableModel = new DefaultTableModel(data, columnNames) {

		  @Override
		  public boolean isCellEditable(int row, int column) {  
		     return true; //all cells false
		  }
		};

		// Insert the data into the table.
		for (int j=0; j<statusList.size(); j++) {
			CadetStatus cadet = statusList.get(j);
			addToTable(cadet);
		}
		
		//removeFromTable(statusList.get(0));
		
		// Create the display table
		table = new JTable(tableModel);
		table.setAutoCreateRowSorter(true);
		table.setFillsViewportHeight(true);
		
		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		// Add the scroll pane to the windowPanel.
		windowPanel.add(scrollPane, BorderLayout.CENTER);

		// Create the menus
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		menuBar.add( fileMenu );
		menuBar.add( editMenu );
		saveLog = new JMenuItem("Save log... (not required)");
		exit = new JMenuItem("Exit");
		fileMenu.add(saveLog);
		fileMenu.add(exit);
		newStatus = new JMenuItem("New (or modify) an Activity");
		deleteStatus = new JMenuItem("Delete an Activity");
		editMenu.add(newStatus);
		editMenu.add(new JSeparator());
		editMenu.add(deleteStatus);
		
		saveLog.addActionListener(this);
		exit.addActionListener(this);
		newStatus.addActionListener(this);
		deleteStatus.addActionListener(this);
		
		frame.setVisible(true);
		
		isAlive = true;
		connection = serverConnection;
		this.user = user;
		
		if(connectToServer()){

//			updateThread = new Thread((Runnable) new UpdateThread(serverConnection));
//			updateThread.start();
			
			//Create the Keep Alive Thread Timer
			TimerTask updateTask = new UpdateThread(connection);
			Timer updateTimer = new Timer();
			updateTimer.schedule(updateTask, 5000, 5000);
			

			
			
			System.out.println("Timertask enabled");
			
			
		}
		//Otherwise this thread will exit itself and go back to the login.
	}

	//-----------------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == saveLog) {
			
			// Save the records to a file (not required for PEX 2)
			//System.out.println("Save the logs");
			
		} else if (event.getSource() == exit) {
			
			// Quit the client
			//System.out.println("Quiting the client");
			frame.dispose();
			
			// TODO: cleanly close the TCP connection
			
		} else if (event.getSource() == newStatus) {
			
			// Allow the user to enter a new (or modified) status
			//System.out.println("Create a new status");
			CadetStatus cadetStatus = new CadetStatus(user.firstName, user.lastName);
			int index = findInTable(cadetStatus);
			if (index >= 0) {
				cadetStatus = getFromTable(index); 
			} 
			// TODO: start an ActivityDialogBox to edit the cadetStatus
			
		} else if (event.getSource() == deleteStatus) {
			
			// Delete the user's current status record
			System.out.println("Delete the current status");
			// TODO: send a message to the server to delete the user's current
			// SIGOUT log.

			
		}
	}
	
	//-----------------------------------------------------------------
	// Add a new CadetStatus record to the display table.
	public void addToTable(CadetStatus cadet) {
		Vector<String> newRow = new Vector<String>(5);
		
		newRow.add(cadet.timeStamp);
		newRow.add(cadet.lastName + ", " + cadet.firstName);
		newRow.add(cadet.departTime);
		newRow.add(cadet.returnTime);
		newRow.add(cadet.activity);
		
		tableModel.addRow(newRow);
	}

	//-----------------------------------------------------------------
	// Get a CadetStatus record out of the display table.
	public CadetStatus getFromTable(int rowIndex) {
		
		String postTime, firstName, lastName, departTime, returnTime, activity;
		String fullName;
		
		// Get the strings in the table
		postTime = (String) tableModel.getValueAt(rowIndex, 0);
		fullName = (String) tableModel.getValueAt(rowIndex, 1);
		departTime = (String) tableModel.getValueAt(rowIndex, 2);
		returnTime = (String) tableModel.getValueAt(rowIndex, 3);
		activity = (String) tableModel.getValueAt(rowIndex, 4);
		
		// Separate out the first and last name
		String [] names = fullName.split(",");
		if (names.length == 2) {
		    lastName = names[0].trim();
		    firstName = names[1].trim();
		} else {
			firstName = "Unknown";
			lastName = "Unknown";
		}

		// Create a CadetStatus object.
		CadetStatus status = new CadetStatus(firstName, lastName, postTime,
                departTime, returnTime, activity);
		
		return status;
	}

	//-----------------------------------------------------------------
	// Find the row in the display table that contains a specific status record.
	public int findInTable(CadetStatus cadet) {
		
		for (int row=0; row<tableModel.getRowCount(); row++) {
			String name = (String) tableModel.getValueAt(row, 1);
			String [] names = name.split(",");
			if (names.length == 2) {
			    String lastName = names[0].trim();
			    String firstName = names[1].trim();
			    
			    if (lastName.compareToIgnoreCase(cadet.lastName) == 0 &&
			    	firstName.compareToIgnoreCase(cadet.firstName) == 0) {
			    		return row;
			    }
			}
		}
		return -1; // a matching row was not found
	}
	
	//-----------------------------------------------------------------
	// Remove the row from the display table that matches the specified cadet.
	public void removeFromTable(CadetStatus cadet) {
		// Find the matching row in the display table.
		int rowIndex = findInTable(cadet);
		
		// If a row was found, delete the row.
		if (rowIndex >= 0) {
			tableModel.removeRow(rowIndex);
		}
		
		// Update the display of the table.
		redrawTable();
	}
	
	//-----------------------------------------------------------------
	// Repaint the display table so changes in the table will be displayed.
	public void redrawTable() {
		table.repaint();
	}
	
	private boolean connectToServer() {
		String loginString = "LOGIN:"+user.firstName+":"+user.lastName+":"+user.password+"|";
		System.out.println(loginString);
		connection.sendMessage(loginString);
		
		String response = connection.receiveMessage();
		System.out.println(response);
		
		if(response.startsWith("VALID_LOGIN|")){
			System.out.println("Valid Login Received");
			return true;
		}
		else{
			System.out.println("Login Failed");
			this.frame.dispose();
			new LoginDialogBox();
			return false;
		}
		
	}


}



