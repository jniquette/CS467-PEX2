import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.border.EmptyBorder;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

// Implements a dialog box for editing the information needed for
// a CadetStatus object.

// Written by; Dr. Wayne Brown, Fall 2014
// Modified by:

public class ActivityDialogBox implements ActionListener {

	// Uncomment this main program if you want to run the GUI by itself
	
/*	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	// Create a cadetStatus object for testing
            	CadetStatus cadetStatus = new CadetStatus("Daniel", "Forero", 
            			"2014/11/16 @ 1213 Sun", "2014/11/17 @ 1530 Mon", "2014/11/18 @ 2330 Tue", "To sponsors house");
            	new ActivityDialogBox(cadetStatus, null);
            }
        });
	}
*/	
	// GUI components needed by the actionPerformed method
	private JFrame frame;
	private JTextField departTimeInput;
	private JTextField returnTimeInput;
	private JTextField activityInput;
	private JLabel errorLabel;
	
	private JButton submitButton;
	private JButton cancelButton;
	
	private UtilDateModel departModel;
	private UtilDateModel returnModel;
	
	// SIGNOUT objects needed to display the current information and 
	// possibly send new information to the server.
	private TCPClientConnection connection;
	private CadetStatus         cadetStatus;
	
	//-----------------------------------------------------------------
	/**
	 * Constructor for the ActivityDialogBox
	 * @param cadetStatus  The cadetStatus object you want the user to edit
	 * @param connection   An object that can send messages to the server.
	 */
	public ActivityDialogBox( CadetStatus cadetStatus, TCPClientConnection connection) {
		
		// Remember the cadet status and connection so that the inputs 
		// from the dialog box can be sent to the server. 
		this.cadetStatus = cadetStatus;
		this.connection = connection; 
		
		// Create the application window
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(510, 260));
		frame.setLocation(100,100);
		frame.setTitle("Sign Out Activity");
		frame.setResizable(false);

		// Create a JPanel for the entire window and set a border around it.
		JPanel windowPanel = new JPanel(new BorderLayout());
		EmptyBorder panelBorder = new EmptyBorder(10,10,10,10);
		windowPanel.setBorder(panelBorder);
		windowPanel.setOpaque(true);
        frame.setContentPane(windowPanel);
        
        // Create panels for the component layout
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel inputPanel = new JPanel(new BorderLayout());
        JPanel departPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel returnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel activityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
        // Layout the panels
        windowPanel.add(namePanel, BorderLayout.NORTH);
        windowPanel.add(inputPanel, BorderLayout.CENTER);
        windowPanel.add(submitPanel, BorderLayout.SOUTH);
        
        inputPanel.add(departPanel, BorderLayout.NORTH);
        inputPanel.add(returnPanel, BorderLayout.CENTER);
        inputPanel.add(activityPanel, BorderLayout.SOUTH);
        
        // Put the cadet's name in the namePanel
        JLabel nameLabel = new JLabel("Signout for " + 
             cadetStatus.firstName + " " + cadetStatus.lastName);
        namePanel.add(nameLabel);
        
        // Get the cadet's depart and return dates.
        Calendar depart = cadetStatus.getDepartTimeCalendar();
        int departYear = depart.get(Calendar.YEAR);
        int departMonth = depart.get(Calendar.MONTH);
        int departDay = depart.get(Calendar.DAY_OF_MONTH);
        int departHour = depart.get(Calendar.HOUR_OF_DAY);
        int departMinute = depart.get(Calendar.MINUTE);
        
        Calendar retTime = cadetStatus.getReturnTimeCalendar();
        int returnYear = retTime.get(Calendar.YEAR);
        int returnMonth = retTime.get(Calendar.MONTH);
        int returnDay = retTime.get(Calendar.DAY_OF_MONTH);
        int returnHour = retTime.get(Calendar.HOUR_OF_DAY);
        int returnMinute = retTime.get(Calendar.MINUTE);
        
        //-------------------------------------------------------------
        // DepartDate: Create a JLabel and JDatePicker
        JLabel departDateLabel = new JLabel("Depart date:");
        
        departModel = new UtilDateModel();
        departModel.setDate(departYear, departMonth, departDay);
        departModel.setSelected(true); // Show the initial date

        JDatePanelImpl datePanel = new JDatePanelImpl(departModel, new Properties());
        JDatePickerImpl departDatePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        departDatePicker.setShowYearButtons(true);
        
        departPanel.add(departDateLabel);
        departPanel.add(departDatePicker);
        
        // DepartTime: Create a JLabel and JTextField
        JLabel departTimeLabel = new JLabel("  Depart time:");
        departTimeInput = new JTextField(9);
        departTimeInput.setText(String.format("%04d", (departHour*100+departMinute)) );
        
        departPanel.add(departTimeLabel);
        departPanel.add(departTimeInput);
        
        //-------------------------------------------------------------
        // ReturnDate: Create a JLabel and JDatePicker
        JLabel returnDateLabel = new JLabel("Return date:");
    	returnModel = new UtilDateModel();
    	returnModel.setDate(returnYear, returnMonth, returnDay);
    	returnModel.setSelected(true); // Show the initial date

        JDatePanelImpl returnDatePanel = new JDatePanelImpl(returnModel, new Properties());
        JDatePickerImpl returnDatePicker = new JDatePickerImpl(returnDatePanel, new DateLabelFormatter());
        
        returnPanel.add(returnDateLabel);
        returnPanel.add(returnDatePicker);
        
        // ReturnTime: Create a JLabel and JTextField
        JLabel returnTimeLabel = new JLabel("  Return time:");
        returnTimeInput = new JTextField(9);
        returnTimeInput.setText(String.format("%04d", (returnHour*100+returnMinute)) );
        
        returnPanel.add(returnTimeLabel);
        returnPanel.add(returnTimeInput);
        
        //-------------------------------------------------------------
        // Activity: Create a JLabel and JTextField
        JLabel activityLabel = new JLabel("Activity:");
        activityInput = new JTextField(36);
        activityInput.setText(cadetStatus.activity);

        activityPanel.add(activityLabel);
        activityPanel.add(activityInput);
        
        //-------------------------------------------------------------
        // Submit and cancel buttons
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");

        submitPanel.add(errorLabel);
        submitPanel.add(submitButton);
        submitPanel.add(cancelButton);
        submitButton.addActionListener(this);
        cancelButton.addActionListener(this);
        
        frame.pack();
		frame.setVisible(true);
	}
	
	// -------------------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent event) {
		
		if (event.getSource() == cancelButton) {
			
			// Just close the dialog box
			frame.dispose();
			
		} else if (event.getSource() == submitButton) {
			
			// These values are assigned the user inputs from the GUI components.
			// Initialize the outputs so we can check for errors
			String newActivity   = "";
			boolean inputError = false;
			String errorText = "Invalid: ";

			// Get the depart time
			MyTimeFormatter departTime = new MyTimeFormatter(departTimeInput.getText());
			if (departTime.invalid()) {
				errorText += "Departure time; ";
				inputError = true;
			}
			
			// Get the return time
			MyTimeFormatter returnTime = new MyTimeFormatter(returnTimeInput.getText());
			if (returnTime.invalid()) {
				errorText += "Return time; ";
				inputError = true;
			}

			// Get the activity
			newActivity = activityInput.getText();
			if (newActivity.length() <= 0) {
				errorText += "Activity ";
				inputError = true;
			}
			
			if (! inputError) {
				// Close the current dialog box.
				frame.dispose();
				
				// Update the status of the cadet
				cadetStatus.setTimeStamp();
				cadetStatus.setDepartTime(departModel.getYear(), 
		                  departModel.getMonth(), 
		                  departModel.getDay(), 
		                  departTime.hour, 
		                  departTime.minutes);
				cadetStatus.setReturnTime(returnModel.getYear(), 
						  returnModel.getMonth(), 
						  returnModel.getDay(), 
		                  returnTime.hour, 
		                  returnTime.minutes);
				cadetStatus.activity = newActivity;
				
				// Debugging: print the values retrieved from the GUI
				//System.out.println("Updated cadet status\n" + cadetStatus);
				
				// TODO: Send the update to the server

			} else {
				errorLabel.setText(errorText);
			}
		}
	}
}

//---------------------------------------------------------------------
class MyTimeFormatter {
	public int hour;
	public int minutes;
	private boolean validTime;
		
	public MyTimeFormatter(String timeStr) {
		hour = 0;
		minutes = 0;

		try {
			int time = Integer.parseInt(timeStr);
			hour = time / 100;
			minutes = time % 100;
			validTime = true;
			    
		} catch (NumberFormatException e) {
			validTime = false;
		}
	    
	    if (hour < 0 || hour > 23 || minutes < 0 || minutes > 59) {
	    	validTime = false;
	    }
	}
	
	public boolean invalid() {
		return ! validTime;
	}
}

//---------------------------------------------------------------------
class DateLabelFormatter extends AbstractFormatter {
	static final long serialVersionUID = 1;
	
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
          
    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }
 
    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }
         
        return "";
    }
 
}





