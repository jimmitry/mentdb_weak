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

package re.jpayet.mentdb.ext.rest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.ext.json.JsonManager;

public class REST {
	
	//HTTP request
	@SuppressWarnings("unchecked")
	public static String http_exe(String method, String url, String urlAction, String jsonHeader, String cookies_array) throws Exception {

		JSONObject header = (JSONObject) JsonManager.load(jsonHeader);
		
		//Initialization
		StringBuilder result = new StringBuilder("");

		//Check if the url is null or empty
		if (url==null || url.equals("")) {

			//Generate an exception
			throw new Exception("Sorry, the url cannot be empty.");

		}

		//Generate an error if the method is not valid
		if (method==null || method.equals("")) {

			//Generate an exception
			throw new Exception("Sorry the method cannot be empty.");

		}
		
		method = method.toLowerCase();

		//Generate an error if the method is not valid
		if (!method.equals("get") && !method.equals("post") && !method.equals("delete") && !method.equals("put")) {

			//Generate an exception
			throw new Exception("Sorry the method must be get|post|put|delete.");

		}
		
		HttpURLConnection conn = null;

		try {
			
			//Create the url and open the connection
			URL postURL = new URL( (method.equals("post") || method.equals("put")?url:url+"?"+urlAction) );
			conn = (HttpURLConnection)postURL.openConnection();
			
			// Set connection parameters. We need to perform input and output,
			// so set both as true.
			conn.setDoInput (true);
			conn.setDoOutput (true);

			// Set the content type we are posting.
			// encoded form data
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty( "User-Agent", "MentDB/MentDB.org" );
			
			JSONArray cs_array = (JSONArray) JsonManager.load(cookies_array);
			String cookies_str = "";
			for(int i=0;i<cs_array.size();i++) {
				cookies_str+=";"+cs_array.get(i);
			}
			if (!cookies_str.equals("")) {
				conn.addRequestProperty("Cookie", cookies_str.substring(1));
			}
			
			for(Object o : header.keySet()) {
				
				String key = (String) o;
				String value = (String) header.get(key);
				conn.setRequestProperty( key, value );
				
			}
			
			//Set the method
			conn.setRequestMethod(method.toUpperCase());
			
			//Manage post method
			if (method.equals("post") || method.equals("put")) {
				
				conn.setRequestProperty("Content-length", String.valueOf(urlAction.length())); 
				
				DataOutputStream output = new DataOutputStream(conn.getOutputStream());  
				output.writeBytes(urlAction);
				output.close();
				
			}
			
			// Read input from the input stream.
			int rc = conn.getResponseCode();
			
			if ( rc >= 200 && rc <= 206) {
				
				//Initialization
				BufferedReader is = new BufferedReader(new InputStreamReader( conn.getInputStream()));
				String _line = null;
				
				//Parse the stream
				while(((_line = is.readLine()) !=null))
				{

					//Get the current line
					result.append(_line);

				}
				
				conn.getInputStream().close();
				
			} else {
				
				//Initialization
				BufferedReader is = new BufferedReader(new InputStreamReader( conn.getErrorStream()));
				String _line = null;
				
				//Parse the stream
				while(((_line = is.readLine()) !=null))
				{

					//Get the current line
					result.append(_line);

				}
				
				conn.getErrorStream().close();
				
				throw new Exception(result.toString());
				
			}

		} catch( Exception e ) {
			
			//An error was generated
			throw new Exception("Sorry an exception appears: "+e.getMessage());

		}
		
		JSONObject r = new JSONObject();
		JSONArray reponse_header = new JSONArray();
		r.put("reponse_header", reponse_header);
		JSONArray cookies = new JSONArray();
		r.put("cookies", cookies);
		r.put("reponse", result.toString());;
		if (conn!=null) {
			
			if (conn.getHeaderFields().containsKey("Set-Cookie")) {
				List<String> cs = conn.getHeaderFields().get("Set-Cookie");
				for(int i=0;i<cs.size();i++) {
					cookies.add(cs.get(i));
				}
			}
			
			Map<String, List<String>> map = conn.getHeaderFields();
			for (Map.Entry<String, List<String>> entry : map.entrySet()) {
				reponse_header.add("Key : " + entry.getKey() + " ,Value : " + entry.getValue());
			}
			
			conn.disconnect();
		}
		
		//Return the html page
		return r.toJSONString();

	}
	
	//HTTP request
	@SuppressWarnings("unchecked")
	public static String http_json_post_exe(String url, String jsonHeader, String cookies_array, String jsonData) throws Exception {

		JSONObject header = (JSONObject) JsonManager.load(jsonHeader);
		
		//Initialization
		StringBuilder result = new StringBuilder("");

		//Check if the url is null or empty
		if (url==null || url.equals("")) {

			//Generate an exception
			throw new Exception("Sorry, the url cannot be empty.");

		}
		
		HttpURLConnection conn = null;

		try {

			//Create the url and open the connection
			URL postURL = new URL(url);
			conn = (HttpURLConnection)postURL.openConnection();

			// Set connection parameters. We need to perform input and output,
			// so set both as true.
			conn.setDoInput(true);
			conn.setDoOutput(true);

			// Set the content type we are posting.
			// encoded form data
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty( "User-Agent", "MentDB/MentDB.org" );
			
			JSONArray cs_array = (JSONArray) JsonManager.load(cookies_array);
			String cookies_str = "";
			for(int i=0;i<cs_array.size();i++) {
				cookies_str+=";"+cs_array.get(i);
			}
			if (!cookies_str.equals("")) {
				conn.addRequestProperty("Cookie", cookies_str.substring(1));
			}
			
			for(Object o : header.keySet()) {
				
				String key = (String) o;
				String value = (String) header.get(key);
				conn.setRequestProperty( key, value );
				
			}

			//Set the method
			conn.setRequestMethod("POST");
			
			conn.setRequestProperty("Content-length", String.valueOf(jsonData.length())); 
			
			DataOutputStream output = new DataOutputStream(conn.getOutputStream());  
			output.writeBytes(jsonData);
			output.close();

			// Read input from the input stream.
			int rc = conn.getResponseCode();
if ( rc >= 200 && rc <= 206) {
				
				//Initialization
				BufferedReader is = new BufferedReader(new InputStreamReader( conn.getInputStream()));
				String _line = null;
				
				//Parse the stream
				while(((_line = is.readLine()) !=null))
				{

					//Get the current line
					result.append(_line);

				}
				
				conn.getInputStream().close();
				
			} else {
				
				//Initialization
				BufferedReader is = new BufferedReader(new InputStreamReader( conn.getErrorStream()));
				String _line = null;
				
				//Parse the stream
				while(((_line = is.readLine()) !=null))
				{

					//Get the current line
					result.append(_line);

				}
				
				conn.getErrorStream().close();
				
				throw new Exception(result.toString());
				
			}

		} catch( Exception e ) {

			//An error was generated
			throw new Exception("Sorry an exception appears: "+e.getMessage());

		}
		
		JSONObject r = new JSONObject();
		JSONArray reponse_header = new JSONArray();
		r.put("reponse_header", reponse_header);
		JSONArray cookies = new JSONArray();
		r.put("cookies", cookies);
		r.put("reponse", result.toString());;
		if (conn!=null) {
			
			if (conn.getHeaderFields().containsKey("Set-Cookie")) {
				List<String> cs = conn.getHeaderFields().get("Set-Cookie");
				for(int i=0;i<cs.size();i++) {
					cookies.add(cs.get(i));
				}
			}
			
			Map<String, List<String>> map = conn.getHeaderFields();
			for (Map.Entry<String, List<String>> entry : map.entrySet()) {
				reponse_header.add("Key : " + entry.getKey() + " ,Value : " + entry.getValue());
			}
			
			conn.disconnect();
		}

		//Return the html page
		return r.toJSONString();

	}
	
	@SuppressWarnings("unchecked")
	public static String https_exe(String method, String url, String urlAction, String jsonHeader, String cookies_array) throws Exception {

		JSONObject header = (JSONObject) JsonManager.load(jsonHeader);
		
		//Initialization
		StringBuilder result = new StringBuilder("");

		//Check if the url is null or empty
		if (url==null || url.equals("")) {

			//Generate an exception
			throw new Exception("Sorry, the url cannot be empty.");

		}

		//Generate an error if the method is not valid
		if (method==null || method.equals("")) {

			//Generate an exception
			throw new Exception("Sorry the method cannot be empty.");

		}
		
		method = method.toLowerCase();

		//Generate an error if the method is not valid
		if (!method.equals("get") && !method.equals("post") && !method.equals("delete") && !method.equals("put")) {

			//Generate an exception
			throw new Exception("Sorry the method must be get|post|put|delete.");

		}
		
		HttpsURLConnection conn = null;

		try {
			
			//Create a new trust manager that trust all certificates
			javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[] { new javax.net.ssl.X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(
						java.security.cert.X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(
						java.security.cert.X509Certificate[] certs, String authType) {
				}
			}};
			
			// Install the all-trusting trust manager
	        final SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			//Create the url and open the connection
			URL postURL = new URL( (method.equals("post") || method.equals("put")?url:url+"?"+urlAction) );
			conn = (HttpsURLConnection)postURL.openConnection();
			
			conn.setHostnameVerifier(new HostnameVerifier() {
	            @Override
	            public boolean verify(String arg0, SSLSession arg1) {
	                return true;
	            }
	        });

			// Set connection parameters. We need to perform input and output,
			// so set both as true.
			conn.setDoInput (true);
			conn.setDoOutput (true);

			// Set the content type we are posting.
			// encoded form data
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty( "User-Agent", "MentDB/MentDB.org" );
			
			JSONArray cs_array = (JSONArray) JsonManager.load(cookies_array);
			String cookies_str = "";
			for(int i=0;i<cs_array.size();i++) {
				cookies_str+=";"+cs_array.get(i);
			}
			if (!cookies_str.equals("")) {
				conn.addRequestProperty("Cookie", cookies_str.substring(1));
			}
			
			for(Object o : header.keySet()) {
				
				String key = (String) o;
				String value = (String) header.get(key);
				conn.setRequestProperty( key, value );
				
			}

			//Set the method
			conn.setRequestMethod(method.toUpperCase());
			
			//Manage post method
			if (method.equals("post") || method.equals("put")) {
				
				conn.setRequestProperty("Content-length", String.valueOf(urlAction.length())); 
				
				DataOutputStream output = new DataOutputStream(conn.getOutputStream());  
				output.writeBytes(urlAction);
				output.close();
				
			}

			// Read input from the input stream.
			int rc = conn.getResponseCode();
			
			if ( rc >= 200 && rc <= 206) {
				
				//Initialization
				BufferedReader is = new BufferedReader(new InputStreamReader( conn.getInputStream()));
				String _line = null;
				
				//Parse the stream
				while(((_line = is.readLine()) !=null))
				{

					//Get the current line
					result.append(_line);

				}
				
				conn.getInputStream().close();
				
			} else {
				
				//Initialization
				BufferedReader is = new BufferedReader(new InputStreamReader( conn.getErrorStream()));
				String _line = null;
				
				//Parse the stream
				while(((_line = is.readLine()) !=null))
				{

					//Get the current line
					result.append(_line);

				}
				
				conn.getErrorStream().close();
				
				throw new Exception(result.toString());
				
			}

		} catch( Exception e ) {

			//An error was generated
			throw new Exception("Sorry an exception appears: "+e.getMessage()+"\n"+conn.getResponseMessage());

		}
		
		JSONObject r = new JSONObject();
		JSONArray reponse_header = new JSONArray();
		r.put("reponse_header", reponse_header);
		JSONArray cookies = new JSONArray();
		r.put("cookies", cookies);
		r.put("reponse", result.toString());;
		if (conn!=null) {
			
			if (conn.getHeaderFields().containsKey("Set-Cookie")) {
				List<String> cs = conn.getHeaderFields().get("Set-Cookie");
				for(int i=0;i<cs.size();i++) {
					cookies.add(cs.get(i));
				}
			}
			
			Map<String, List<String>> map = conn.getHeaderFields();
			for (Map.Entry<String, List<String>> entry : map.entrySet()) {
				reponse_header.add("Key : " + entry.getKey() + " ,Value : " + entry.getValue());
			}
			
			conn.disconnect();
		}

		//Return the html page
		return r.toJSONString();

	}
	
	@SuppressWarnings("unchecked")
	public static String https_json_post_exe(String url, String jsonHeader, String cookies_array, String jsonData) throws Exception {

		JSONObject header = (JSONObject) JsonManager.load(jsonHeader);
		
		//Initialization
		StringBuilder result = new StringBuilder("");

		//Check if the url is null or empty
		if (url==null || url.equals("")) {

			//Generate an exception
			throw new Exception("Sorry, the url cannot be empty.");

		}
		
		HttpsURLConnection conn = null;

		try {
			
			//Create a new trust manager that trust all certificates
			javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[] { new javax.net.ssl.X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(
						java.security.cert.X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(
						java.security.cert.X509Certificate[] certs, String authType) {
				}
			}};
			
			// Install the all-trusting trust manager
	        final SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			//Create the url and open the connection
			URL postURL = new URL( url );
			conn = (HttpsURLConnection)postURL.openConnection();
			
			conn.setHostnameVerifier(new HostnameVerifier() {
	            @Override
	            public boolean verify(String arg0, SSLSession arg1) {
	                return true;
	            }
	        });

			// Set connection parameters. We need to perform input and output,
			// so set both as true.
			conn.setDoInput (true);
			conn.setDoOutput (true);

			// Set the content type we are posting.
			// encoded form data
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty( "User-Agent", "MentDB/MentDB.org" );
			
			JSONArray cs_array = (JSONArray) JsonManager.load(cookies_array);
			String cookies_str = "";
			for(int i=0;i<cs_array.size();i++) {
				cookies_str+=";"+cs_array.get(i);
			}
			if (!cookies_str.equals("")) {
				conn.addRequestProperty("Cookie", cookies_str.substring(1));
			}
			
			for(Object o : header.keySet()) {
				
				String key = (String) o;
				String value = (String) header.get(key);
				conn.setRequestProperty( key, value );
				
			}

			//Set the method
			conn.setRequestMethod("POST");
			
			//Manage post method
			conn.setRequestProperty("Content-length", String.valueOf(jsonData.length())); 
			
			DataOutputStream output = new DataOutputStream(conn.getOutputStream());  
			output.writeBytes(jsonData);
			output.close();

			// Read input from the input stream.
			int rc = conn.getResponseCode();
if ( rc >= 200 && rc <= 206) {
				
				//Initialization
				BufferedReader is = new BufferedReader(new InputStreamReader( conn.getInputStream()));
				String _line = null;
				
				//Parse the stream
				while(((_line = is.readLine()) !=null))
				{

					//Get the current line
					result.append(_line);

				}
				
				conn.getInputStream().close();
				
			} else {
				
				//Initialization
				BufferedReader is = new BufferedReader(new InputStreamReader( conn.getErrorStream()));
				String _line = null;
				
				//Parse the stream
				while(((_line = is.readLine()) !=null))
				{

					//Get the current line
					result.append(_line);

				}
				
				conn.getErrorStream().close();
				
				throw new Exception(result.toString());
				
			}

		} catch( Exception e ) {

			//An error was generated
			throw new Exception("Sorry an exception appears: "+e.getMessage());

		}
		
		JSONObject r = new JSONObject();
		JSONArray reponse_header = new JSONArray();
		r.put("reponse_header", reponse_header);
		JSONArray cookies = new JSONArray();
		r.put("cookies", cookies);
		r.put("reponse", result.toString());;
		if (conn!=null) {
			
			if (conn.getHeaderFields().containsKey("Set-Cookie")) {
				List<String> cs = conn.getHeaderFields().get("Set-Cookie");
				for(int i=0;i<cs.size();i++) {
					cookies.add(cs.get(i));
				}
			}
			
			Map<String, List<String>> map = conn.getHeaderFields();
			for (Map.Entry<String, List<String>> entry : map.entrySet()) {
				reponse_header.add("Key : " + entry.getKey() + " ,Value : " + entry.getValue());
			}
			
			conn.disconnect();
		}

		//Return the html page
		return r.toJSONString();

	}

}
