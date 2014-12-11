package org.niquette.cs467pex2client;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//Implements an object to hold SIGNOUT information for a single cadet.

//Written by; Dr. Wayne Brown, Fall 2014
//Modified by:

public class CadetStatus {

	public String firstName;
	public String lastName;
	public String timeStamp;   // The time the status was posted to the server
	public String departTime;  // The departure date/time for the cadet
	public String returnTime;  // The return date/time for the cadet
	public String activity;    // The activity of the cadet while away
	
	// Create a date/time formatter. The text order is important. Using
	// the year/month/day format allows the strings to be sorted in 
	// data/time order.
	public static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd @ HHmm EEE");
	
	//-----------------------------------------------------------------
	// Main Constructor for a CadetStatus object. All values are specified
	// as strings. The time values are assumed to be in the format 
	// "yyyy/MM/dd @ HHmm EEE". For example "2014/11/17 @ 1530 Mon"
	public CadetStatus(String firstName, String lastName, String postTime,
	                   String departTime, String returnTime, String activity) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.timeStamp = postTime;
		this.departTime = departTime;
		this.returnTime = returnTime;
		this.activity = activity;	
	}

	//-----------------------------------------------------------------
	// Constructor that only specifies the user name.
	public CadetStatus(String firstName, String lastName) {
		
		this(firstName, lastName, 
			dateFormatter.format(new Date()), // Make the postTime be "now"
			dateFormatter.format(new Date()), // Make the departTime be "now"
			dateFormatter.format(new Date()), // Make the returnTime be "now"
			"--"); // No activity
	}
	
	//-----------------------------------------------------------------
	public Calendar getTimeStampCalendar() {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(toDate(timeStamp));
		return calendar;
	}
	
	//-----------------------------------------------------------------
	public Calendar getDepartTimeCalendar() {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(toDate(departTime));
		return calendar;
	}
	
	//-----------------------------------------------------------------
	public Calendar getReturnTimeCalendar() {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(toDate(returnTime));
		return calendar;
	}
	
	//-----------------------------------------------------------------
	public void setDepartTime(int year, int month, int day, int hour, int minutes) {
		departTime = dateToString(year, month, day, hour, minutes);
	}
	
	//-----------------------------------------------------------------
	public void setReturnTime(int year, int month, int day, int hour, int minutes) {
		returnTime = dateToString(year, month, day, hour, minutes);
	}
	
	//-----------------------------------------------------------------
	public void setTimeStamp() {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
		timeStamp = dateToString(calendar.get(Calendar.YEAR), 
				                 calendar.get(Calendar.MONTH), 
				                 calendar.get(Calendar.DAY_OF_MONTH), 
				                 calendar.get(Calendar.HOUR_OF_DAY), 
				                 calendar.get(Calendar.MINUTE));
	}
	
	//-----------------------------------------------------------------
	private String dateToString(int year, int month, int day, int hour, int minutes) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.YEAR, year);
	    calendar.set(Calendar.MONTH,month);
	    calendar.set(Calendar.DAY_OF_MONTH,day);
	    calendar.set(Calendar.HOUR_OF_DAY,hour);
	    calendar.set(Calendar.MINUTE,minutes);
	    Date date = calendar.getTime();
	    return dateFormatter.format(date);
	}
	
	//-----------------------------------------------------------------
	private Date toDate(String dateString) {
		Date date;
		try {
			date = dateFormatter.parse(dateString);
		} catch (ParseException e) {
			// If the dateString can't be parsed, set the date to "now"
			date = new Date();
		}
		return date;
	}
	
	//-----------------------------------------------------------------
	@Override
	public boolean equals(Object other) {
		if (other instanceof CadetStatus) {
			CadetStatus o = (CadetStatus) other;
			if (firstName.compareToIgnoreCase(o.firstName) == 0 &&
				lastName.compareToIgnoreCase(o.lastName) == 0) {
				return true;
			}
		}
		return false;
	}

	//-----------------------------------------------------------------
	@Override
	public String toString() {
		return "Cadet name : " + firstName + " " + lastName + "\n"
			 + "Time stamp : " + timeStamp + "\n"
			 + "Depart time: " + departTime + "\n"
			 + "Return time: " + returnTime + "\n"
			 + "activity   : " + activity + "\n";
	}
}
