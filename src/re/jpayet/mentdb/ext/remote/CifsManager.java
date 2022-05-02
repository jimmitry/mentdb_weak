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

package re.jpayet.mentdb.ext.remote;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.json.JsonManager;

public class CifsManager {

	public static String mkdir(EnvManager env, String dir, String json) throws Exception {

		JSONObject conf = (JSONObject) JsonManager.load(json);

		String hostname = (String) conf.get("hostname");
		String port = (String) conf.get("port");
		
		String domain = (String) conf.get("domain");
		String user = (String) conf.get("user");
		String password = (String) conf.get("password");
		String connectTimeout = (String) conf.get("connectTimeout");
		String readTimeout = (String) conf.get("readTimeout");
		
		String auth = domain+";"+user+":"+password;
		String smbUrl = "smb://"+hostname+(port!=null && !port.equals("")?":"+port:"")+"/"+dir;
		
		//Generate an error if the url is null or empty
		if (smbUrl==null || smbUrl.equals("")) {
			
			throw new Exception("Sorry, the SMB URL cannot be null or empty.");
			
		}
		
		//Generate an error if the connect timeout is not valid
		try {

			if (Integer.parseInt(connectTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the connect timeout is not valid.");

		}
		
		//Generate an error if the read timeout is not valid
		try {

			if (Integer.parseInt(readTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the read timeout is not valid.");

		}
		
		NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication(domain, user, password);
        
        try {
        
	        SmbFile smbf = new SmbFile(smbUrl, authentication);
	
	        smbf.setReadTimeout(Integer.parseInt(readTimeout));
	        smbf.setConnectTimeout(Integer.parseInt(connectTimeout));
	        
	        smbf.mkdir();
	        
        } catch (Exception e) {
        	
        		throw new Exception("Sorry, "+e.getMessage()+". ["+auth+"---"+smbUrl+"]");
        	
        }
        
        return "1";
        
    }
	
	public static String rm(EnvManager env, String file, String json) throws Exception {

		JSONObject conf = (JSONObject) JsonManager.load(json);

		String hostname = (String) conf.get("hostname");
		String port = (String) conf.get("port");
		
		String domain = (String) conf.get("domain");
		String user = (String) conf.get("user");
		String password = (String) conf.get("password");
		String connectTimeout = (String) conf.get("connectTimeout");
		String readTimeout = (String) conf.get("readTimeout");

		String auth = domain+";"+user+":"+password;
		String smbUrl = "smb://"+hostname+(port!=null && !port.equals("")?":"+port:"")+"/"+file;

		//Generate an error if the url is null or empty
		if (smbUrl==null || smbUrl.equals("")) {
			
			throw new Exception("Sorry, the SMB URL cannot be null or empty.");
			
		}

		//Generate an error if auth is null or empty
		if (auth==null || auth.equals("")) {
			
			throw new Exception("Sorry, the auth string cannot be null or empty.");
			
		}
		
		//Generate an error if the connect timeout is not valid
		try {

			if (Integer.parseInt(connectTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the connect timeout is not valid.");

		}
		
		//Generate an error if the read timeout is not valid
		try {

			if (Integer.parseInt(readTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the read timeout is not valid.");

		}
		
		NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication(domain, user, password);
        
        try {
        
	        SmbFile smbf = new SmbFile(smbUrl, authentication);
	
	        smbf.setReadTimeout(Integer.parseInt(readTimeout));
	        smbf.setConnectTimeout(Integer.parseInt(connectTimeout));
	        
	        smbf.delete();
	        
        } catch (Exception e) {
        	
    			throw new Exception("Sorry, "+e.getMessage()+". ["+auth+"---"+smbUrl+"]");
        	
        }
        
        return "1";
        
    }
	
	@SuppressWarnings("unchecked")
	public static String ls(EnvManager env, String dir, String json) throws Exception {

		JSONObject conf = (JSONObject) JsonManager.load(json);

		String hostname = (String) conf.get("hostname");
		String port = (String) conf.get("port");
		
		String domain = (String) conf.get("domain");
		String user = (String) conf.get("user");
		String password = (String) conf.get("password");
		String connectTimeout = (String) conf.get("connectTimeout");
		String readTimeout = (String) conf.get("readTimeout");

		String auth = domain+";"+user+":"+password;
		String smbUrl = "smb://"+hostname+(port!=null && !port.equals("")?":"+port:"")+"/"+dir;

		//Generate an error if the url is null or empty
		if (smbUrl==null || smbUrl.equals("")) {
			
			throw new Exception("Sorry, the SMB URL cannot be null or empty.");
			
		}
		
		//Generate an error if the connect timeout is not valid
		try {

			if (Integer.parseInt(connectTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the connect timeout is not valid.");

		}
		
		//Generate an error if the read timeout is not valid
		try {

			if (Integer.parseInt(readTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the read timeout is not valid.");

		}
		
		String result = "";
		
		NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication(domain, user, password);
        
        try {
        
	        SmbFile smbf = new SmbFile(smbUrl, authentication);
	
	        smbf.setReadTimeout(Integer.parseInt(readTimeout));
	        smbf.setConnectTimeout(Integer.parseInt(connectTimeout));
	        
	        SmbFile[] list = smbf.listFiles();
	        
	        JSONObject table = new JSONObject();
			table.put("table", "FILES");
			JSONArray columns = new JSONArray();
			table.put("columns", columns);
			columns.add("filename");
			columns.add("isDirectory");
			columns.add("lastModified");
			JSONArray data = new JSONArray();
			table.put("data", data);
	        
	        for(int i=0;i<list.length;i++) {

				JSONArray loc = new JSONArray();
				loc.add(list[i].getName());
				loc.add(list[i].isDirectory());
				loc.add(DateFx.long_to_ts(""+list[i].getLastModified()));
		        data.add(loc);
	        	
	        }
	        
	        result = table.toJSONString();
	        
        } catch (Exception e) {
        	
    			throw new Exception("Sorry, "+e.getMessage()+". ["+auth+"---"+smbUrl+"]");
        	
        }
        
        return result;
        
    }
	
	@SuppressWarnings("unchecked")
	public static String ls(EnvManager env, String dir, String filter, String json) throws Exception {

		JSONObject conf = (JSONObject) JsonManager.load(json);

		String hostname = (String) conf.get("hostname");
		String port = (String) conf.get("port");
		
		String domain = (String) conf.get("domain");
		String user = (String) conf.get("user");
		String password = (String) conf.get("password");
		String connectTimeout = (String) conf.get("connectTimeout");
		String readTimeout = (String) conf.get("readTimeout");

		String auth = domain+";"+user+":"+password;
		String smbUrl = "smb://"+hostname+(port!=null && !port.equals("")?":"+port:"")+"/"+dir;

		//Generate an error if the url is null or empty
		if (smbUrl==null || smbUrl.equals("")) {
			
			throw new Exception("Sorry, the SMB URL cannot be null or empty.");
			
		}
		
		//Generate an error if the connect timeout is not valid
		try {

			if (Integer.parseInt(connectTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the connect timeout is not valid.");

		}
		
		//Generate an error if the read timeout is not valid
		try {

			if (Integer.parseInt(readTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the read timeout is not valid.");

		}
		
		String result = "";
		
		NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication(domain, user, password);
        
        try {
        
	        SmbFile smbf = new SmbFile(smbUrl, authentication);
	
	        smbf.setReadTimeout(Integer.parseInt(readTimeout));
	        smbf.setConnectTimeout(Integer.parseInt(connectTimeout));
	        
	        SmbFile[] list = smbf.listFiles();
	        
	        JSONObject table = new JSONObject();
			table.put("table", "FILES");
			JSONArray columns = new JSONArray();
			table.put("columns", columns);
			columns.add("filename");
			columns.add("isDirectory");
			columns.add("lastModified");
			JSONArray data = new JSONArray();
			table.put("data", data);
	        
	        for(int i=0;i<list.length;i++) {
	        	
	        	if (StringFx.matches(list[i].getName(), filter).equals("1")) {

					JSONArray loc = new JSONArray();
					loc.add(list[i].getName());
					loc.add(list[i].isDirectory());
					loc.add(DateFx.long_to_ts(""+list[i].getLastModified()));
			        data.add(loc);
			        
	        	}
	        	
	        }
	        
	        result = table.toJSONString();
	        
        } catch (Exception e) {
        	
    			throw new Exception("Sorry, "+e.getMessage()+". ["+auth+"---"+smbUrl+"]");
        	
        }
        
        return result;
        
    }
	
	public static String rename(EnvManager env, String file, String renameTo, String json) throws Exception {

		JSONObject conf = (JSONObject) JsonManager.load(json);

		String hostname = (String) conf.get("hostname");
		String port = (String) conf.get("port");
		
		String domain = (String) conf.get("domain");
		String user = (String) conf.get("user");
		String password = (String) conf.get("password");
		String connectTimeout = (String) conf.get("connectTimeout");
		String readTimeout = (String) conf.get("readTimeout");

		String auth = domain+";"+user+":"+password;
		String smbUrl = "smb://"+hostname+(port!=null && !port.equals("")?":"+port:"")+"/"+file;
		String smbUrlRenameTo = "smb://"+hostname+(port!=null && !port.equals("")?":"+port:"")+"/"+renameTo;

		//Generate an error if the url is null or empty
		if (smbUrl==null || smbUrl.equals("")) {
			
			throw new Exception("Sorry, the SMB URL cannot be null or empty.");
			
		}

		//Generate an error if smbUrlRenameTo is null or empty
		if (smbUrlRenameTo==null || smbUrlRenameTo.equals("")) {
			
			throw new Exception("Sorry, the new SMB URL cannot be null or empty.");
			
		}

		//Generate an error if auth is null or empty
		if (auth==null || auth.equals("")) {
			
			throw new Exception("Sorry, the auth string cannot be null or empty.");
			
		}
		
		//Generate an error if the connect timeout is not valid
		try {

			if (Integer.parseInt(connectTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the connect timeout is not valid.");

		}
		
		//Generate an error if the read timeout is not valid
		try {

			if (Integer.parseInt(readTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the read timeout is not valid.");

		}
		
		NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication(domain, user, password);
        
        try {
        
	        SmbFile smbf = new SmbFile(smbUrl, authentication);
	
	        smbf.setReadTimeout(Integer.parseInt(readTimeout));
	        smbf.setConnectTimeout(Integer.parseInt(connectTimeout));
	        
	        smbf.renameTo(new SmbFile(smbUrlRenameTo, authentication));
	        
        } catch (Exception e) {
        	
    			throw new Exception("Sorry, "+e.getMessage()+". ["+auth+"---"+smbUrl+"]");
        	
        }
        
        return "1";
        
    }
	
	public static String get(EnvManager env, String remoteFile, String localFile, String json) throws Exception {

		JSONObject conf = (JSONObject) JsonManager.load(json);

		String hostname = (String) conf.get("hostname");
		String port = (String) conf.get("port");
		
		String domain = (String) conf.get("domain");
		String user = (String) conf.get("user");
		String password = (String) conf.get("password");
		String connectTimeout = (String) conf.get("connectTimeout");
		String readTimeout = (String) conf.get("readTimeout");
		
		String auth = domain+";"+user+":"+password;
		String smbUrl = "smb://"+hostname+(port!=null && !port.equals("")?":"+port:"")+"/"+remoteFile;

		//Generate an error if the url is null or empty
		if (smbUrl==null || smbUrl.equals("")) {
			
			throw new Exception("Sorry, the SMB URL cannot be null or empty.");
			
		}

		//Generate an error if the local file is null or empty
		if (localFile==null || localFile.equals("")) {
			
			throw new Exception("Sorry, the local file name cannot be null or empty.");
			
		}

		//Generate an error if auth is null or empty
		if (auth==null || auth.equals("")) {
			
			throw new Exception("Sorry, the auth string cannot be null or empty.");
			
		}
		
		//Generate an error if the connect timeout is not valid
		try {

			if (Integer.parseInt(connectTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the connect timeout is not valid.");

		}
		
		//Generate an error if the read timeout is not valid
		try {

			if (Integer.parseInt(readTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the read timeout is not valid.");

		}

		NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication(domain, user, password);
        SmbFileInputStream in = null;
        FileOutputStream out = null;
        
        try {
        
	        SmbFile smbf = new SmbFile(smbUrl, authentication);
	
	        smbf.setReadTimeout(Integer.parseInt(readTimeout));
	        smbf.setConnectTimeout(Integer.parseInt(connectTimeout));
	        
	        in = new SmbFileInputStream( smbf );
	        out = new FileOutputStream( localFile );

	        byte[] b = new byte[8192];
	        int n;
	        while(( n = in.read( b )) > 0 ) {
	            out.write( b, 0, n );
	        }
	        
        } catch (Exception e) {
        	
    			throw new Exception("Sorry, "+e.getMessage()+". ["+auth+"---"+smbUrl+"]");
        	
        } finally {
        	
        	try{in.close();} catch (Exception e) {};
        	try{out.close();} catch (Exception e) {};
        	
        }
        
        return "1";
        
    }
	
	public static String put(EnvManager env, String localFile, String remoteFile, String json) throws Exception {

		JSONObject conf = (JSONObject) JsonManager.load(json);

		String hostname = (String) conf.get("hostname");
		String port = (String) conf.get("port");
		
		String domain = (String) conf.get("domain");
		String user = (String) conf.get("user");
		String password = (String) conf.get("password");
		String connectTimeout = (String) conf.get("connectTimeout");
		String readTimeout = (String) conf.get("readTimeout");
		
		String auth = domain+";"+user+":"+password;
		String smbUrl = "smb://"+hostname+(port!=null && !port.equals("")?":"+port:"")+"/"+remoteFile;

		//Generate an error if the url is null or empty
		if (smbUrl==null || smbUrl.equals("")) {
			
			throw new Exception("Sorry, the SMB URL cannot be null or empty.");
			
		}

		//Generate an error if localfile is null or empty
		if (localFile==null || localFile.equals("")) {
			
			throw new Exception("Sorry, the local file name cannot be null or empty.");
			
		}

		//Generate an error if auth is null or empty
		if (auth==null || auth.equals("")) {
			
			throw new Exception("Sorry, the auth string cannot be null or empty.");
			
		}
		
		//Generate an error if the connect timeout is not valid
		try {

			if (Integer.parseInt(connectTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the connect timeout is not valid.");

		}
		
		//Generate an error if the read timeout is not valid
		try {

			if (Integer.parseInt(readTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the read timeout is not valid.");

		}
		
		NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication(domain, user, password);
        FileInputStream in = null;
        SmbFileOutputStream out = null;
        
        try {
        
	        SmbFile smbf = new SmbFile(smbUrl, authentication);
	
	        smbf.setReadTimeout(Integer.parseInt(readTimeout));
	        smbf.setConnectTimeout(Integer.parseInt(connectTimeout));
	        
	        in = new FileInputStream( localFile );
	        out = new SmbFileOutputStream( smbf );

	        byte[] b = new byte[8192];
	        int n;
	        while(( n = in.read( b )) > 0 ) {
	            out.write( b, 0, n );
	        }
	        
        } catch (Exception e) {
        	
    			throw new Exception("Sorry, "+e.getMessage()+". ["+auth+"---"+smbUrl+"]");
        	
        } finally {
        	
        	try{in.close();} catch (Exception e) {};
        	try{out.close();} catch (Exception e) {};
        	
        }
        
        return "1";
        
    }

}