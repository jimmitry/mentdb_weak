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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.ext.bot.BotManager;
import re.jpayet.mentdb.ext.bot.SimiRes;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.tools.Misc;

//The string class
public class StringFx {
	
	@SuppressWarnings("unchecked")
	public static String order_asc(String jsonArray) throws Exception {
		
		try {

			JSONArray jsonArr =  Misc.loadArray(jsonArray);
			List<String> jsonValues = new ArrayList<String>();
		    for (int i = 0; i < jsonArr.size(); i++) {
		        jsonValues.add(jsonArr.get(i)+"");
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
	public static JSONArray get_variable(String input, String pattern) throws Exception {
	       
		SimiRes simi = BotManager.similarityCount(input, pattern);
		JSONArray variables = new JSONArray();
		for(int i=0;i<simi.vars.size();i++) {
			variables.add(simi.vars.get(i));
		}
		
        return variables;
       
    }
	
	public static void correction(String[] split_input, HashMap<Integer, ArrayList<Integer>> prob, int n_next) {

		for(int i_input=0;i_input<split_input.length-1;i_input++) {

			ArrayList<Integer> positions_current = prob.get(i_input);
			
			if (positions_current!=null && (i_input+n_next)<split_input.length-1) {
				
				for(int i=0;i<positions_current.size();i++) {
					
					int current = positions_current.get(i);
					
					int nb = 1;
					boolean stop = false;
					
					for(int z=1;z<n_next;z++) {
						
						ArrayList<Integer> positions_next = prob.get(i_input+z);
						
						if (positions_next!=null && positions_next.contains(current+z)) {
							
							nb++;
							
							if (nb==n_next) {
								stop = true;
								break;
							}
							
						} else {
							break;
						}
						
					}
					
					if (stop) {
						
						positions_current.clear();
						delete_aready_use(prob, current);
						positions_current.add(current);
						
						for(int z=1;z<n_next;z++) {
							
							ArrayList<Integer> positions_next = prob.get(i_input+z);
							positions_next.clear();
							delete_aready_use(prob, current+z);
							positions_next.add(current+z);
							
						}
						
						break;
					}
					
				}
				
			}
			
		}
		
	}
	
	public static void delete_aready_use(HashMap<Integer, ArrayList<Integer>> prob, int current_position) {
		
		Iterator<Map.Entry<Integer, ArrayList<Integer>>> iterator = prob.entrySet().iterator();
		while (iterator.hasNext()) {
			
			Map.Entry<Integer, ArrayList<Integer>> e = iterator.next();
			
			ArrayList<Integer> positions = e.getValue();
			
			for(int i=0;i<positions.size();i++) {
				if (positions.get(i)==current_position) {
					positions.remove(i);
				}
			}
			
			
		}
		
	}

	@SuppressWarnings("unchecked")
	public static JSONArray split_word(String word, String chars) {

		//Try to split
		try {
			
			word = StringFx.trim(word);
			JSONArray result = new JSONArray();
			JSONObject minObj = null;
			
			while (word.length()>0) {
				
				minObj = new JSONObject();
				
				for(int i=0;i<chars.length();i++) {
					
					String c = ""+chars.charAt(i);
					int pos = word.indexOf(c);
					if (pos>-1) minObj.put(c, pos);
					
				}
				
				if (minObj.size()>0) {
					
					List<Map.Entry<String, Integer>> list = new LinkedList<>( minObj.entrySet() );
					Collections.sort( list, new Comparator<Map.Entry<String, Integer>>() {
						@Override
						public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
						{
							return ( o1.getValue() ).compareTo( o2.getValue() );
						}
					} );
					
					int min = Integer.parseInt(""+list.get(0).getValue());
					String tmpStr = word.substring(0, min);
					word = word.substring(tmpStr.length()+1);

					result.add(tmpStr);
					result.add(list.get(0).getKey());
					
				} else {
					
					if (word.length()>0) {
						
						String tmpStr = word;
						if (!tmpStr.equals("")) result.add(tmpStr);
						
						word = "";
						
					}
					
				}
				
			}
			
			return result;

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject sentences_distance(String activation_percent, String levenshtein_percent, String sentence1, String sentences2) throws ParseException {

		JSONObject result = new JSONObject();
		JSONObject sentences_2 = (JSONObject) JsonManager.load(sentences2);
		
		double best_value = 0;
		String best_id = "";
		String best_sentence = "";
		String best_predict = "";
		
		for(Object o : sentences_2.keySet()) {
			
			String key = (String) o;
			
			JSONObject obj = (JSONObject) sentences_2.get(key);

			String sentence = obj.get("i")+"";
			String predict = obj.get("o")+"";
			
			double d = sentence_distance(activation_percent, levenshtein_percent, sentence1, sentence);
			
			if (d>best_value) {
				
				best_value = d;
				best_id = key;
				best_sentence = sentence;
				best_predict = predict;
				
			}
			
			obj.put("v", d);
			sentences_2.put(key, obj);
			
		}

		result.put("handle", sentences_2);
		result.put("best_value", best_value);
		result.put("best_id", best_id);
		result.put("best_sentence", best_sentence);
		result.put("best_predict", best_predict);
		
		return result;
		
	}
	
	public static double sentence_distance(String activation_percent, String levenshtein_percent, String sentence1, String sentence2) {

		sentence1 = StringFx.trim(sentence1.toLowerCase().replace("(", "").replace(")", "").replace("é", "e").replace("è", "e").replace("ê", "e").replace("ë", "e").replace("à", "a").replace("ô", "o").replace("û", "u"));
		sentence2 = StringFx.trim(sentence2.toLowerCase().replace("(", "").replace(")", "").replace("é", "e").replace("è", "e").replace("ê", "e").replace("ë", "e").replace("à", "a").replace("ô", "o").replace("û", "u"));
		
		double d_levenshtein_percent = 100-Double.parseDouble(levenshtein_percent);
		
		JSONArray words1 = StringFx.split(sentence1, " ", "-1");
		JSONArray words2 = StringFx.split(sentence2, " ", "-1");
		
		double size_values = words2.size()*Double.parseDouble(activation_percent);
		
		HashMap<String, Vector<Integer>> keys1 = new HashMap<String, Vector<Integer>>();
		for(int i=0;i<words1.size();i++) {
			
			String cur_word = words1.get(i)+"";
			if (keys1.containsKey(cur_word)) {
				keys1.get(cur_word).addElement(i);
			} else {
				keys1.put(cur_word, new Vector<Integer>());
				keys1.get(cur_word).addElement(i);
			}
			
		}
		
		HashMap<Integer, Vector<Integer>> position_to_search = new HashMap<Integer, Vector<Integer>>();
		
		for(int i=0;i<words2.size();i++) {
			
			String currentWord = words2.get(i)+"";
			
			if (keys1.containsKey(currentWord)) {
				
				position_to_search.put(i, keys1.get(currentWord));
			
			}
			
		}
		
		Vector<Double> best_distance = new Vector<Double>();
		for (Entry<Integer, Vector<Integer>> e : position_to_search.entrySet()) {
			
			int pos2 = e.getKey();
			Vector<Integer> pos1v = e.getValue();
			
			for(int i_v=0;i_v<pos1v.size();i_v++) {
				
				double value = 100;
				
				int pos1 = pos1v.get(i_v);
				
				//Parse left
				int tmp_pos1 = pos1-1;
				int tmp_pos2 = pos2-1;
				while (tmp_pos1>=0 && tmp_pos2>=0) {
					
					String w = (""+words1.get(tmp_pos1));
					Integer l = StringUtils.getLevenshteinDistance(w, (""+words2.get(tmp_pos2)));
					double dis = l.doubleValue()/w.length()*100;
					
					if (dis<d_levenshtein_percent) {
						
						value += 100;
						
					} else break;
					
					tmp_pos1--;
					tmp_pos2--;
					
				}
				
				//Parse right
				tmp_pos1 = pos1+1;
				tmp_pos2 = pos2+1;
				while (tmp_pos1<words1.size() && tmp_pos2<words2.size()) {
					
					String w = (""+words1.get(tmp_pos1));
					Integer l = StringUtils.getLevenshteinDistance(w, (""+words2.get(tmp_pos2)));
					double dis = l.doubleValue()/w.length()*100;
					
					if (dis<d_levenshtein_percent) {
						
						value += 100;
						
					} else break;
					
					tmp_pos1++;
					tmp_pos2++;
					
				}
				
				best_distance.add(value);
				
			}
			
		}
		
		Collections.sort(best_distance);
		
		if (best_distance.size()==0) return 0;
		else {
			
			if (words2.size()==1) {
				return best_distance.get(best_distance.size()-1);
			} else {
				double d = best_distance.get(best_distance.size()-1);
				if (d>=size_values) return d;
				else return 0;
			}
			
		}

	}
	
	public static int levenshtein_distance(String word1, String word2) {

		return StringUtils.getLevenshteinDistance(word1, word2);

	}

	public static String mql_encode(String str) {

		return str.replace("\"", "\\\"");

	}
	
	public static String csv_value(String data, String columnSeparator, String quoteChar) {

		try {

			if (data.indexOf(columnSeparator)>-1 
					|| data.indexOf(quoteChar)>-1
					|| data.indexOf("\n")>-1
					|| data.indexOf("\r")>-1) 
				return quoteChar+data.replace(quoteChar, quoteChar+quoteChar)+quoteChar;
			else return data;

		} catch (Exception e) {

			return null;

		}

	}
	
	public static String ascii(String chr) {

		//Convert a character to integer
		return char_to_int(chr);

	}
	
	public static String encode_des_generate_key(String keysize) throws Exception {

		// Get a DES private key
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
		keyGen.init(Integer.parseInt(keysize));
		SecretKey key = keyGen.generateKey();
		
		return Base64.getEncoder().encodeToString(key.getEncoded());

	}
	
	public static String encode_des(String data, String keyStr) throws Exception {

		byte[] plainText = data.getBytes();
		
		SecretKey key = new SecretKeySpec(Base64.getDecoder().decode(keyStr), "DES");
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] cipherText = cipher.doFinal(plainText);
		
		return Base64.getEncoder().encodeToString(cipherText);

	}
	
	public static String decode_des(String data, String keyStr) throws Exception {

		SecretKey key = new SecretKeySpec(Base64.getDecoder().decode(keyStr), "DES");
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] newPlainText = cipher.doFinal(Base64.getDecoder().decode(data));
		
		return new String(newPlainText);

	}
	
	public static String encode_blowfish(String data, String secretKey) throws Exception {

		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "Blowfish");
		Cipher cipherEncode = Cipher.getInstance("Blowfish");
        cipherEncode.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return Base64.getEncoder().encodeToString(cipherEncode.doFinal(data.getBytes()));

	}
	
	public static String decode_blowfish(String data, String secretKey) throws Exception {

		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "Blowfish");
		Cipher cipherDecode = Cipher.getInstance("Blowfish");
		cipherDecode.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return new String(cipherDecode.doFinal(Base64.getDecoder().decode(data)));

	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject encode_sign_generate_key(String keysize) throws Exception {
		
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
	    keyPairGen.initialize(Integer.parseInt(keysize), new SecureRandom());
	    KeyPair kp = keyPairGen.generateKeyPair(); 

	    String pu = Base64.getEncoder().encodeToString(kp.getPublic().getEncoded());
	    String pr = Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded());
	      
		JSONObject result = new JSONObject();
		result.put("publicKey", pu);
		result.put("privateKey", pr);
		
		return result;
	
	}
	
	public static String encode_sign(String data, String privateKeyb64) throws Exception {
		
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyb64));
		KeyFactory keyFactory = KeyFactory.getInstance("DSA");
		PrivateKey privateKey = (PrivateKey)keyFactory.generatePrivate(privateKeySpec);
		
		Signature signature = Signature.getInstance("DSA");
	    signature.initSign(privateKey);
	    signature.update(data.getBytes());
		
		byte[] sig = signature.sign();
		
		return Base64.getEncoder().encodeToString(sig);
		
	}
	
	public static String decode_sign_verify(String data, String sign, String publicKeyb64) throws Exception {
		
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyb64));
		KeyFactory keyFactory = KeyFactory.getInstance("DSA");
		PublicKey publicKey = (PublicKey)keyFactory.generatePublic(publicKeySpec);
		
		Signature signature = Signature.getInstance("DSA");
	    signature.initVerify(publicKey);
	    signature.update(data.getBytes());
	    
		if (signature.verify(Base64.getDecoder().decode(sign))) {
			return "1";
		} else {
			return "0";
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static String encode_pbe(String data, String pwd) throws Exception {

		char[] password = pwd.toCharArray();
        PBEKeySpec keySpec = new PBEKeySpec(password);
        
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey secretKey = keyFactory.generateSecret(keySpec);
        
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] input = data.getBytes();
        
        JSONObject result = new JSONObject();
        result.put("encrypted", Base64.getEncoder().encodeToString(cipher.doFinal(input)));
        
        AlgorithmParameters params = cipher.getParameters();
        result.put("params", Base64.getEncoder().encodeToString(params.getEncoded()));
        
        return result.toJSONString();
        
	}
	
	public static String decode_pbe(String json_data, String pwd) throws Exception {
		
		JSONObject j_s = (JSONObject) JsonManager.load(json_data);
		String data = (String) j_s.get("encrypted");
		String p = (String) j_s.get("params");
		
		AlgorithmParameters params = AlgorithmParameters.getInstance("PBEWithMD5AndDES");
		params.init(Base64.getDecoder().decode(p));

		char[] password = pwd.toCharArray();
        PBEKeySpec keySpec = new PBEKeySpec(password);
        
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey secretKey = keyFactory.generateSecret(keySpec);
        
		Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, params);
        byte[] plain = cipher.doFinal(Base64.getDecoder().decode(data));
        
        return new String(plain);

	}
	
	public static String md5(String str) throws NoSuchAlgorithmException {
		
		MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(str.getBytes(),0,str.length());

		return new BigInteger(1,md.digest()).toString(16);

	}
	
	public static String sha(String str) throws NoSuchAlgorithmException {
		
		MessageDigest md = MessageDigest.getInstance("SHA");
	    md.update(str.getBytes(),0,str.length());

		return new BigInteger(1,md.digest()).toString(16);

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

		//Convert
		return int_to_char(num);

	}

	public static String char_length(String str) {

		//LENGTH
		return length(str);

	}

	public static String char_to_int(String chr) {

		//Try to get the integer
		try {

			//Return null if chr is null
			if (chr==null) {

				return null;

			} else {

				//If chr is empty string then return 0 else return the integer
				if (chr.equals("")) return "0";
				else return ""+((int) chr.toCharArray()[0]);

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String concat(Vector<MQLValue> parameters) throws Exception {

		//Initialization
		StringBuilder result = new StringBuilder("");

		//Concat all values
		for(int i=0;i<parameters.size();i++) {

			if (parameters.get(i).value!=null) result.append(parameters.get(i).value);

		}

		//Return the result of the function
		return result.toString();

	}

	public static String concat_var(Vector<MQLValue> parameters, EnvManager env) throws Exception {
		
		//Get the variable name
		String var = parameters.get(0).value;
		
		//Generate an error if the variable name is not valid
		if (!EnvManager.is_valid_varname(var)) {
			throw new Exception("Sorry, the variable name "+var+" is not valid (example: [var1]).");
		}
		
		if (!env.exist(var)) {
			env.set(var, "");
		}
		
		//Concat all values
		for(int i=1;i<parameters.size();i++) {

			env.concat(var, parameters.get(i).value);

		}
		
		return "1";

	}

	public static String count(String string, String find)
	{
		int count = 0;
		int indexOf = 0;

		if (string==null || find==null || string.equals("") || find.equals("")) {

			return "0";

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

	public static int count_int(String string, String find)
	{
		
		int count = 0;
		int indexOf = 0;
		
		while (indexOf > -1)
		{
			indexOf = string.indexOf(find, indexOf);
			if (indexOf > -1) {
				count++;
				indexOf++;
			}
		}

		return count;
		
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

		//Try to test
		try {

			//If one in all parameters are null then null will be returned
			if (stringValue==null || stringToEnd==null) {

				return null;

			} else {

				//Return the test
				if (stringValue.endsWith(stringToEnd)) return "1";
				else return "0";

			}

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
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

	public static String is_letter_(String value) {
		if (value.matches("[a-zA-Z_]+")) return "1";
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
	public static String split_sentence(String str, String chars) {

		//Try to split
		try {
			
			str = StringFx.trim(str);
			JSONArray result = new JSONArray();
			JSONObject minObj = null;
			
			while (str.length()>0) {
				
				minObj = new JSONObject();
				
				for(int i=0;i<chars.length();i++) {
					
					String c = ""+chars.charAt(i);
					int pos = str.indexOf(c);
					if (pos>-1) minObj.put(c, pos);
					
				}
				
				if (minObj.size()>0) {
					
					List<Map.Entry<String, Integer>> list = new LinkedList<>( minObj.entrySet() );
					Collections.sort( list, new Comparator<Map.Entry<String, Integer>>() {
						@Override
						public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
						{
							return ( o1.getValue() ).compareTo( o2.getValue() );
						}
					} );
					
					int min = Integer.parseInt(""+list.get(0).getValue());
					String tmpStr = str.substring(0, min);
					str = str.substring(tmpStr.length()+1);
					
					if (!tmpStr.equals("")) result.add(StringFx.lrtrim(tmpStr));
					
				} else {
					
					if (str.length()>0) {
						
						String tmpStr = str;
						if (!tmpStr.equals("")) result.add(StringFx.lrtrim(tmpStr));
						
						str = "";
						
					}
					
				}
				
			}
			
			return result.toJSONString();

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	@SuppressWarnings("unchecked")
	public static JSONArray split(String str, String regex, String limit) {

		//Try to split
		try {

			JSONArray result = new JSONArray();

			String[] list = str.split(regex, Integer.parseInt(limit));

			for(int i=0;i<list.length;i++) {
				result.add(list[i]);
			}

			return result;

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static String split_mql(String str, String index) {

		//Try to split
		try {

			return Misc.splitCommand(str).get(0).get(Integer.parseInt(index)).value;

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

	public static String starts_with_or(String stringValue, String stringToStart) {

		//Try to test
		try {

			//If one in all parameters are null then null will be returned
			if (stringValue==null || stringToStart==null) {

				return null;

			} else {

				//Make the test
				boolean b = false;
				for(int i=0;i<stringToStart.length();i++) {
					
					if (stringValue.startsWith(stringToStart.charAt(i)+"")) {
						b=true;
						break;
					}
					
				}
				
				if (b) return "1";
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
				else if (Integer.parseInt(index)<0) return str;
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
