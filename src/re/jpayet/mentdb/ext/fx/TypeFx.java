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

package re.jpayet.mentdb.ext.fx;

import java.util.GregorianCalendar;

import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.session.SessionThread;

//The type class
public class TypeFx {
	
	public static String is_enum(String value, String values) throws Exception {
		
		if (Integer.parseInt(AtomFx.find(values, value, ","))==0) return "0";
		else return "1";
		
	}
	
	public static String is_bool(SessionThread session, String value, String bool1, String bool2, EnvManager env) throws Exception {
		
		//Test
		return OperatorFx.or(session, OperatorFx.equal(value, bool1), OperatorFx.equal(value, bool2), env, null, null);
		
	}
	
	public static String is_char(String value, String size) {
		
		//Try to test
		try {
			
			//If one in all parameters are null then null will be returned
			if (value==null || size==null) {
				
				return null;
				
			} else {
				
				//Check if char
				if (value.length()!=Integer.parseInt(size)) {
					return "0";
				}
				else return "1";
				
			}
			
		} catch (Exception e) {
			
			//If an error was generated then return null in a basic data object
			return null;
			
		}
		
	}
	
	public static String is_date(String date) {
		
		//Try to test
		try {
			
			//If one in all parameters are null then null will be returned
			if (date==null) {
				
				return null;
				
			} else {
				
				//Define the boolean for Check if date
				boolean b = true;
				
				//Try to test the string
				try
				{
					GregorianCalendar d = new GregorianCalendar();
					d.set(GregorianCalendar.YEAR, Integer.parseInt(AtomFx.get(date, "1", "-")));
					d.set(GregorianCalendar.MONTH, Integer.parseInt(AtomFx.get(date, "2", "-"))-1);
					d.set(GregorianCalendar.DAY_OF_MONTH, Integer.parseInt(AtomFx.get(date, "3", "-")));
					if (!(d.get(GregorianCalendar.DAY_OF_MONTH)==Integer.parseInt(AtomFx.get(date, "3", "-")) && d.get(GregorianCalendar.MONTH)==(Integer.parseInt(AtomFx.get(date, "2", "-"))-1) && d.get(GregorianCalendar.YEAR)==Integer.parseInt(AtomFx.get(date, "1", "-")))) b = false;
				}
				catch (Exception e)
				{
					
					//An error appears
					b = false;
					
				}
				
				//Return the result of the test
				if (b) return "1";
				return "0";
				
			}
			
		} catch (Exception e) {
			
			//If an error was generated then return null in a basic data object
			return "0";
			
		}
		
	}

	public static String is_decimal(String stringDecimal, String digitBeforeTheDecimalPoint, String digitAfterTheDecimalPoint) {
		
		//Try to test
		try {
			
			//If one in all parameters are null then null will be returned
			if (stringDecimal==null || digitBeforeTheDecimalPoint==null || digitAfterTheDecimalPoint==null) {
				
				return null;
				
			} else {
				
				//Define the boolean for test the decimal number
				boolean b = true;
				
				//Prepare the string decimal number for test
				String left = "", right = "", str=stringDecimal.trim();
				if (str.charAt(0) == '-') str = str.substring(1);
				str = str.replace(",", ".");
				if (AtomFx.size(str, ".").equals("1")) str = str + ".";
				
				//Test the size of the decimal number
				if (!AtomFx.size(str, ".").equals("2")) b = false;
				else
				{
					
					//Get the left part
					left = AtomFx.get(str, "1", ".");
					
					//Get the right part
					right = AtomFx.get(str, "2", ".");
					
					//Test the decimal number
					if ((!(is_number(left).equals("1"))) || (!right.equals("") && !(is_number(right).equals("1"))))
					{
						b = false;
					}
					else
					{

						if ((left.length() <= Integer.parseInt(digitBeforeTheDecimalPoint)) && (right.length() <= Integer.parseInt(digitAfterTheDecimalPoint))) {
							b = true;
						}
						else {
							b = false;
						}
					}
					
				}
				
				//Return the result of the test
				if (b) return "1";
				else return "0";
				
			}
			
		} catch (Exception e) {
			
			//If an error was generated then return null in a basic data object
			return null;
			
		}
		
	}
	
	public static String is_email(String emailAddress) {

		//If one in all parameters are null then null will be returned
		if (emailAddress==null) {
			
			return null;
			
		} else {
			
			//Check if is an email address
			if (AtomFx.size(emailAddress, "@").equals("2")) {
				if (AtomFx.size(AtomFx.get(emailAddress, "2", "@"), ".").equals("2"))
					return "1";
				else return "0";
			} else return "0";
			
		}
		
	}
	
	public static String is_hour(String stringHour) {
		
		//Try to test
		try {
			
			//If stringHour is null then null will be returned
			if (stringHour==null) {
				
				return null;
				
			} else {
				
				//Define a boolean for the test
				boolean b = true;
				
				//Test the size of the string hour
				if (AtomFx.size(stringHour,":").equals("3"))
				{
					
					//Check if the string is in hour format
					String h = AtomFx.get(stringHour,"1",":"), m = AtomFx.get(stringHour,"2",":"), s = AtomFx.get(stringHour,"3",":");
					try
					{
						if (Integer.parseInt(h)<=23 && Integer.parseInt(m)<=59 && Integer.parseInt(s)<=59)
							b = true;
						else b = false;
					}
					catch (Exception e)
					{
						b = false;
					}
					
				}
				else
				{
					
					//The format of the string is not valid
					b = false;
				}
				
				//Return the result of the test
				if (b) return "1";
				return "0";
				
			}
			
		} catch (Exception e) {
			
			//If an error was generated then return null in a basic data object
			return null;
			
		}
		
	}
	
	public static String is_time(String stringHour) {
		
		//Try to test
		try {
			
			//If stringHour is null then null will be returned
			if (stringHour==null) {
				
				return null;
				
			} else {
				
				//Define a boolean for the test
				boolean b = true;
				
				//Test the size of the string hour
				if (AtomFx.size(stringHour,":").equals("3"))
				{
					
					//Check if the string is in hour format
					String h = AtomFx.get(stringHour,"1",":"), m = AtomFx.get(stringHour,"2",":"), s = AtomFx.get(stringHour,"3",":");
					try
					{
						if (Integer.parseInt(h)<=23 && Integer.parseInt(m)<=59 && Integer.parseInt(s)<=59)
							b = true;
						else b = false;
					}
					catch (Exception e)
					{
						b = false;
					}
					
				}
				else
				{
					
					//The format of the string is not valid
					b = false;
				}
				
				//Return the result of the test
				if (b) return "1";
				return "0";
				
			}
			
		} catch (Exception e) {
			
			//If an error was generated then return null in a basic data object
			return null;
			
		}
		
	}
	
	public static String is_hour_without_sec(String stringHour) {
		
		//Try to test
		try {
			
			//If stringHour is null then null will be returned
			if (stringHour==null) {
				
				return null;
				
			} else {
				
				//Define a boolean for the test
				boolean b = true;
				
				//Test the size of the string hour
				if (AtomFx.size(stringHour,":").equals("2"))
				{
					
					//Check if the string is in hour format
					String h = AtomFx.get(stringHour,"1",":"), m = AtomFx.get(stringHour,"2",":");
					try
					{
						if (Integer.parseInt(h)<=23 && Integer.parseInt(m)<=59)
							b = true;
						else b = false;
					}
					catch (Exception e)
					{
						b = false;
					}
				}
				else
				{
					
					//The format of the string is not valid
					b = false;
					
				}
				
				//Return the result of the test
				if (b) return "1";
				return "0";
				
			}
			
		} catch (Exception e) {
			
			//If an error was generated then return null in a basic data object
			return null;
			
		}
		
	}
	
	public static String is_integer(String value, String size)
	{
		
		//Test the value
		return is_decimal(value, size, "0");
		
	}
	
	public static String is_number(String value) {
		
		//Try to test
		try {
			
			//If value is null then null will be returned
			if (value==null) {
				
				return null;
				
			} else {
				
				@SuppressWarnings("unused")
				Double d = Double.parseDouble(value);
				
				return "1";
				
			}
			
		} catch (Exception e) {
			
			return "0";
			
		}
		
	}
	
	public static String is_byte(String value) {
		
		//Try to test
		try {
			
			//If value is null then null will be returned
			if (value==null) {
				
				return null;
				
			} else {
				
				Byte.parseByte(value);
				
				return "1";
				
			}
			
		} catch (Exception e) {
			
			return "0";
			
		}
		
	}
	
	public static String is_small_int(String value) {
		
		//Try to test
		try {
			
			//If value is null then null will be returned
			if (value==null) {
				
				return null;
				
			} else {
				
				Short.parseShort(value);
				
				return "1";
				
			}
			
		} catch (Exception e) {
			
			return "0";
			
		}
		
	}
	
	public static String is_medium_int(String value) {
		
		//Try to test
		try {
			
			//If value is null then null will be returned
			if (value==null) {
				
				return null;
				
			} else {
				
				if (Integer.parseInt(value)>8388607) return "0";
				else if (Integer.parseInt(value)<-8388608) return "0";
				else return "1";
				
			}
			
		} catch (Exception e) {
			
			return "0";
			
		}
		
	}
	
	public static String is_int(String value) {
		
		//Try to test
		try {
			
			//If value is null then null will be returned
			if (value==null) {
				
				return null;
				
			} else {
				
				Integer.parseInt(value);
				
				return "1";
				
			}
			
		} catch (Exception e) {
			
			return "0";
			
		}
		
	}
	
	public static String is_big_int(String value) {
		
		//Try to test
		try {
			
			//If value is null then null will be returned
			if (value==null) {
				
				return null;
				
			} else {
				
				Long.parseLong(value);
				
				return "1";
				
			}
			
		} catch (Exception e) {
			
			return "0";
			
		}
		
	}
	
	public static String is_float(String value) {
		
		//Try to test
		try {
			
			//If value is null then null will be returned
			if (value==null) {
				
				return null;
				
			} else {
				
				Float.parseFloat(value);
				
				return "1";
				
			}
			
		} catch (Exception e) {
			
			return "0";
			
		}
		
	}
	
	public static String is_double(String value) {
		
		//Try to test
		try {
			
			//If value is null then null will be returned
			if (value==null) {
				
				return null;
				
			} else {
				
				Double.parseDouble(value);
				
				return "1";
				
			}
			
		} catch (Exception e) {
			
			return "0";
			
		}
		
	}
	
	public static String is_timestamp(String value) {
		
		return DateFx.is_valid_timestamp(value);
		
	}
	
	public static String is_valid_date(String dateToValidate){
		 
		return DateFx.is_valid_date(dateToValidate);
	}
	
	public static String is_valid_date(String dateToValidate, String format){
		 
		return DateFx.is_valid_date(dateToValidate, format);
		
	}
	
	public static String is_valid_time(String timeToValidate){
		 
		return DateFx.is_valid_time(timeToValidate);
		
	}
	
	public static String is_valid_timestamp(String timestampToValidate){
		 
		return DateFx.is_valid_timestamp(timestampToValidate);

	}
	
	public static String is_valid_timestamp(String timestampToValidate, String format){
		 
		return DateFx.is_valid_timestamp(timestampToValidate, format);
		
	}
	
	public static String is_varchar(String value, String size) {
		
		//Try to test
		try {
			
			//If one in all parameters are null then null will be returned
			if (value==null || size==null) {
				
				return null;
				
			} else {
				
				//Test the size of the string
				if (value.length()>Integer.parseInt(size)) {
					return "0";
				}
				else return "1";
				
			}
			
		} catch (Exception e) {
			
			//If an error was generated then return null in a basic data object
			return null;
			
		}
		
	}

}
