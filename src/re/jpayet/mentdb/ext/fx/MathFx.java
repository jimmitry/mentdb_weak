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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.github.skjolber.packing.Box;
import com.github.skjolber.packing.BoxItem;
import com.github.skjolber.packing.Container;
import com.github.skjolber.packing.LargestAreaFitFirstPackager;
import com.github.skjolber.packing.Packager;

import re.jpayet.mentdb.ext.tools.Misc;

//The math class
public class MathFx {
	
	public static String convertFromBaseToBase(String str, String fromBase, String toBase) {
	    return Integer.toString(Integer.parseInt(str, Integer.parseInt(fromBase)), Integer.parseInt(toBase));
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray bin_package_3d(String nb_max_package, String timeout_to_stop, String jsonContainers, String jsonElements) throws ParseException {
		
		JSONArray result = new JSONArray();

		JSONArray jsonConts = (JSONArray) new JSONParser().parse(jsonContainers);
		JSONArray jsonElems = (JSONArray) new JSONParser().parse(jsonElements);
		
		// initialization
		List<Container> containers = new ArrayList<Container>();
		HashMap<String, String> keys = new HashMap<String, String>();
		for(int i=0;i<jsonConts.size();i++) {
			JSONObject cont = (JSONObject) jsonConts.get(i);
			String id = (String) cont.get("id");
			String x = (String) cont.get("x");
			String y = (String) cont.get("y");
			String z = (String) cont.get("z");
			String w = (String) cont.get("w");
			containers.add(new Container(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z), Integer.parseInt(w)));
			keys.put(x+"x"+y+"x"+z, id);
		}
		
		Packager packager = LargestAreaFitFirstPackager.newBuilder().withContainers(containers).build();
				
		List<BoxItem> products = new ArrayList<BoxItem>();
		for(int i=0;i<jsonElems.size();i++) {
			JSONObject elem = (JSONObject) jsonElems.get(i);
			String id = (String) elem.get("id");
			String x = (String) elem.get("x");
			String y = (String) elem.get("y");
			String z = (String) elem.get("z");
			String w = (String) elem.get("w");
			String q = (String) elem.get("q");
			products.add(new BoxItem(new Box(id, Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z), Integer.parseInt(w)), Integer.parseInt(q)));
		}
		
		// match multiple containers
		long deadline = System.currentTimeMillis() + Integer.parseInt(timeout_to_stop);
		List<Container> matchs = packager.packList(products, Integer.parseInt(nb_max_package), deadline);
		
		if (matchs!=null) {
			for(Container match : matchs) {
				result.add(keys.get(match.encode()));
			}
		}
		
		return result;

	}
	
	@SuppressWarnings("unchecked")
	public static String order_int_asc(String jsonArray) throws Exception {
		
		try {

			JSONArray jsonArr =  Misc.loadArray(jsonArray);
			List<Integer> jsonValues = new ArrayList<Integer>();
		    for (int i = 0; i < jsonArr.size(); i++) {
		        jsonValues.add(Integer.parseInt(jsonArr.get(i)+""));
		    }
			
		    Collections.sort( jsonValues );
		    
		    JSONArray result = new JSONArray();
		    for (int i = 0; i < jsonValues.size(); i++) {
		    		result.add(jsonValues.get(i));
		    }
		    
		    return result.toJSONString();

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}
	
	@SuppressWarnings("unchecked")
	public static String order_long_asc(String jsonArray) throws Exception {
		
		try {

			JSONArray jsonArr =  Misc.loadArray(jsonArray);
			List<Long> jsonValues = new ArrayList<Long>();
		    for (int i = 0; i < jsonArr.size(); i++) {
		        jsonValues.add(Long.parseLong(jsonArr.get(i)+""));
		    }
			
		    Collections.sort( jsonValues );
		    
		    JSONArray result = new JSONArray();
		    for (int i = 0; i < jsonValues.size(); i++) {
		    		result.add(jsonValues.get(i));
		    }
		    
		    return result.toJSONString();

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}
	
	@SuppressWarnings("unchecked")
	public static String order_double_asc(String jsonArray) throws Exception {
		
		try {

			JSONArray jsonArr =  Misc.loadArray(jsonArray);
			List<Double> jsonValues = new ArrayList<Double>();
		    for (int i = 0; i < jsonArr.size(); i++) {
		        jsonValues.add(Double.parseDouble(jsonArr.get(i)+""));
		    }
			
		    Collections.sort( jsonValues );
		    
		    JSONArray result = new JSONArray();
		    for (int i = 0; i < jsonValues.size(); i++) {
		    		result.add(jsonValues.get(i));
		    }
		    
		    return result.toJSONString();

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}
	
	@SuppressWarnings("unchecked")
	public static String order_float_asc(String jsonArray) throws Exception {
		
		try {

			JSONArray jsonArr =  Misc.loadArray(jsonArray);
			List<Float> jsonValues = new ArrayList<Float>();
		    for (int i = 0; i < jsonArr.size(); i++) {
		        jsonValues.add(Float.parseFloat(jsonArr.get(i)+""));
		    }
			
		    Collections.sort( jsonValues );
		    
		    JSONArray result = new JSONArray();
		    for (int i = 0; i < jsonValues.size(); i++) {
		    		result.add(jsonValues.get(i));
		    }
		    
		    return result.toJSONString();

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}
	
	public static String abs(String number) throws Exception {

		//Try to get the absolute value
		try {

			//Get the absolute value
			String result = ""+java.lang.Math.abs(Double.parseDouble(number));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}
	
	public static String acos(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.acos(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}
	
	public static String asin(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.asin(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String atan(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.atan(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String atan2(String number1, String number2) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.atan2(Double.parseDouble(number1), Double.parseDouble(number2)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}
	
	public static String avg(String json_array) throws Exception {

		//Try to get the absolute value
		try {

			double avg = 0;
			int counter = 0;
			
			JSONArray a = (JSONArray) new JSONParser().parse(json_array);
			
			for(int i=0;i<a.size();i++) {
				
				double v = Double.parseDouble(""+a.get(i));
				
				avg += v;
				counter++;
				
			}
			
			return ""+(avg/counter);

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String bit_and(String number1, String number2) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(Long.parseLong(number1) & Long.parseLong(number2));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String bit_or(String number1, String number2) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(Long.parseLong(number1) | Long.parseLong(number2));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String bit_xor(String number1, String number2) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(Long.parseLong(number1) ^ Long.parseLong(number2));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String cbrt(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.cbrt(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String ceil(String number) throws Exception {

		//Try to execute the function
		try {

			//Execute the CEIL function
			String result = ""+(java.lang.Math.ceil(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;
			
		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String ceiling(String number) throws Exception {

		//Get the smallest integer value not less than the number specified as an argument
		return ceil(number);

	}

	public static String cos(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.cos(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String cosh(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.cosh(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String cot(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(1/java.lang.Math.tan(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String decimal_format(String number, String pattern, String decimalSeparator, String groupingSeparator) throws Exception {

		//Try to get the value
		try {
			
			if (number==null || number.equals("")) {
				return null;
			}

			DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
			unusualSymbols.setDecimalSeparator(decimalSeparator.charAt(0));
			unusualSymbols.setGroupingSeparator(groupingSeparator.charAt(0));

			DecimalFormat myFormatter = new DecimalFormat(pattern, unusualSymbols);
			return myFormatter.format(Double.parseDouble(number));

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String deg_to_rad(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.toRadians(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String e() {

		//Try to get the value
		return ""+(java.lang.Math.E);

	}

	public static String exp(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.exp(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String expm1(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.expm1(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String floor(String number) throws Exception {

		//Try to execute the function
		try {

			//Execute the floor function
			String result = ""+(java.lang.Math.floor(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String hypot(String number1, String number2) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.hypot(Double.parseDouble(number1), Double.parseDouble(number2)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String log(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.log(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String log10(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.log10(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String log1p(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.log1p(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String max(String number1, String number2) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.max(Double.parseDouble(number1), Double.parseDouble(number2)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String min(String number1, String number2) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.min(Double.parseDouble(number1), Double.parseDouble(number2)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String mod(String number1, String number2) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(Long.parseLong(number1) % Long.parseLong(number2));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String pi() {

		//Try to get the value
		return ""+(java.lang.Math.PI);

	}

	public static String pow(String number1, String number2) throws Exception {

		//Get the value of a number raised to the power of another number
		return power(number1, number2);

	}

	public static String power(String number1, String number2) throws Exception {

		//Try to get the power
		try {

			//Get the power
			String result = ""+(java.lang.Math.pow(Double.parseDouble(number1), Double.parseDouble(number2)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String rad_to_deg(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.toDegrees(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}
	
	public static String random(String number) throws Exception {

		//Try to get the random
		try {

			//Get the random number
			String result = ""+(java.lang.Math.floor(java.lang.Math.random()*Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}
	
	public static String rint(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.rint(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}
	
	public static String round(String number1, String number2) throws Exception {

		//Try to round
		try {

			//Get the round number
			double val = Double.parseDouble(number1);
			val *= java.lang.Math.pow(10, Double.parseDouble(number2))*1.0;
			val = java.lang.Math.floor(val+0.5);
			val /= java.lang.Math.pow(10, Double.parseDouble(number2))*1.0;
			String result = ""+val;
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

	public static String sign(String number) throws Exception {

		//Try to get the sign
		try {

			//Get the sign
			if (Double.parseDouble(number)<0) return "-1";
			else if (Double.parseDouble(number)>0) return "1";
			else return "0";

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}
	
	public static String signum(String number) throws Exception {

		//Get the sign of a number
		return sign(number);

	}
	
	public static String sin(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.sin(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}
	
	public static String sinh(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.sinh(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}
	
	public static String sqrt(String number) throws Exception {

		//Try to get the square root
		try {

			//Get the square root
			String result = ""+(java.lang.Math.sqrt(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}
	
	public static String tan(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.tan(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}
	
	public static String tanh(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.tanh(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}
	
	public static String ulp(String number) throws Exception {

		//Try to get the value
		try {

			//Get the value
			String result = ""+(java.lang.Math.ulp(Double.parseDouble(number)));
			if (result.endsWith(".0")) return result.substring(0, result.length()-2);
			else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, an error appears ("+e.getMessage()+").");

		}

	}

}
