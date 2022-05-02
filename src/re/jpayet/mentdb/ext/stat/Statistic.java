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

package re.jpayet.mentdb.ext.stat;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.crypto.Cipher;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sun.management.OperatingSystemMXBean;

import re.jpayet.mentdb.ext.tools.Misc;

//The server threads
public class Statistic extends Thread {
	
	//Statistic
	public static Vector<String> stat_date = new Vector<String>();
	public static String currentDate = "";
	public static OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory
    .getOperatingSystemMXBean();
	
	public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);      
        return keyPairGenerator.genKeyPair();
    }

    public static byte[] encrypt(PrivateKey privateKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  

        return cipher.doFinal(message.getBytes());  
    }
    
    public static byte[] decrypt(PublicKey publicKey, byte [] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        
        return cipher.doFinal(encrypted);
    }
	
	public static String currentMemJvm() {
		
		//Initialization
		String result = "";
		
		Runtime runtime = Runtime.getRuntime();
		
		result = ((runtime.totalMemory() - runtime.freeMemory())/(1024*1024))+"/"+(runtime.totalMemory()/(1024*1024))+"/"+(runtime.maxMemory()/(1024*1024));
		
		return result;
		
	}

	public static String initCurrentDate() {

		//Get the current day number
		Date dt = new Date();
		return new SimpleDateFormat("yyyy-MM-dd").format(dt);

	}

	//Amount of free swap memory in bytes.
	public static long getUsedSwapSpaceSize() {

		return (bean.getTotalSwapSpaceSize()-bean.getFreeSwapSpaceSize())/(1024*1024);

	}

	//Get the system name
	public static String systemName() {

		return bean.getName();

	}

	//Get the system architecture
	public static String systemArchitecture() {

		return bean.getArch();

	}

	//Get the system version
	public static String systemVersion() {

		return bean.getVersion();

	}

	//Get the system nb processor
	public static int systemNbProcessor() {

		return bean.getAvailableProcessors();

	}

	//Get the java vendor
	public static String systemJavaVendor() {

		return System.getProperty("java.vendor");

	}

	//Get the java version
	public static String systemJavaVersion() {

		return System.getProperty("java.version");

	}

	//Get the file system roots
	@SuppressWarnings("unchecked")
	public static String fileSystemRoots() {
		
		JSONArray all = new JSONArray();
		
		/* Get a list of all filesystem roots on this system */
	    File[] roots = File.listRoots();

	    /* For each filesystem root, print some info */
	    for (File root : roots) {
	    	
	    	JSONObject rootObj = new JSONObject();

	    	rootObj.put("absolutePath", ""+root.getAbsolutePath());
	    	rootObj.put("totalSpace", ""+root.getTotalSpace()/(1024*1024));
	    	rootObj.put("freeSpace", ""+root.getFreeSpace()/(1024*1024));
	    	rootObj.put("usedSpace", ""+(root.getTotalSpace()-root.getFreeSpace())/(1024*1024));
	    	
	    	if (root.getTotalSpace()>0) all.add(rootObj);
	        
	    }

		return all.toJSONString();

	}

	//Amount of free swap memory in bytes.
	public static long getFreeSwapSpaceSize() {

		return (bean.getFreeSwapSpaceSize()/(1024*1024));

	}

	//Total amount of swap memory in bytes.
	public static long getTotalSwapSpaceSize() {

		return (bean.getTotalSwapSpaceSize()/(1024*1024));

	}

	//Amount of free physical memory in bytes.
	public static long getUsedPhysicalMemorySize() {

		return (bean.getTotalPhysicalMemorySize()-bean.getFreePhysicalMemorySize())/(1024*1024);

	}

	//Amount of free physical memory in bytes.
	public static long getFreePhysicalMemorySize() {

		return (bean.getFreePhysicalMemorySize()/(1024*1024));

	}

	//Total amount of physical memory in bytes.
	public static long getTotalPhysicalMemorySize() {

		return (bean.getTotalPhysicalMemorySize()/(1024*1024));

	}

	//CPU for whole system
	public static double currentSystemCpuValue() {

		return Misc.round(bean.getSystemCpuLoad()*100, 1);

	}

	//CPU for the JVM
	public static String currentJvmCpuValue() {
		
		return ""+Misc.round(bean.getProcessCpuLoad()*100, 1);

	}

}