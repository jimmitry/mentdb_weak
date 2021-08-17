/**
 * Project: MentDB
 * License: GPL v_3
 * Description: Mentalese Database Engine
 * Website: https://www.mentdb.org
 * Twitter: https://twitter.com/mentalese_db
 * Facebook: https://www.facebook.com/mentdb
 * Author: Jimmitry Payet
 * Mail: contact@mentdb.org
 * Locality: Reunion Island (French)
 */

package re.jpayet.mentdb.core.fx;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//The date class
public class DateFx {
	
	public static String add(String date, String field, String number) throws Exception {
		
		//Generate an error if the date is not valid
		if (is_valid_date(date).equals("0")) {
			
			throw new Exception("Sorry, the date '"+date+"' is not valid.");
		
		}
		
		//Generate an error if the field or the number are null or empty
		if (field==null || field.equals("") || number==null || number.equals("")) {
			
			throw new Exception("Sorry, the date, field and number cannot be null or empty.");
		
		}
		
		//The parameter number must be a number
		try {
			
			@SuppressWarnings("unused")
			int i = Integer.parseInt(number);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the parameter number must be a number.");
			
		}
		
		//The field must be DAY|MONTH|YEAR
		if (!(field.equals("DAY") || field.equals("MONTH") || field.equals("YEAR"))) {
			
			throw new Exception("Sorry, the field must be DAY or MONTH or YEAR.");
		
		}
		
		//Load the calendar object
		Calendar cal = Calendar.getInstance();    
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(8, 10)));
		cal.set(Calendar.MONTH, Integer.parseInt(date.substring(5, 7))-1);
		cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		
		//Add the number to the date
		if (field.equals("DAY")) cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(number));
		else if (field.equals("MONTH")) cal.add(Calendar.MONTH, Integer.parseInt(number));
		else if (field.equals("YEAR")) cal.add(Calendar.YEAR, Integer.parseInt(number));
		
		//Reformat the calendar
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}
	
	public static String addt(String timestamp, String field, String number) throws Exception {
		
		//Generate an error if the time stamp is not valid
		if (is_valid_timestamp(timestamp).equals("0")) {
			
			throw new Exception("Sorry, the timestamp '"+timestamp+"' is not valid.");
		
		}
		
		//Generate an error if the field or the number are null or empty
		if (field==null || field.equals("") || number==null || number.equals("")) {
			
			throw new Exception("Sorry, the date, field and number cannot be null or empty.");
		
		}
		
		//The parameter number must be a number
		try {
			
			@SuppressWarnings("unused")
			int i = Integer.parseInt(number);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the number must be a number.");
			
		}
		
		//The field must be SEC|MIN|HOUR|DAY|MONTH|YEAR
		if (!(field.equals("DAY") || field.equals("MONTH") || field.equals("YEAR") || field.equals("SEC") || field.equals("MIN") || field.equals("HOUR"))) {
			
			throw new Exception("Sorry, the field must be DAY or MONTH or YEAR.");
		
		}
		
		//Load the calendar object
		Calendar cal = Calendar.getInstance();    
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(timestamp.substring(8, 10)));
		cal.set(Calendar.MONTH, Integer.parseInt(timestamp.substring(5, 7))-1);
		cal.set(Calendar.YEAR, Integer.parseInt(timestamp.substring(0, 4)));
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timestamp.substring(11, 13)));
		cal.set(Calendar.MINUTE, Integer.parseInt(timestamp.substring(14, 16)));
		cal.set(Calendar.SECOND, Integer.parseInt(timestamp.substring(17, 19)));

		//Add the number to the date
		if (field.equals("HOUR")) cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(number));
		else if (field.equals("MIN")) cal.add(Calendar.MINUTE, Integer.parseInt(number));
		else if (field.equals("SEC")) cal.add(Calendar.SECOND, Integer.parseInt(number));
		else if (field.equals("DAY")) cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(number));
		else if (field.equals("MONTH")) cal.add(Calendar.MONTH, Integer.parseInt(number));
		else if (field.equals("YEAR")) cal.add(Calendar.YEAR, Integer.parseInt(number));

		//Reformat the calendar
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(cal.getTime());
		
	}
	
	public static String curdate() {
		return sysdate();
	}
	
	public static String current_date() {
		return sysdate();
	}
	
	public static String current_time() {
		return systime();
	}
	
	public static String current_timestamp() {
		return systimestamp();
	}
	
	public static String curtime() {
		return systime();
	}
	
	public static String curtimestamp() {
		return systimestamp();
	}
	
	public static String dateadd(String date, String field, String number) throws Exception {
		return add(date, field, number);
	}
	
	public static String dateaddt(String timestamp, String field, String number) throws Exception {
		return addt(timestamp, field, number);
	}
	
	public static String datediff(String date, String field, String number) throws Exception {
		return diff(date, field, number);
	}
	
	public static String datedifft(String timestamp, String field, String number) throws Exception {
		return difft(timestamp, field, number);
	}
	
	public static String ts_to_long(String timestamp) throws Exception {
		
		//Initialization
		Date dt = null;
		
		//Generate an error if the timestamp is not valid
		if (is_valid_timestamp(timestamp).equals("0")) {
			
			throw new Exception("Sorry, the timestamp '"+timestamp+"' is not valid (date.ts_to_long).");
		
		}
	
		try {
			
			//Get the date object
			dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp);
			
		} catch (Exception e) {};
		
		//Return the long value
		return ""+dt.getTime();
	}
	
	public static String ts_milli_sec_to_long(String timestamp) throws Exception {
		
		//Initialization
		Date dt = null;
		
		try {
			
			//Get the date object
			dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp);
			
		} catch (Exception e) {};
		
		//Return the long value
		return ""+dt.getTime();
	}
	
	public static String dt_to_long(String date) throws Exception {
		
		//Initialization
		Date dt = null;
		
		//Generate an error if the date is not valid
		if (is_valid_date(date).equals("0")) {
			
			throw new Exception("Sorry, the date '"+date+"' is not valid (date.dt_to_long).");
		
		}
		
		try {
			
			//Get the date object
			dt = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			
		} catch (Exception e) {};
		
		//Return the long value
		return ""+dt.getTime();
		
	}
	
	public static String day_of_month(String date) throws Exception {
		
		//Initialization
		Date dt = null;
		
		//Generate an error if the date is not valid
		if (is_valid_date(date).equals("0")) {
			
			throw new Exception("Sorry, the date '"+date+"' is not valid (date.day_of_month).");
		
		}
		
		try {
			
			//Get the date object
			dt = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			
		} catch (Exception e) {};

		//Get the day of month 
		return new SimpleDateFormat("dd").format(dt);
		
	}
	
	public static String day_of_week(String date) throws Exception {
		
		//Initialization
		Date dt = null;
		
		//Generate an error if the date is not valid
		if (is_valid_date(date).equals("0")) {
			
			throw new Exception("Sorry, the date '"+date+"' is not valid (date.day_of_week).");
		
		}
		
		try {
			
			//Get the date object
			dt = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			
		} catch (Exception e) {};

		//Get the day of week 
		return new SimpleDateFormat("u").format(dt);
		
	}
	
	public static String day_of_year(String date) throws Exception {
		
		//Initialization
		Date dt = null;
		
		//Generate an error if the date is not valid
		if (is_valid_date(date).equals("0")) {
			
			throw new Exception("Sorry, the date '"+date+"' is not valid (date.day_of_year).");
		
		}
		
		try {
			
			//Get the date object
			dt = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			
		} catch (Exception e) {};

		//Get the day of year
		return new SimpleDateFormat("D").format(dt);
		
	}
	
	public static String dayname(String date) throws Exception {
		
		//Initialization
		Date dt = null;
		
		//Generate an error if the date is not valid
		if (is_valid_date(date).equals("0")) {
			
			throw new Exception("Sorry, the date '"+date+"' is not valid (date.dayname).");
		
		}
		
		try {
			
			//Get the date object
			dt = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			
		} catch (Exception e) {};

		//Get the day name
		return new SimpleDateFormat("EEEE").format(dt);
		
	}
	
	public static String diff(String date, String field, String number) throws Exception {
		
		//Generate an error if the date is not valid
		if (is_valid_date(date).equals("0")) {
			
			throw new Exception("Sorry, the date '"+date+"' is not valid (date.diff).");
		
		}
		
		//Generate an error if the field or the number are null or empty
		if (field==null || field.equals("") || number==null || number.equals("")) {
			
			throw new Exception("Sorry, the date, field and number cannot be null or empty (date.diff).");
		
		}
		
		//The parameter number must be a number
		try {
			
			@SuppressWarnings("unused")
			int i = Integer.parseInt(number);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the number must be a number (date.dateadd).");
			
		}
		
		//The field must be DAY|MONTH|YEAR
		if (!(field.equals("DAY") || field.equals("MONTH") || field.equals("YEAR"))) throw new Exception("Sorry, the field must be DAY or MONTH or YEAR (date.diff).");
		
		//Load the calendat object
		Calendar cal = Calendar.getInstance();    
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(8, 10)));
		cal.set(Calendar.MONTH, Integer.parseInt(date.substring(5, 7))-1);
		cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		
		//Subtract the number to the date
		if (field.equals("DAY")) cal.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(number));
		else if (field.equals("MONTH")) cal.add(Calendar.MONTH, -Integer.parseInt(number));
		else if (field.equals("YEAR")) cal.add(Calendar.YEAR, -Integer.parseInt(number));
		
		//Reformat the calendar
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
		
	}
	
	public static String difft(String timestamp, String field, String number) throws Exception {
		
		//Generate an error if the time stamp is not valid
		if (is_valid_timestamp(timestamp).equals("0")) {
			
			throw new Exception("Sorry, the timestamp '"+timestamp+"' is not valid (date.difft).");
		
		}
		
		//Generate an error if the field or the number are null or empty
		if (field==null || field.equals("") || number==null || number.equals("")) {
			
			throw new Exception("Sorry, the date, field and number cannot be null or empty (date.difft).");
		
		}
		
		//The parameter number must be a number
		try {
			
			@SuppressWarnings("unused")
			int i = Integer.parseInt(number);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the number must be a number (date.difft).");
			
		}
		
		//The field must be SEC|MIN|HOUR|DAY|MONTH|YEAR
		if (!(field.equals("DAY") || field.equals("MONTH") || field.equals("YEAR") || field.equals("SEC") || field.equals("MIN") || field.equals("HOUR"))) {
			
			throw new Exception("Sorry, the field must be DAY or MONTH or YEAR.");
		
		}
		
		//Load the calendar object
		Calendar cal = Calendar.getInstance();    
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(timestamp.substring(8, 10)));
		cal.set(Calendar.MONTH, Integer.parseInt(timestamp.substring(5, 7))-1);
		cal.set(Calendar.YEAR, Integer.parseInt(timestamp.substring(0, 4)));
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timestamp.substring(11, 13)));
		cal.set(Calendar.MINUTE, Integer.parseInt(timestamp.substring(14, 16)));
		cal.set(Calendar.SECOND, Integer.parseInt(timestamp.substring(17, 19)));
		
		//Subtract the number to the date
		if (field.equals("HOUR")) cal.add(Calendar.HOUR_OF_DAY, -Integer.parseInt(number));
		else if (field.equals("MIN")) cal.add(Calendar.MINUTE, -Integer.parseInt(number));
		else if (field.equals("SEC")) cal.add(Calendar.SECOND, -Integer.parseInt(number));
		else if (field.equals("DAY")) cal.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(number));
		else if (field.equals("MONTH")) cal.add(Calendar.MONTH, -Integer.parseInt(number));
		else if (field.equals("YEAR")) cal.add(Calendar.YEAR, -Integer.parseInt(number));
		
		//Reformat the calendar
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(cal.getTime());
		
	}
	
	public static String full_systimestamp() {
		
		//Prepare the format
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SZ");
		
		//Get the timestamp
		return dateFormat.format(System.currentTimeMillis());
		
	}
	
	public static String hour(String time) throws Exception {
		
		//Initialization
		Date dt = null;
		
		//Generate an error if the time is not valid
		if (is_valid_time(time).equals("0")) {
			
			throw new Exception("Sorry, the time '"+time+"' is not valid.");
		
		}
		
		try {
			
			//Get the date object
			dt = new SimpleDateFormat("HH:mm:ss").parse(time);
			
		} catch (Exception e) {};

		//Get the hour
		return new SimpleDateFormat("HH").format(dt);
		
	}
	
	public static String is_valid_date(String dateToValidate) {
		 
		//If null return false
		if(dateToValidate == null) {
			
			return "0";
		
		}
 
		//Initialization
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
 
		try {
 
			//if not valid, it will throw ParseException
			@SuppressWarnings("unused")
			Date date = sdf.parse(dateToValidate);
 
		} catch (ParseException e) {
 
			return "0";
		}
 
		return "1";
	}
	
	public static String is_valid_date(String dateToValidate, String format){
		 
		//If null return false
		if(dateToValidate == null){
			return "0";
		}
 
		//Initialization
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
 
		try {
 
			//if not valid, it will throw ParseException
			@SuppressWarnings("unused")
			Date date = sdf.parse(dateToValidate);
 
		} catch (ParseException e) {
 
			return "0";
		}
 
		return "1";
	}
	
	public static String is_valid_time(String timeToValidate){
		 
		//If null return false
		if(timeToValidate == null) {
			
			return "0";
		
		}
 
		//Initialization
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		sdf.setLenient(false);
 
		try {
 
			//if not valid, it will throw ParseException
			@SuppressWarnings("unused")
			Date date = sdf.parse(timeToValidate);
 
		} catch (ParseException e) {
 
			return "0";
		}
 
		return "1";
	}
	
	public static String is_valid_timestamp(String timestampToValidate){
		 
		//If null return false
		if(timestampToValidate == null){
			return "0";
		}
 
		//Initialization
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setLenient(false);
 
		try {
 
			//if not valid, it will throw ParseException
			@SuppressWarnings("unused")
			Date date = sdf.parse(timestampToValidate);
 
		} catch (ParseException e) {
 
			return "0";
		}
 
		return "1";
	}
	
	public static String is_valid_timestamp(String timestampToValidate, String format){
		 
		//If null return false
		if(timestampToValidate == null){
			return "0";
		}
 
		//Initialization
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
 
		try {
 
			//if not valid, it will throw ParseException
			@SuppressWarnings("unused")
			Date date = sdf.parse(timestampToValidate);
 
		} catch (ParseException e) {
 
			return "0";
		}
 
		return "1";
	}
	
	public static String format(String timestampToFormat, String formatIn, String formatOut) throws Exception{
		 
		//Generate an error if the timestamp to format is null or empty
		if(timestampToFormat == null || timestampToFormat.equals("")){
			
			throw new Exception("Sorry, the timestamp cannot be null or empty.");

		}
 
		try {

			//Initialization
			SimpleDateFormat sdfIn = new SimpleDateFormat(formatIn);
			sdfIn.setLenient(false);
			SimpleDateFormat sdfOut = new SimpleDateFormat(formatOut);
			sdfOut.setLenient(false);
 
			//if not valid, it will throw ParseException
			Date date = sdfIn.parse(timestampToFormat);
			
			return sdfOut.format(date);
 
		} catch (ParseException e) {
 
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
	}
	
	public static String minute(String time) throws Exception {
		
		//Initialization
		Date dt = null;
		
		//Generate an error if the time is not valid
		if (is_valid_time(time).equals("0")) {
			
			throw new Exception("Sorry, the time '"+time+"' is not valid.");
		
		}
		
		try {
			
			//Get the date object
			dt = new SimpleDateFormat("HH:mm:ss").parse(time);
			
		} catch (Exception e) {};

		//Get the hour
		return new SimpleDateFormat("mm").format(dt);
		
	}
	
	public static String month(String date) throws Exception {
		
		//Initialization
		Date dt = null;
		
		//Generate an error if the date is not valid
		if (is_valid_date(date).equals("0")) {
			
			throw new Exception("Sorry, the date '"+date+"' is not valid.");
		
		}
		
		try {
			
			//Get the date object
			dt = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			
		} catch (Exception e) {};

		//Get the month
		return new SimpleDateFormat("M").format(dt);
		
	}
	
	public static String monthname(String date) throws Exception {
		
		//Initialization
		Date dt = null;
		
		//Generate an error if the date is not valid
		if (is_valid_date(date).equals("0")) {
			
			throw new Exception("Sorry, the date '"+date+"' is not valid.");
		
		}
		
		try {
			
			//Get the date object
			dt = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			
		} catch (Exception e) {};

		//Get the day of month
		return new SimpleDateFormat("MMMM").format(dt);
		
	}
	
	public static String seconde(String time) throws Exception {
		
		//Initialization
		Date dt = null;
		
		//Generate an error if the time is not valid
		if (is_valid_time(time).equals("0")) {
			
			throw new Exception("Sorry, the time '"+time+"' is not valid.");
		
		}
		
		try {
			
			//get the date object
			dt = new SimpleDateFormat("HH:mm:ss").parse(time);
			
		} catch (Exception e) {};

		//Get the hour
		return new SimpleDateFormat("ss").format(dt);
		
	}
	
	public static String sysdate() {
		
		//Prepare format
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		//Get the date
		return dateFormat.format(System.currentTimeMillis());
		
	}
	
	public static String systime() {
		
		//Prepare format
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		
		//Get the time
		return dateFormat.format(System.currentTimeMillis());
		
	}
	
	public static String systimestamp() {
		
		//Prepare format
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		//Get the timestamp
		return dateFormat.format(System.currentTimeMillis());
		
	}
	
	public static String[] systimestamp(String separator) {
		
		//Prepare format
		DateFormat dateFormat = new SimpleDateFormat("yyyy"+separator+"MM"+separator+"dd"+separator+"HH"+separator+"mm"+separator+"ss");
		
		//Get the timestamp
		return dateFormat.format(System.currentTimeMillis()).split(separator, -1);
		
	}
	
	public static String systimestamp_milli_sec() {
		
		//Prepare format
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		
		//Get the timestamp
		return dateFormat.format(System.currentTimeMillis());
		
	}
	
	public static String systimestamp_min() {
		
		//Prepare format
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		
		//Get the timestamp
		return dateFormat.format(System.currentTimeMillis());
		
	}
	
	public static String time(String timestamp) throws Exception {
		
		//Initialization
		Date dt = null;
		
		//Generate an error if the time stamp is not valid
		if (is_valid_timestamp(timestamp).equals("0")) {
			
			throw new Exception("Sorry, the timestamp '"+timestamp+"' is not valid.");
		
		}
		
		try {
			
			//Get the date object
			dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp);
			
		} catch (Exception e) {};

		//Get the time.
		return new SimpleDateFormat("HH:mm:ss").format(dt);
		
	}
	
	public static String year(String date) throws Exception {
		
		//Initialization
		Date dt = null;
		
		//Generate an error if the date is not valid
		if (is_valid_date(date).equals("0")) {
			
			throw new Exception("Sorry, the date '"+date+"' is not valid.");
		
		}
		
		try {
			
			//Get the date object
			dt = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			
		} catch (Exception e) {};

		//Get the year.
		return new SimpleDateFormat("yyyy").format(dt);
		
	}

}
