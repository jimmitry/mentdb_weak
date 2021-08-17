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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;

//The string class
public class StringFx {
	
	public static String ascii(String chr) {

		//Convert a character to an integer
		return char_to_int(chr);

	}
	
	public static String bin(String num) {

		//Return null if num is null
		if (num==null) {

			return null;

		} else {

			try {

				//Return the binary representation
				return Long.toBinaryString(Long.parseLong(num));

			} catch (Exception e) {

				return null;

			}

		}

	}
	
	public static String bit_length(String str) {

		//Return null if str is null
		if (str==null) {

			return null;

		} else {

			//Return the bit length
			return ""+(8*str.length());

		}

	}

	public static String char_str(String num) {

		//Convert int to char
		return int_to_char(num);

	}

	public static String char_length(String str) {

		//Get the size of the string
		return length(str);

	}

	public static String char_to_int(String chr) {

		//Try to get the integer
		try {

			//Return null if chr is null
			if (chr==null) {

				return null;

			} else {

				//If chr is an empty string then return 0 else return the integer
				if (chr.equals("")) return "0";
				else return ""+((int) chr.toCharArray()[0]);

			}

		} catch (Exception e) {

			//If an error was generated then return null
			return null;

		}

	}

	public static String count(String string, String find)
	{
		int count = 0;
		int indexOf = 0;

		if (string==null || find==null || string.equals("") || find.equals("")) {

			return null;

		} else {

			while (indexOf > -1)
			{
				indexOf = string.indexOf(find, indexOf);
				if (indexOf > -1) {
					count++;
					indexOf+=find.length();
				}
			}

			return ""+count;

		}
	}

	public static String encode_b64(String string)
	{
		
		if (string==null || string.equals("")) return string;
		
		//Initialization
		byte[] encoded = Base64.getEncoder().encode(string.getBytes());
		
		return new String(encoded);
		
	}

	public static String decode_b64(String string)
	{
		
		if (string==null || string.equals("")) return string;
		
		//Initialization
		byte[] decoded = Base64.getDecoder().decode(string.getBytes());
		
		return new String(decoded);
		
	}

	public static String del_char_before_each_line(String data, String nbChar) throws IOException, Exception {
		
		//Data cannot be null or empty
		if (data==null || data.equals("")) {
			
			throw new Exception("Sorry, data cannot be null or empty.");
		
		}
		
		//Get data from file
		String[] lines = data.split("\n", -1);
		String result = "";
		
		//Check the number of char
		try {
			
			@SuppressWarnings("unused")
			int i = Integer.parseInt(nbChar);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the number of chars must be a number.");
			
		}
		
		//Parse all lines
		for(int i=0;i<lines.length;i++) {
			
			//Get the current line
			String line = lines[i];
			
			if (line.length()<Integer.parseInt(nbChar)) result+="\n";
			else result+=line.substring(Integer.parseInt(nbChar))+"\n";
			
		}

		//Return the result
		return result;

	}

	public static String ends_with(String stringValue, String stringToEnd) {

		//Try to check
		try {

			//If one in all parameters are null then null will be returned
			if (stringValue==null || stringToEnd==null) {

				return null;

			} else {

				if (stringValue.endsWith(stringToEnd)) return "1";
				else return "0";

			}

		} catch (Exception e) {

			//If an error was generated then return null
			return null;

		}

	}

	public static String first_letter_upper(String str) {

		//Try to get the new string
		try {

			//Return null if str is null
			if (str==null) {

				return null;

			} else {

				//Get the string
				return str.substring(0,1).toUpperCase() + str.substring(1,str.length()).toLowerCase();

			}

		} catch (Exception e) {

			//If an error was generated then return null
			return null;

		}

	}

	public static String first_letter(String str) {

		//Try to get the new string
		try {

			//Return null if str is null
			Pattern p = Pattern.compile("\\p{L}");
			Matcher m = p.matcher(str);
			if (m.find()) {
			    return str.charAt(m.start())+"";
			} else return "";

		} catch (Exception e) {

			//If an error was generated then return null
			return null;

		}

	}

	public static String generate_random_str(String size) throws Exception{

		//The size must be a number
		try {
			
			@SuppressWarnings("unused")
			int i = Integer.parseInt(size);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the size must be a number.");
			
		}
		
		//Initialization
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; 
		StringBuffer pass = new StringBuffer();

		//Loop
		for(int x=0;x<Integer.parseInt(size);x++)   {

			//Ad a new char
			int i = (int)java.lang.Math.floor(java.lang.Math.random() * (chars.length() -1));
			pass.append(chars.charAt(i));
			
		}
		
		//Return the string
		return pass.toString();

	}

	public static String hex(String num) {

		//Return null if num is null
		if (num==null) {

			return null;

		} else {

			try {

				//Return the hexadecimal representation
				return Long.toHexString(Long.parseLong(num));

			} catch (Exception e) {

				return null;

			}

		}

	}

	public static String hex_to_int(String hex) {

		//Return null if hex is null
		if (hex==null) {

			return null;

		} else {

			try {

				//Return the number
				return (new BigInteger(hex, 16)).toString();

			} catch (Exception e) {

				return null;

			}

		}

	}

	public static String hex_to_str(String hex) {

		//Return null if hex is null
		if (hex==null) {

			return null;

		} else {

			try {

				StringBuilder sb = new StringBuilder();
				StringBuilder temp = new StringBuilder();

				//49204c6f7665204a617661 split into two characters 49, 20, 4c...
				for( int i=0; i<hex.length()-1; i+=2 ){

					//grab the hex in pairs
					String output = hex.substring(i, (i + 2));
					//convert hex to decimal
					int decimal = Integer.parseInt(output, 16);
					//convert the decimal to character
					sb.append((char)decimal);

					temp.append(decimal);
				}

				return sb.toString();

			} catch (Exception e) {

				return null;

			}

		}

	}

	public static String indent(String str, String nbSpaceBefore) {

		//Try to indent
		try {

			//Return null if str or nbSpaceBefore is null
			if (str==null || nbSpaceBefore==null) {

				return null;

			} else {

				//Get all lines
				String[] list = str.split("\n", -1);
				
				String result = list[0];
				
				//Parse all lines
				for(int i=1;i<list.length;i++) {
					
					//Get the current line
					result += "\n"+space(nbSpaceBefore)+list[i];
					
				}
				
				//Return the string
				return result;

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String instr(String str1, String str2) {

		//Try to get the index
		try {

			//Return null if str1 or str2 is null
			if (str1==null || str2==null) {

				return null;

			} else {

				//Get the index
				return ""+str1.indexOf(str2);

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String instr(String str1, String str2, String fromIndex) {

		//Try to get the index
		try {

			//Return null if one parameter is null
			if (str1==null || str2==null || fromIndex==null) {

				return null;

			} else {

				//Get the index
				return ""+str1.indexOf(str2, Integer.parseInt(fromIndex));

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String int_to_char(String num) {

		//Try to get the string
		try {

			//Return null if num is null
			if (num==null) {

				return null;

			} else {

				//If num is an empty string then null will be returned
				if (num.equals("")) return null;
				else return ""+((char) Integer.parseInt(num));

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String int_to_hex(String num) {

		//INT_TO_HEX
		return hex(num);

	}



	public static String int_to_oct(String num) {

		//INT_TO_OCT
		return oct(num);

	}

	public static String is_letter(String value) {
		if (value.matches("[a-zA-Z]+")) return "1";
		else return "0";
	}

	public static String is_alpha_num_uds(String value) {
		if (value.matches("[a-zA-Z0-9_]+")) return "1";
		else return "0";
	}
	
	public static String is_alpha_num(String value) {
		if (value.matches("[a-zA-Z0-9]+")) return "1";
		else return "0";
	}

	public static String is_number_char(String value) {
		if (value.matches("[0-9]+")) return "1";
		else return "0";
	}

	public static String itrim(String str) {

		//Try to delete space inside
		try {

			//Return null if str is null
			if (str==null) {

				return null;

			} else {

				//Delete space inside
				return str.replaceAll("\\b\\s{2,}\\b", " ");

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String lcase(String str) {

		//LOWER
		return lower(str);

	}
	
	public static String left(String str, String len) {

		//Try to get string
		try {

			//Return null if one parameter is null
			if (str==null || len==null) {

				return null;

			} else {

				if (str.length()<=Integer.parseInt(len)) {
					return str;
				} else return str.substring(0, Integer.parseInt(len));

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String length(String str) {

		//If str is null then return null
		if (str==null) {

			return null;

		} else {

			//Get the length
			return ""+str.length();

		}

	}

	public static String like(String str, String pat) {

		//All parameters can not be null (return null in this case)
		return regexp(str, pat);

	}

	public static String locate(String str1, String str2) {

		//locate
		return instr(str1, str2);

	}

	public static String locate(String str1, String str2, String fromIndex) {

		//locate
		return instr(str1, str2, fromIndex);

	}

	public static String lower(String str) {

		//Try to convert
		try {

			//Return null if str parameter is null
			if (str==null) {

				return null;

			} else {

				//Convert
				return str.toLowerCase();

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String lpad(String str, String padString, String paddedLength) {

		//Initialization
		String strTmp=str;

		//Try to complete the string
		try {

			//Return null if one parameter is null
			if (str==null || padString==null || paddedLength==null) {

				return null;

			} else {

				//Complete the string
				for(int i=str.length();i<Integer.parseInt(paddedLength);i++) {
					strTmp=padString+strTmp;
				}

				//Return the left pad string
				return strTmp;

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String lrtrim(String str) {

		//Try to delete space characters
		try {

			//Return null if str is null
			if (str==null) {

				return null;

			} else {

				//Delete space characters
				return ltrim(rtrim(str));

			}

		} catch (Exception e) {

			//If an error was generated then return null
			return null;

		}

	}

	public static String lrtrim0(String str) {

		//Try to delete space characters
		try {

			//Return null if str is null
			if (str==null) {

				return null;

			} else {
				int iBegin=0;
				for(iBegin=0;iBegin<=str.length();iBegin++) {
					
					if (str.charAt(iBegin)!='0') {
						break;
					}
					
				}

				int iEnd=0;
				for(iEnd=str.length()-1;iEnd>=0;iEnd--) {
					
					if (str.charAt(iEnd)!='0') {
						break;
					}
					
				}
				
				if (iBegin<iEnd) {
					
					return str.substring(iBegin, iEnd+1);
					
				} else return null;

			}

		} catch (Exception e) {

			//If an error was generated then return null
			return null;

		}

	}

	public static String ltrim(String str) {

		//Try to delete space characters
		try {

			//Return null if str is null
			if (str==null) {

				return null;

			} else {

				//Delete space characters
				return str.replaceAll("^\\s+", "");

			}

		} catch (Exception e) {

			//If an error was generated then return null
			return null;

		}

	}

	public static String matches(String str, String pat) {

		//LIKE
		return regexp(str, pat);

	}

	public static String mid(String str, String index) {

		//SUBSTR
		return substr(str, index);

	}

	public static String mid(String str, String beginIndex, String endIndex) {

		//SUBSTR
		return substr(str, beginIndex, endIndex);

	}

	public static String not_like(String str, String pat) {

		//Check if the value1 is not like than the second
		return not_regexp(str, pat);

	}

	public static String not_regexp(String str, String pat) {

		//Try to search
		try {

			//Return null if one parameter is null
			if (str==null || pat==null) {

				return null;

			} else {

				//the value
				Pattern p = Pattern.compile(pat);
				Matcher m = p.matcher(str);
				if (m.matches()) return "0";
				else return "1";

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String oct(String num) {

		//Return null if num is null
		if (num==null) {

			return null;

		} else {

			try {

				//Return the octal representation
				return Long.toOctalString(Long.parseLong(num));

			} catch (Exception e) {

				return null;

			}

		}

	}

	public static String oct_to_int(String oct) {

		//Return null if oct is null
		if (oct==null) {

			return null;

		} else {

			try {

				//Return the number
				return (new BigInteger(oct, 8)).toString();

			} catch (Exception e) {

				return null;

			}

		}

	}

	public static String position(String str1, String str2) {

		//locate
		return instr(str1, str2);

	}

	public static String position(String str1, String str2, String fromIndex) {

		//locate
		return instr(str1, str2, fromIndex);

	}

	public static String regexp(String str, String pat) {

		//Try to search
		try {

			//Return null if one parameter is null
			if (str==null || pat==null) {

				return null;

			} else {

				//the value
				Pattern p = Pattern.compile(pat);
				Matcher m = p.matcher(str);
				if (m.matches()) return "1";
				else return "0";

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String repeat(String str, String count) {

		//Initialization
		String result = "";

		//Try to repeat the string
		try {

			//Return null if one parameter is null
			if (str==null || count==null) {

				return null;

			} else {

				//the value
				for(int i=1;i<=Integer.parseInt(count);i++) {
					result+=str;
				}

				return result;

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String repeat_insert_str(String str, String strToInsert, String incr) throws Exception {

		//Initialization
		String result = "";
		int i = 1;
		
		if (str==null || str.equals("") || strToInsert==null || strToInsert.equals("")) {
			
			return str;
			
		}
		
		//Check the increment
		try {
			
			i = Integer.parseInt(incr);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the increment must be a number.");
			
		}
		
		int index = 0;
		while (index < str.length()) {
			result += str.substring(index, java.lang.Math.min(index + i,str.length())) + strToInsert;
		    index += i;
		}
		
		return result;

	}

	public static String replace(String str, String target, String replacement) {

		//Try to replace
		try {

			//Return null if one parameter is null
			if (str==null || target==null || replacement==null) {

				return null;

			} else {

				//Replace
				return str.replace(target, replacement);

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String reverse(String str) {

		//Try to reverse
		try {

			//Return null if str is null
			if (str==null) {

				return null;

			} else {

				//Reverse
				return new StringBuffer(str).reverse().toString();

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String right(String str, String len) {

		//Try to get string
		try {

			//Return null if one parameter is null
			if (str==null || len==null) {

				return null;

			} else {
				
				if (str.length()<=Integer.parseInt(len)) {
					return str;
				} else return str.substring(str.length()-Integer.parseInt(len), str.length());

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String rpad(String str, String padString, String paddedLength) {

		//Initialization
		String strTmp=str;

		//Try to complete the string
		try {

			//Return null if one parameter is null
			if (str==null || padString==null || paddedLength==null) {

				return null;

			} else {

				//Complete the string
				for(int i=str.length();i<Integer.parseInt(paddedLength);i++) {
					strTmp+=padString;
				}

				//Return the right pad string
				return strTmp;

			}

		} catch (Exception e) {

			//If an error was generated then return null
			return null;

		}

	}

	public static String rtrim(String str) {

		//Try to delete space characters
		try {

			//Return null if str is null
			if (str==null) {

				return null;

			} else {

				//Delete space characters
				return str.replaceAll("\\s+$", "");

			}

		} catch (Exception e) {

			//If an error was generated then return null
			return null;

		}

	}

	public static String space(String count) {

		//Initialization
		String result = "";

		//Try repeat
		try {

			//Return null if count is null
			if (count==null) {

				return null;

			} else {

				//Concatenation
				for(int i=1;i<=Integer.parseInt(count);i++) {
					result+=" ";
				}

				return result;

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	@SuppressWarnings("unchecked")
	public static String split(String str, String regex, String limit) {

		//Try to split
		try {

			JSONArray result = new JSONArray();
			
			String[] list = str.split(regex, Integer.parseInt(limit));
			
			for(int i=0;i<list.length;i++) {
				result.add(list[i]);
			}

			return result.toJSONString();

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String starts_with(String stringValue, String stringToStart) {

		//Try to test
		try {

			//If one in all parameters are null then null will be returned
			if (stringValue==null || stringToStart==null) {

				return null;

			} else {

				//Make the test
				if (stringValue.startsWith(stringToStart)) return "1";
				else return "0";

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String str_to_hex(String str) {

		//Return null if str is null
		if (str==null) {

			return null;

		} else {

			try {

				//Return the hexadecimal representation
				char[] chars = str.toCharArray();

				StringBuffer hex = new StringBuffer();
				for(int i = 0; i < chars.length; i++){
					hex.append(Integer.toHexString((int)chars[i]));
				}

				return hex.toString();

			} catch (Exception e) {

				return null;

			}

		}

	}

	public static String strcmp(String str1, String str2) {

		//Return null if one parameter is null
		if (str1==null || str2==null) {

			return null;

		} else {

			//Compare
			return ""+str1.compareTo(str2);

		}

	}

	public static String strpos(String str1, String str2) {

		//locate
		return instr(str1, str2);

	}

	public static String strpos(String str1, String str2, String fromIndex) {

		//locate
		return instr(str1, str2, fromIndex);

	}

	public static String sublrchar(String str, String numberDeleteChar) {

		//Try to delete chars
		try {

			//Return null if one parameter is null
			if (str==null || numberDeleteChar==null) {

				return null;

			} else {

				//Delete chars
				return str.substring(0, str.length()-Integer.parseInt(numberDeleteChar)).substring(Integer.parseInt(numberDeleteChar));

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String substr(String str, String index) {

		//Get the sub string
		try {

			//Return null if one parameter is null
			if (str==null || index==null) {

				return null;

			} else {

				//Get the sub string
				if (Integer.parseInt(index)>=str.length()) return "";
				else return ""+str.substring(Integer.parseInt(index));

			}

		} catch (Exception e) {

			//If an error was generated then return null
			return null;

		}

	}

	public static String substr(String str, String beginIndex, String endIndex) {

		//Try to get the sub string
		try {

			//Return null if one parameter is null
			if (str==null || beginIndex==null || endIndex==null) {

				return null;

			} else {

				//Return null if beginIndex and endIndex are not valid
				if (Integer.parseInt(beginIndex)>=str.length() || Integer.parseInt(endIndex)<Integer.parseInt(beginIndex)) return "";
				else {

					//Get the sub string
					if (Integer.parseInt(endIndex)>str.length()) return ""+str.substring(Integer.parseInt(beginIndex), str.length());
					else return ""+str.substring(Integer.parseInt(beginIndex), Integer.parseInt(endIndex));

				}

			}

		} catch (Exception e) {

			//If an error was generated then return null
			return null;

		}

	}

	public static String substring(String str, String index) {

		//SUBSTR
		return substr(str, index);

	}

	public static String substring(String str, String beginIndex, String endIndex) {

		//SUBSTR
		return substr(str, beginIndex, endIndex);

	}

	public static String to_string(String str) {

		//TXT
		return txt(str);

	}

	public static String encode(String str, String sourceEnc, String destinationEnc) throws UnsupportedEncodingException {
		
		//Return null if data is null
		if (str==null || sourceEnc==null || sourceEnc.equals("") || destinationEnc==null || destinationEnc.equals("")) {

			return null;

		} else {

			return new String(str.getBytes(sourceEnc), destinationEnc);
			
		}

	}

	public static String empty_if_null(String str) {
		
		//Return empty if data is null
		if (str==null) {

			return "";

		} else {
			
			return str;
			
		}

	}

	public static String null_if_empty(String str) {
		
		//Return null if data is empty
		if (str!=null && str.equals("")) {

			return null;

		} else {
			
			return str;
			
		}

	}

	public static String trim(String str) {

		//Try to delete space characters
		return itrim(ltrim(rtrim(str)));

	}

	public static String txt(String data) {

		//Try to generate the SQL string
		try {

			//Return null if data is null
			if (data==null) {

				return "null";

			} else {

				//Replace all quotes in string and complete the string with '
				return "'"+data.replace("'", "\\'")+"'";

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String txt2(String data) {

		//Try to generate the SQL string
		try {

			//Return null if data is null
			if (data==null) {

				return null;

			} else {

				//Replace all quotes in string and complete the string with '
				return "'"+data.replace("'", "''").replace("\\", "\\\\")+"'";

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String ucase(String str) {

		//UPPER
		return upper(str);

	}

	public static String unhex(String hex) {

		return hex_to_int(hex);

	}

	public static String upper(String str) {

		//Try to convert
		try {

			//Return null if str is null
			if (str==null) {

				return null;

			} else {

				//Convert
				return str.toUpperCase();

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String zero(String str) {

		//Return null if str is null
		if (str==null) {

			return null;

		} else {

			//Return a number 0 if a string is empty
			if (str.equals("")) return "0";
			else return str;

		}

	}

}
