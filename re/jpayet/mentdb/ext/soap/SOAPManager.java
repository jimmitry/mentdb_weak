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

package re.jpayet.mentdb.ext.soap;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.json.simple.JSONObject;

import re.jpayet.mentdb.ext.json.JsonManager;

//The SOAP class
public class SOAPManager {

	public static String execute_http(String url, String jsonHeader, String method, String contentType, String xml) throws Exception {
		
		DataOutputStream wr = null;
		HttpURLConnection con = null;
		JSONObject header = (JSONObject) JsonManager.load(jsonHeader);
		
		try {
			
			URL obj = new URL(url);
			con = (HttpURLConnection) obj.openConnection();
			
			con.setRequestMethod("POST");
			if (contentType!=null && !contentType.equals("")) con.setRequestProperty("Content-type", contentType);
			if (method!=null && !method.equals("")) con.setRequestProperty("SOAPAction", method);
			con.setRequestProperty( "User-Agent", "MentDB/mentdb.org" );
			
			for(Object o : header.keySet()) {
				
				String key = (String) o;
				String value = (String) header.get(key);
				con.setRequestProperty( key, value );
				
			}
			
			con.setDoOutput(true);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(xml);
			wr.flush();
			wr.close();
			
			InputStream _is = null;
			
			if (con.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
			    _is = con.getInputStream();
			    
			    BufferedReader in = new BufferedReader(new InputStreamReader(_is));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				
				return response.toString();
				
			} else {

				System.out.println();
				
			    _is = con.getErrorStream();
			    
			    BufferedReader in = new BufferedReader(new InputStreamReader(_is));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				
				throw new Exception("Sorry, "+con.getResponseMessage()+": "+response.toString());
				
			}
			
		} catch (Exception e) {
			
			try {con.disconnect();} catch (Exception f) {};
			try {wr.close();} catch (Exception f) {};
			
			throw e;
			
		}
		
	}

	public static String execute_https(String url, String jsonHeader, String method, String contentType, String xml) throws Exception {
		
		DataOutputStream wr = null;
		HttpsURLConnection con = null;
		JSONObject header = (JSONObject) JsonManager.load(jsonHeader);
		
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
			
			URL obj = new URL(url);
			con = (HttpsURLConnection) obj.openConnection();
			
			con.setHostnameVerifier(new HostnameVerifier() {
	            @Override
	            public boolean verify(String arg0, SSLSession arg1) {
	                return true;
	            }
	        });
			
			con.setRequestMethod("POST");
			if (contentType!=null && !contentType.equals("")) con.setRequestProperty("Content-type", contentType);
			if (method!=null && !method.equals("")) con.setRequestProperty("SOAPAction", method);
			con.setRequestProperty( "User-Agent", "MentDB/mentdb.org" );
			
			for(Object o : header.keySet()) {
				
				String key = (String) o;
				String value = (String) header.get(key);
				con.setRequestProperty( key, value );
				
			}
			
			con.setDoOutput(true);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(xml);
			wr.flush();
			wr.close();
			
			InputStream _is = null;
			
			if (con.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
			    _is = con.getInputStream();
			    
			    BufferedReader in = new BufferedReader(new InputStreamReader(_is));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				
				return response.toString();
				
			} else {

				System.out.println();
				
			    _is = con.getErrorStream();
			    
			    BufferedReader in = new BufferedReader(new InputStreamReader(_is));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				
				throw new Exception("Sorry, "+con.getResponseMessage()+": "+response.toString());
				
			}
			
		} catch (Exception e) {

			try {con.disconnect();} catch (Exception f) {};
			try {wr.close();} catch (Exception f) {};
			
			throw e;
			
		}
		
	}

}
