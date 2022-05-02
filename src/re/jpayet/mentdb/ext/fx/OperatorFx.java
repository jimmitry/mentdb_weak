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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Vector;

import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.command.CommandManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.tools.Misc;

//The operator class
public class OperatorFx {
	
	//Add function
	public static String add(Vector<MQLValue> parameters, EnvManager env) throws Exception {

		try {
			
			double f = 0;
			
			//Add all values
			for(int i=0;i<parameters.size();i++) {
				
				f += Double.parseDouble(parameters.get(i).value);

			}
			
			if ((""+f).endsWith(".0")) return (""+f).substring(0, (""+f).length()-2);
			else return (""+f);
			
		} catch (Exception e) {
			
			return null;
			
		}
		
	}
	
	//Sub function
	public static String sub(String number1, String number2) throws Exception {

		try {
			
			double f = (Double.parseDouble(number1)-Double.parseDouble(number2));
			
			if ((""+f).endsWith(".0")) return (""+f).substring(0, (""+f).length()-2);
			else return (""+f);
			
		} catch (Exception e) {
			
			return null;
			
		}
		
	}
	
	//Mul function
	public static String mul(String number1, String number2) throws Exception {

		try {
			
			double f = (Double.parseDouble(number1)*Double.parseDouble(number2));
			if ((""+f).endsWith(".0")) return (""+f).substring(0, (""+f).length()-2);
			else return (""+f);
			
		} catch (Exception e) {
			
			return null;
			
		}
		
	}
	
	//Div function
	public static String div(String number1, String number2) throws Exception {

		try {

			double f = (Double.parseDouble(number1)/Double.parseDouble(number2));
			if ((""+f).endsWith(".0")) return (""+f).substring(0, (""+f).length()-2);
			else return (""+f);
			
		} catch (Exception e) {
			
			return null;
			
		}
		
	}
	
	//Add function
	public static String add_big_int(Vector<MQLValue> parameters, EnvManager env) throws Exception {

		try {
			
			BigInteger bi = BigInteger.valueOf(0);
			
			//Add all values
			for(int i=0;i<parameters.size();i++) {

				bi = bi.add(new BigInteger(parameters.get(i).value));

			}
			
			return bi.toString();
			
		} catch (Exception e) {
			
			return null;
			
		}
		
	}
	
	//Sub function
	public static String sub_big_int(String number1, String number2) throws Exception {

		try {

			BigInteger n1  = new BigInteger(number1);
			BigInteger n2  = new BigInteger(number2);

			return n1.subtract(n2).toString();
			
		} catch (Exception e) {
			
			return null;
			
		}
		
	}
	
	//Mul function
	public static String mul_big_int(String number1, String number2) throws Exception {

		try {

			BigInteger n1  = new BigInteger(number1);
			BigInteger n2  = new BigInteger(number2);

			return n1.multiply(n2).toString();
			
		} catch (Exception e) {
			
			return null;
			
		}
		
	}
	
	//Div function
	public static String div_big_int(String number1, String number2) throws Exception {

		try {

			BigInteger n1  = new BigInteger(number1);
			BigInteger n2  = new BigInteger(number2);

			return n1.divide(n2).toString();
			
		} catch (Exception e) {
			
			return null;
			
		}
		
	}
	
	//Add function
	public static String add_big_dec(Vector<MQLValue> parameters, EnvManager env) throws Exception {

		try {
			
			BigDecimal bi = BigDecimal.valueOf(0);
			
			//Add all values
			for(int i=0;i<parameters.size();i++) {

				bi = bi.add(new BigDecimal(parameters.get(i).value));

			}
			
			return bi.toString();
			
		} catch (Exception e) {
			
			return null;
			
		}
		
	}
	
	//Sub function
	public static String sub_big_dec(String number1, String number2) throws Exception {

		try {

			BigDecimal n1  = new BigDecimal(number1);
			BigDecimal n2  = new BigDecimal(number2);

			return n1.subtract(n2).toString();
			
		} catch (Exception e) {
			
			return null;
			
		}
		
	}
	
	//Mul function
	public static String mul_big_dec(String number1, String number2) throws Exception {

		try {

			BigDecimal n1  = new BigDecimal(number1);
			BigDecimal n2  = new BigDecimal(number2);

			return n1.multiply(n2).toString();
			
		} catch (Exception e) {
			
			return null;
			
		}
		
	}
	
	//Div function
	public static String div_big_dec(String number1, String number2) throws Exception {

		try {

			BigDecimal n1  = new BigDecimal(number1);
			BigDecimal n2  = new BigDecimal(number2);

			return n1.divide(n2).toString();
			
		} catch (Exception e) {
			
			return null;
			
		}
		
	}
	
	//And function
	public static String and(SessionThread session, String boolValue1, String boolValue2, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		//Execute the first command
		String r1 = CommandManager.executeAllCommands(session, Misc.splitCommand(boolValue1), env, parent_pid, current_pid);
		
		if (r1==null) return "0";
		else if (r1.equals("1")) {
			
			//Execute the second command
			String r2 = CommandManager.executeAllCommands(session, Misc.splitCommand(boolValue2), env, parent_pid, current_pid);
			
			if (r2==null) return "0";
			else if (r2.equals("1")) return "1";
			else return "0";
			
		} else return "0";
		
	}
	
	//Or function
	public static String or(SessionThread session, String boolValue1, String boolValue2, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		//Execute the first command
		String r1 = CommandManager.executeAllCommands(session, Misc.splitCommand(boolValue1), env, parent_pid, current_pid);
		
		if (r1==null) return "0";
		else if (r1.equals("1")) return "1";
		else {
			
			//Execute the second command
			String r2 = CommandManager.executeAllCommands(session, Misc.splitCommand(boolValue2), env, parent_pid, current_pid);
			
			if (r2==null) return "0";
			else if (r2.equals("1")) return "1";
			else return "0";
			
		}
		
	}
	
	//Xor function
	public static String xor(String boolValue1, String boolValue2) throws Exception {

		//Return the new boolean
		if (boolValue1==null || boolValue2==null) return "0";
		else if ((boolValue1.equals("1") && boolValue2.equals("0")) || (boolValue1.equals("0") && boolValue2.equals("1"))) return "1";
		else return "0";
		
	}
	
	//Not function
	public static String not(String bool) throws Exception {

		//Make the test
		if (bool==null) return null;
		else if (bool.equals("0")) return "1";
		else return "0";
		
	}
	
	//Is empty function
	public static String is_empty(String value) throws Exception {

		if (value==null) {
			return "0";
		} else if (value.equals("")) {
			return "1";
		} else {
			return "0";
		}
		
	}
	
	//Is not empty function
	public static String is_not_empty(String value) throws Exception {

		return not(is_empty(value));
		
	}
	
	//Equal function
	public static String equal(String value1, String value2) throws Exception {

		//Test
		if (value1==null && value2==null) {
			
			//All values are null (equal)
			return "1";
			
		} else if (value1==null) {
			
			//Here values are not equal
			return "0";
			
		} else if (value2==null) {
			
			//Here values are not equal
			return "0";
			
		} else {
			
			//Check if data of the two values are equal
			if (value1.equals(value2)) return "1";
			else return "0";
			
		}
		
	}
	
	//Not equal function
	public static String not_equal(String value1, String value2) throws Exception {

		return not(equal(value1, value2));
		
	}
	
	//Num equal function
	public static String num_equal(String number1, String number2) throws Exception {

		try {
			
			//Make the test
			if (Double.parseDouble(number1)==Double.parseDouble(number2)) return "1";
			else return "0";
			
		} catch (Exception e) {
			
			return "0";
			
		}
		
	}
	
	//Not num equal function
	public static String not_num_equal(String value1, String value2) throws Exception {

		return not(num_equal(value1, value2));
		
	}
	
	//Greater function
	public static String greater(String number1, String number2) throws Exception {

		try {
			
			//Check if greater
			if (Double.parseDouble(number1)>Double.parseDouble(number2)) return "1";
			else return "0";
			
		} catch (Exception e) {
			
			return "0";
			
		}
		
	}
	
	//Greater or equal function
	public static String greater_or_equal(String number1, String number2) throws Exception {

		try {
			
			//Check if greater
			if (Double.parseDouble(number1)>=Double.parseDouble(number2)) return "1";
			else return "0";
			
		} catch (Exception e) {
			
			return "0";
			
		}
		
	}
	
	//Less function
	public static String less(String number1, String number2) throws Exception {

		try {
			
			//Check if greater
			if (Double.parseDouble(number1)<Double.parseDouble(number2)) return "1";
			else return "0";
			
		} catch (Exception e) {
			
			return "0";
			
		}
		
	}
	
	//Less or equal function
	public static String less_or_equal(String number1, String number2) throws Exception {

		try {
			
			//Check if greater
			if (Double.parseDouble(number1)<=Double.parseDouble(number2)) return "1";
			else return "0";
			
		} catch (Exception e) {
			
			return "0";
			
		}
		
	}
	
	//Is not null
	public static String is_not_null(String value) {
		
		if (value==null) {
			return "0";
		} else {
			return "1";
		}
		
	}
	
	//Is null
	public static String is_null(String value) {
		
		if (value==null) {
			return "1";
		} else {
			return "0";
		}
		
	}
	
	//Is null or empty
	public static String is_null_or_empty(String value) {
		
		if (value==null || value.equals("")) {
			return "1";
		} else {
			return "0";
		}
		
	}

}
