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

package re.jpayet.mentdb.ext.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Pattern;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.encoders.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.fx.TypeFx;
import re.jpayet.mentdb.ext.server.Start;

//The miscellaneous class
public class Misc {
	
	public static String build_CA_DN() {
		
		return "EmailAddress="+Start.CA_EMAIL_ADDRESS+","+
				"CN="+Start.CA_CN+","+
				"OU="+Start.CA_OU+","+
				"O="+Start.CA_O+","+
				"L="+Start.CA_L+","+
				"ST="+Start.CA_ST+","+
				"C="+Start.CA_C;
		
	}
	
	public static String build_CU_DN() {
		
		return "EmailAddress="+Start.CU_EMAIL_ADDRESS+","+
				"CN="+Start.CU_CN+","+
				"OU="+Start.CU_OU+","+
				"O="+Start.CU_O+","+
				"L="+Start.CU_L+","+
				"ST="+Start.CU_ST+","+
				"C="+Start.CU_C;
		
	}
	
	public static String generate_ca() throws Exception {

		//Generate the cert directory if not exist
		if (!(new File("cert")).exists()) {
			(new File("cert")).mkdir();
		}
		
		//Generate the public/private rsa key pair
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
		keyPairGenerator.initialize(Start.CERT_KEY_SIZE, new SecureRandom());
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		
		String DN = build_CA_DN();

		//Create the certificate builder
		X509v3CertificateBuilder certBldr = new JcaX509v3CertificateBuilder(
				new X500Name(DN),
				BigInteger.valueOf(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis() + 1000 * 3600 * 24 * Start.CA_DAY_LIMIT),
				new X500Name(DN), keyPair.getPublic()).addExtension(
				        new ASN1ObjectIdentifier("2.5.29.19"), 
				        true,
				        new BasicConstraints(0));
		JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
		certBldr.addExtension(
				Extension.subjectKeyIdentifier, false,
				extUtils.createSubjectKeyIdentifier(keyPair.getPublic()));
		
		

		//Get the signature
		ContentSigner signer = new JcaContentSignerBuilder("SHA1WithRSAEncryption").setProvider("BC").build(keyPair.getPrivate());

		//Get the certificate signed by the private key
		X509Certificate ca_cert_to_distribute = new JcaX509CertificateConverter().setProvider("BC")
				.getCertificate(certBldr.build(signer));

		ca_cert_to_distribute.verify(ca_cert_to_distribute.getPublicKey());

		FileWriter fcert = new FileWriter("cert/ca_cert.pem");
		JcaPEMWriter pemWriterCert = new JcaPEMWriter(fcert);
		pemWriterCert.writeObject(ca_cert_to_distribute);
		pemWriterCert.flush();
		pemWriterCert.close();

		FileWriter fpriv = new FileWriter("cert/ca_private_key.pem");
		JcaPEMWriter pemWriterPriv = new JcaPEMWriter(fpriv);
		pemWriterPriv.writeObject(keyPair.getPrivate());
		pemWriterPriv.flush();
		pemWriterPriv.close();
		
		Misc.create("cert/ca.md5", StringFx.md5(Misc.build_CA_DN()));

		return "1";
	}
	
	public static String generate_cu() throws Exception {
		
		//Generate the cert directory if not exist
		if (!(new File("cert")).exists()) {
			(new File("cert")).mkdir();
		}

		//Get the private key
		String privKeyPEM = Misc.load("cert/ca_private_key.pem").replace("-----BEGIN RSA PRIVATE KEY-----\n", "").replace("-----END RSA PRIVATE KEY-----\n", "");
		byte [] encoded = Base64.decode(privKeyPEM);
        // PKCS8 decode the encoded RSA private key
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey ca_pk = kf.generatePrivate(keySpec);
 
		// Loading certificate file  
        String certFile = "cert/ca_cert.pem";
        InputStream inStream = new FileInputStream(certFile);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate ca_cert =(X509Certificate)cf.generateCertificate(inStream);

		//Generate the public/private rsa key pair
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
		keyPairGenerator.initialize(Start.CERT_KEY_SIZE);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		
		//Get the CA DN
		String CADN=build_CA_DN();
		
		//Get the DN
		String DN=build_CU_DN();
		
		JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

		//Create the certificate builder
		X509v3CertificateBuilder certBldr = new JcaX509v3CertificateBuilder(
				new X500Name(CADN),
				BigInteger.valueOf(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis() + 1000 * 3600 * 24 * Start.CU_DAY_LIMIT),
				new X500Name(DN), keyPair.getPublic()).addExtension(
						new ASN1ObjectIdentifier("2.5.29.14"), 
						false, 
						extUtils.createSubjectKeyIdentifier(keyPair.getPublic())).addExtension(
								new ASN1ObjectIdentifier("2.5.29.35"), 
								false, extUtils.createAuthorityKeyIdentifier(ca_cert.getPublicKey()));
		
		//For server, client and email
		ASN1EncodableVector keyPurposeVector = new ASN1EncodableVector();
		keyPurposeVector.add(KeyPurposeId.id_kp_serverAuth);
		keyPurposeVector.add(KeyPurposeId.id_kp_clientAuth);
		keyPurposeVector.add(KeyPurposeId.id_kp_emailProtection);
		certBldr.addExtension(new ASN1ObjectIdentifier("2.5.29.37"), true, new DERSequence(keyPurposeVector));
		
		//Add all alt host names
		if (!Misc.lrtrim(Start.CU_ALT_HOSTNAME).equals("")) {
			ASN1EncodableVector alftHostNamesVector = new ASN1EncodableVector();
			
			for (int i=1;i<=Misc.size(Start.CU_ALT_HOSTNAME, ",");i++) {
				alftHostNamesVector.add( new GeneralName( GeneralName.dNSName, Misc.lrtrim(Misc.atom(Start.CU_ALT_HOSTNAME, i, ","))) );
			}
			
			certBldr.addExtension(new ASN1ObjectIdentifier("2.5.29.17"), false, new DERSequence(alftHostNamesVector));
		}

		//Get the signature
		ContentSigner signer = new JcaContentSignerBuilder("SHA1WithRSAEncryption").setProvider("BC").build(ca_pk);

		//Get the a certificate signed be the private key
		X509Certificate cu_cert_to_distribute = new JcaX509CertificateConverter().setProvider("BC")
				.getCertificate(certBldr.build(signer));

		cu_cert_to_distribute.verify(ca_cert.getPublicKey());

		FileWriter fcert = new FileWriter("cert/cu_cert.pem");
		JcaPEMWriter pemWriterCert = new JcaPEMWriter(fcert);
		pemWriterCert.writeObject(cu_cert_to_distribute);
		pemWriterCert.flush();
		pemWriterCert.close();

		FileWriter fpriv = new FileWriter("cert/cu_priv.pem");
		JcaPEMWriter pemWriterPriv = new JcaPEMWriter(fpriv);
		pemWriterPriv.writeObject(keyPair.getPrivate());
		pemWriterPriv.flush();
		pemWriterPriv.close();

		KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
		
		ks.load(null, null);

		char[] password = Start.CU_KEYSTORE_PASSWORD.toCharArray();
		ks.load(null, password);

		Certificate[] chain = new Certificate[] {cu_cert_to_distribute}; 
		ks.setKeyEntry("cu_public_key", keyPair.getPrivate(), password, chain); 

		//Store away the keystore.
		FileOutputStream fos = new FileOutputStream("cert/cu.p12");
		ks.store(fos, password);
		fos.close();
		
		Misc.create("cert/cu.md5", StringFx.md5(Misc.build_CU_DN()));

		return "1";
	}
	
	public static String rpad_smallint(String str) {
		
		return str + ("     ".substring(str.length()));
		
	}
	
	public static String rpad_int(String str) {
		
		return str + ("          ".substring(str.length()));
		
	}
	
	public static String rpad_long(String str) {
		
		return str + ("                   ".substring(str.length()));
		
	}
	
	public static String deleteFile(String fileOrDirectoryPath) throws Exception {

		//Initialization
		String b = "1";
		File fileOrdir = new File(fileOrDirectoryPath);
		String[] dirList;
		String subFileOrDir = "";

		//Check if directory
		if (!fileOrdir.isDirectory()) {

			//It's a file, delete it
			fileOrdir.delete();

		} else {

			//It's a directory

			//Get all files or directories in the current directory
			dirList=fileOrdir.list();

			//Parse all files or directories
			for (int i=0; i<dirList.length; i++) {

				//Get the sub file or directory
				subFileOrDir = dirList[i];

				//Delete the sub file or directory
				if (b.equals("1")) {

					b = deleteFile(fileOrDirectoryPath+File.separator+subFileOrDir);

				}
			}

			//Delete the current directory
			fileOrdir.delete();
		}

		return b;

	}
	
	//Get MD5 of a string
	public static String md5(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		
		byte[] bytesOfPassword = str.getBytes("UTF-8");

		MessageDigest md = MessageDigest.getInstance("MD5");
		return new String(md.digest(bytesOfPassword), "UTF-8");
		
	}
	
	//Get all objects in string tab format
	public static String[] JSONArray2StringTab(JSONArray in) throws Exception {
		
		//Initialization
		String[] result = new String[in.size()];
		
		//Parse all objects
		for(int i=0;i<in.size();i++) {
			
			//Get the current object
			result[i] = in.get(i)+"";
			
		}
		
		return result;
		
	}
	
	//Diff date
	public static String dateDiffOneDay(String date) {
		
		//Initialization
		Calendar cal = Calendar.getInstance();    
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(8, 10)));
		cal.set(Calendar.MONTH, Integer.parseInt(date.substring(5, 7))-1);
		cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		cal.add(Calendar.DAY_OF_MONTH, -1);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		return sdf.format(cal.getTime());
		
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	//Load a JSON string
	public static JSONArray loadArray(String json) {
		
		return (JSONArray) JSONValue.parse(json);
		
	}
	
	public static int size(String atomList, String separator) {

		//Prepare list
		String copyAtomList=atomList;
		Pattern motif = Pattern.compile("["+separator+"]");

		//Split the list
		String[] ch = motif.split(copyAtomList, -1);

		//Return the size
		return ch.length;

	}
	
	//Vector message to string
	public static String vectorToStringMsg(Vector<MQLValue> inMsg) {
		
		//Initialization
		String result = "";
		
		//Parse all string
		for(int i=0;i<inMsg.size();i++) {
			
			if (inMsg.elementAt(i).value!=null) {
			
				//Concat all string
				if (inMsg.elementAt(i).value.indexOf(" ")>-1) result += " \""+inMsg.elementAt(i).value.replace("\"", "\\\"")+"\"";
				else result += " "+inMsg.elementAt(i).value;
				
			}
			
		}
		
		//Delete the first char
		if (!result.equals("")) {
			
			result = result.substring(1);
			
		}
		
		return result;
		
	}
	
	//Split without empty
	public static Vector<Vector<MQLValue>> splitRestrictedCommand(String str) throws Exception {
		
		//Initialization
		int line = 1;

		StringBuilder tmpstr = new StringBuilder(str);
		int nb__tmpstr = tmpstr.length();
		int i__tmpstr = 0;
		
		Vector<Vector<MQLValue>> commands = new Vector<Vector<MQLValue>>();
		commands.add(new Vector<MQLValue>());
		int endIndex = 1;
		int depth = 0;
		StringBuilder tmpCmd = new StringBuilder("");

		//Parse the command string
		while (i__tmpstr<nb__tmpstr) {
			
			switch (tmpstr.charAt(i__tmpstr)) {
			case ';':
				
				if (depth==0) {
					commands.get(commands.size()-1).add(new MQLValue(";", 0, line));
					commands.add(new Vector<MQLValue>());
				} else {
					tmpCmd.append(";");
				}
				i__tmpstr++;
				
				break;
				
			case '\n':
				
				line++;

				if (depth!=0) {
					tmpCmd.append("\n");
				}
				i__tmpstr++;
				
				break;
				
			case ' ':
				
				if (depth!=0) {
					tmpCmd.append(" ");
				}
				i__tmpstr++;
				
				break;
				
			case '\t':
				
				if (depth!=0) {
					tmpCmd.append("\t");
				}
				i__tmpstr++;
				
				break;
				
			case '{':
				
				throw new Exception("Sorry, you are in a restricted MQL session. You can only use 'execute|include|call|stack' commands with basic parameters 'number|string|null|boolean' ...");
				
			case '(':
				
				throw new Exception("Sorry, you are in a restricted MQL session. You can only use 'execute|include|call|stack' commands with basic parameters 'number|string|null|boolean' ...");
				
			case '}':
				
				throw new Exception("Sorry, you are in a restricted MQL session. You can only use 'execute|include|call|stack' commands with basic parameters 'number|string|null|boolean' ...");
				
			case ')':
				
				throw new Exception("Sorry, you are in a restricted MQL session. You can only use 'execute|include|call|stack' commands with basic parameters 'number|string|null|boolean' ...");
				
			case '\"':
				
				//It is a string
				endIndex = tmpstr.indexOf("\"", i__tmpstr+1);
				while (endIndex!=-1 && tmpstr.substring(endIndex-1, endIndex).equals("\\")) {
					endIndex = tmpstr.indexOf("\"", endIndex+1);
				}
				
				//Generate an error if the string is never close
				if (endIndex==-1) {
					
					throw new Exception("Sorry, Invalid command (string must be close)");
					
				} else {
					
					//The string is close
					String tmptmpstr = tmpstr.substring(i__tmpstr+1, endIndex);
					
					if (depth>0) {
						
						tmpCmd.append("\""+tmptmpstr+"\"");
						
					} else {
						
						commands.get(commands.size()-1).add(new MQLValue(tmptmpstr.replace("\\\"", "\""), 0, line));
					
					}
					line += StringFx.count_int(tmptmpstr.toString(), "\n");
					
					i__tmpstr=endIndex+1;
					
				}
				
				break;
				
			default: 
				
				//It is not a string
				//Find the next
				
				endIndex = -1;
				for(int index = i__tmpstr;index<nb__tmpstr;index++) {
					char c = tmpstr.charAt(index);
					if (search_str.indexOf(c)>-1) {
						endIndex = index;
						break;
					}
				}
						
				//Go to the next key
				if (endIndex==-1) {
					
					String tmptmpstr = tmpstr.substring(i__tmpstr, tmpstr.length());

					if (!tmptmpstr.equals("")) {
						
						if (depth>0) {
							tmpCmd.append(tmptmpstr);
						} else {
							
							if (tmptmpstr.startsWith("[") && tmptmpstr.endsWith("]")) {
								commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, 1, line));
							} else {
								if (tmptmpstr.equals("false") || tmptmpstr.equals("true") || tmptmpstr.equals("null")) commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, 3, line));
								else commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, 0, line));
							}
							
						}
						
					}
					i__tmpstr = nb__tmpstr;
					
				} else {

					String tmptmpstr = tmpstr.substring(i__tmpstr, endIndex);
					
					if (!tmptmpstr.equals("")) {
						
						if (depth>0) {
							tmpCmd.append(tmptmpstr);
						} else {
							
							if (tmptmpstr.startsWith("[") && tmptmpstr.endsWith("]")) {
								commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, 1, line));
							} else {
								if (tmptmpstr.equals("false") || tmptmpstr.equals("true") || tmptmpstr.equals("null")) commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, 3, line));
								else commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, 0, line));
							}
							
						}
						
					}
					i__tmpstr=endIndex;
					
				}
				
			}
			
		}

		if (depth!=0) {
			throw new Exception("Sorry, Invalid command ('{' or '}' or '(' or ')' must be close)");
		}
		
		//Remove the last if empty
		int nbDeleted = 0, nbTab = commands.size();
		for(int i=0;i<nbTab;i++) {
			if (commands.get(i-nbDeleted).size()==0) {
				commands.remove(i-nbDeleted);
				nbDeleted++;
			} else {
				String first = commands.get(i-nbDeleted).get(0).value;
				
				switch (first) {
				case ";":
				case "1":
				case "mentdb":
				case "refresh":
				case "login":
				case "exit":
				case "execute":
				case "include":
				case "call":
				case "sid":
				case "concat":
				case "name":
				case "restricted":
				case "Connected.":
				case "stack_execute":
					break;
				default :
					throw new Exception("Sorry, you are in a restricted MQL session. You can only use 'execute|include|call|stack' commands with basic parameters 'number|string|null|boolean' ...");
				}
			}
		}
		
		//Return all command lines
		return commands;
		
	}
	
	//Split without empty
	public static Vector<Vector<MQLValue>> splitCommand(String str) throws Exception {
		
		//Initialization
		int line = 1;

		StringBuilder tmpstr = new StringBuilder(str);
		int nb__tmpstr = tmpstr.length();
		int i__tmpstr = 0;
		
		Vector<Vector<MQLValue>> commands = new Vector<Vector<MQLValue>>();
		commands.add(new Vector<MQLValue>());
		int endIndex = 1;
		int depth = 0;
		StringBuilder tmpCmd = new StringBuilder("");

		//Parse the command string
		while (i__tmpstr<nb__tmpstr) {
			
			switch (tmpstr.charAt(i__tmpstr)) {
			case ';':
				
				if (depth==0) {
					commands.get(commands.size()-1).add(new MQLValue(";", 0, line));
					commands.add(new Vector<MQLValue>());
				} else {
					tmpCmd.append(";");
				}
				i__tmpstr++;
				
				break;
				
			case '\n':
				
				line++;

				if (depth!=0) {
					tmpCmd.append("\n");
				}
				i__tmpstr++;
				
				break;
				
			case ' ':
				
				if (depth!=0) {
					tmpCmd.append(" ");
				}
				i__tmpstr++;
				
				break;
				
			case '\t':
				
				if (depth!=0) {
					tmpCmd.append("\t");
				}
				i__tmpstr++;
				
				break;
				
			case '{':
				
				if (depth>0) {
					tmpCmd.append("{");
				}
				depth += 1;
				i__tmpstr++;
				
				break;
				
			case '(':
				
				if (depth>0) {
					tmpCmd.append("(");
				}
				depth += 1;
				i__tmpstr++;
				
				break;
				
			case '}':
				
				depth -= 1;
				
				if (depth==0) {
					
					if (!Misc.lrtrim(tmpCmd.toString()).endsWith(";")) {
						tmpCmd.append(";");
					}
					
					int nbSize = commands.size()-1;

					if (commands.get(nbSize).size()>0) {
						
						switch (commands.get(nbSize).get(0).value) {
						case "and": case "or": case "if": case "for": case "while": case "repeat": case "synchronized": case "exception": case "try": case "parallel": case "switch": case "mode": case "case": 
						
							commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							
							break;
							
						case "mql": 
							
							if (commands.get(nbSize).size()==1) {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}
							
							break;
							
						case "sql":
							
							if (commands.get(nbSize).get(1).value.equals("parse")) {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}
							
							break;
							
						case "csv":
							
							if (commands.get(nbSize).get(1).value.equals("parse")) {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}
							
							break;
							
						case "html":
							
							if (commands.get(nbSize).get(1).value.equals("parse")) {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}
							
							break;
							
						case "json":
							
							if (commands.get(nbSize).get(1).value.equals("parse_obj") || commands.get(nbSize).get(1).value.equals("parse_array")) {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}
							
							break;
							
						case "script":
							
							if (commands.get(nbSize).get(1).value.equals("add") 
									|| commands.get(nbSize).get(1).value.equals("create")
									|| commands.get(nbSize).get(1).value.equals("insert")
									|| commands.get(nbSize).get(1).value.equals("update")
									|| commands.get(nbSize).get(1).value.equals("merge")) {
								
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
								
							} else if (commands.get(nbSize).get(1).value.equals("set")
											&& (commands.get(nbSize).get(2).value.equals("delay"))) {
								
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
								
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}
							
							break;
							
						case "relation":
							
							if (commands.get(nbSize).get(1).value.equals("condition") && 
									commands.get(nbSize).get(2).value.equals("set")) {
								
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
								
							} else if (commands.get(nbSize).get(1).value.equals("action") && 
									(commands.get(nbSize).get(2).value.equals("add")
										|| commands.get(nbSize).get(2).value.equals("update"))) {
								
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
								
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}
							
							break;
							
						case "data":
							
							if (commands.get(nbSize).get(1).value.equals("type")
									&& commands.get(nbSize).get(2).value.equals("create")) {
								
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
								
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}
							
							break;
							
						default: 
						
							commands.get(commands.size()-1).add(new MQLValue(tmpCmd.toString(), 2, line));
							
						}
						
					} else commands.get(commands.size()-1).add(new MQLValue(tmpCmd.toString(), 2, line));
					
					tmpCmd = new StringBuilder("");
					
				} else {
					tmpCmd.append("}");
				}
				i__tmpstr++;
				
				break;
				
			case ')':
				
				depth -= 1;
				
				if (depth==0) {
					
					if (!Misc.lrtrim(tmpCmd.toString()).endsWith(";")) {
						tmpCmd.append(";");
					}

					int nbSize = commands.size()-1;
					
					if (commands.get(nbSize).size()>0) {
						
						switch (commands.get(nbSize).get(0).value) {
						case "and": case "or": case "if": case "for": case "while": case "repeat": case "synchronized": case "exception": case "try": case "parallel": case "switch": case "mode": case "case": 
							
							commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							
							break;
							
						case "mql": 
							
							if (commands.get(nbSize).size()==1) {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}
							
							break;
							
						case "sql":
							
							if (commands.get(nbSize).get(1).value.equals("parse")) {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}
							
							break;
							
						case "csv":
							
							if (commands.get(nbSize).get(1).value.equals("parse")) {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}
							
							break;
							
						case "html":
							
							if (commands.get(nbSize).get(1).value.equals("parse")) {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}
							
							break;
							
						case "json":
							
							if (commands.get(nbSize).get(1).value.equals("parse_obj") || commands.get(nbSize).get(1).value.equals("parse_array")) {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}
							
							break;
							
						case "script":
							
							if (commands.get(nbSize).get(1).value.equals("add") 
									|| commands.get(nbSize).get(1).value.equals("create")
									|| commands.get(nbSize).get(1).value.equals("insert")
									|| commands.get(nbSize).get(1).value.equals("update")
									|| commands.get(nbSize).get(1).value.equals("merge")) {
								
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
								
							} else if (commands.get(nbSize).get(1).value.equals("set")
											&& (commands.get(nbSize).get(2).value.equals("delay"))) {
								
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
								
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}
							
							break;
							
						case "relation":
							
							if (commands.get(nbSize).get(1).value.equals("condition") && 
									commands.get(nbSize).get(2).value.equals("set")) {
								
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
								
							} else if (commands.get(nbSize).get(1).value.equals("action") && 
									(commands.get(nbSize).get(2).value.equals("add")
										|| commands.get(nbSize).get(2).value.equals("update"))) {
								
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
								
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}
							
							break;
							
						case "data":
							
							if (commands.get(nbSize).get(1).value.equals("type")
									&& commands.get(nbSize).get(2).value.equals("create")) {
								
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
								
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}
							
							break;
							
						default: 
						
							commands.get(commands.size()-1).add(new MQLValue(tmpCmd.toString(), 2, line));
							
						}
						
					} else commands.get(commands.size()-1).add(new MQLValue(tmpCmd.toString(), 2, line));
					
					tmpCmd = new StringBuilder("");
					
				} else {
					tmpCmd.append(")");
				}
				i__tmpstr++;
				
				break;
				
			case '\"':
				
				//It is a string
				endIndex = tmpstr.indexOf("\"", i__tmpstr+1);
				while (endIndex!=-1 && tmpstr.substring(endIndex-1, endIndex).equals("\\")) {
					endIndex = tmpstr.indexOf("\"", endIndex+1);
				}
				
				//Generate an error if the string is never close
				if (endIndex==-1) {
					
					throw new Exception("Sorry, Invalid command (string must be close)");
					
				} else {
					
					//The string is close
					String tmptmpstr = tmpstr.substring(i__tmpstr+1, endIndex);
					
					if (depth>0) {
						
						tmpCmd.append("\""+tmptmpstr+"\"");
						
					} else {
						
						commands.get(commands.size()-1).add(new MQLValue(tmptmpstr.replace("\\\"", "\""), 0, line));
					
					}
					line += StringFx.count_int(tmptmpstr.toString(), "\n");
					
					i__tmpstr=endIndex+1;
					
				}
				
				break;
				
			default: 
				
				//It is not a string
				//Find the next
				
				endIndex = -1;
				for(int index = i__tmpstr;index<nb__tmpstr;index++) {
					char c = tmpstr.charAt(index);
					if (search_str.indexOf(c)>-1) {
						endIndex = index;
						break;
					}
				}
						
				//Go to the next key
				if (endIndex==-1) {
					
					String tmptmpstr = tmpstr.substring(i__tmpstr, tmpstr.length());

					if (!tmptmpstr.equals("")) {
						
						if (depth>0) {
							tmpCmd.append(tmptmpstr);
						} else {
							
							if (tmptmpstr.startsWith("@[") && tmptmpstr.endsWith("]")) {
								commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, -1, line));
							} else if (tmptmpstr.startsWith("[") && tmptmpstr.endsWith("]")) {
								commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, 1, line));
							} else {
								if (tmptmpstr.equals("false") || tmptmpstr.equals("true") || tmptmpstr.equals("null")) commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, 3, line));
								else commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, 0, line));
							}
							
						}
						
					}
					i__tmpstr = nb__tmpstr;
					
				} else {

					String tmptmpstr = tmpstr.substring(i__tmpstr, endIndex);
					
					if (!tmptmpstr.equals("")) {
						
						if (depth>0) {
							tmpCmd.append(tmptmpstr);
						} else {
							
							if (tmptmpstr.startsWith("@[") && tmptmpstr.endsWith("]")) {
								commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, -1, line));
							} else if (tmptmpstr.startsWith("[") && tmptmpstr.endsWith("]")) {
								commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, 1, line));
							} else {
								if (tmptmpstr.equals("false") || tmptmpstr.equals("true") || tmptmpstr.equals("null")) commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, 3, line));
								else commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, 0, line));
							}
							
						}
						
					}
					i__tmpstr=endIndex;
					
				}
				
			}
			
		}

		if (depth!=0) {
			throw new Exception("Sorry, Invalid command ('{' or '}' or '(' or ')' must be close)");
		}
		
		//Remove the last if empty
		int nbDeleted = 0, nbTab = commands.size();
		for(int i=0;i<nbTab;i++) {
			if (commands.get(i-nbDeleted).size()==0) {
				commands.remove(i-nbDeleted);
				nbDeleted++;
			}
		}
		
		//Return all command lines
		return commands;
		
	}
	
	
	//Split without empty
	public static String splitCommandHtml(String str) throws Exception {
		
		//Initialization
		StringBuilder html = new StringBuilder("");

		StringBuilder tmpstr = new StringBuilder(str);
		int nb__tmpstr = tmpstr.length();
		int i__tmpstr = 0;
		
		int endIndex = 1;
		boolean diese = false;
		
		//Parse the command string
		while (i__tmpstr<nb__tmpstr) {
			
			switch (tmpstr.charAt(i__tmpstr)) {
			case ';': case '\n': case ' ': case '\t': case '{': case '}': case '(': case ')':
				
				if (diese) html.append("<span style='color:#bbb'>"+tmpstr.charAt(i__tmpstr)+"</span>");
				else html.append(tmpstr.charAt(i__tmpstr));
				
				if (tmpstr.charAt(i__tmpstr)==';') {
					
					diese = false;
					
				}
				
				i__tmpstr++;
				
				break;
			case '\"':
				
				//It is a string
				endIndex = tmpstr.indexOf("\"", i__tmpstr+1);
				while (endIndex!=-1 && tmpstr.substring(endIndex-1, endIndex).equals("\\")) {
					endIndex = tmpstr.indexOf("\"", endIndex+1);
				}
				
				//Generate an error if the string is never close
				if (endIndex==-1) {
					
					//The string is close
					String tmptmpstr = tmpstr.substring(i__tmpstr+1, tmpstr.length());
					
					html.append("<span style='color:#007700'>\""+tmptmpstr.replace("<", "&lt;")+"\"</span>");
					
					i__tmpstr = nb__tmpstr;
					
				} else {
					
					//The string is close
					String tmptmpstr = tmpstr.substring(i__tmpstr+1, endIndex);
					
					html.append("<span style='color:#007700'>\""+tmptmpstr.replace("<", "&lt;")+"\"</span>");
					
					i__tmpstr=endIndex+1;
					
				}
				
				break;
				
			default: 
				
				//It is not a string
				//Find the next
				
				endIndex = -1;
				for(int index = i__tmpstr;index<nb__tmpstr;index++) {
					char c = tmpstr.charAt(index);
					if (search_str.indexOf(c)>-1) {
						endIndex = index;
						break;
					}
				}
						
				//Go to the next key
				if (endIndex==-1) {
					
					String tmptmpstr = tmpstr.substring(i__tmpstr, tmpstr.length()).replace("<", "&lt;").replace(">", "&gt;");
					
					if (tmptmpstr.startsWith("#") || diese) {
						html.append("<span style='color:#bbb'>"+tmptmpstr.replace("<", "&lt;")+"</span>");
						diese = true;
					} else if (tmptmpstr.equals("break") || tmptmpstr.equals("continue") || tmptmpstr.equals("synchronized") || tmptmpstr.equals("while") || tmptmpstr.equals("repeat") || tmptmpstr.equals("for") || tmptmpstr.equals("exception")
							 || tmptmpstr.equals("if") || tmptmpstr.equals("try") || tmptmpstr.equals("switch") || tmptmpstr.equals("mode") || tmptmpstr.equals("case")
							 || tmptmpstr.equals("parallel")) {
						html.append("<span style='color:#9C27B0;font-weight:bold'>"+tmptmpstr.replace("<", "&lt;")+"</span>");
					} else if (tmptmpstr.startsWith("[") && tmptmpstr.endsWith("]")) {
						html.append("<span style='color:#ba1c00'>"+tmptmpstr.replace("<", "&lt;")+"</span>");
					} else if (tmptmpstr.equals("false") || tmptmpstr.equals("false")|| tmptmpstr.equals("null")) {
						html.append("<span style='color:#003cc8'>"+tmptmpstr.replace("<", "&lt;")+"</span>");
					} else if (TypeFx.is_number(tmptmpstr).equals("1")) {
						html.append("<span style='color:#333'>"+tmptmpstr.replace("<", "&lt;")+"</span>");
					} else if (StringFx.is_letter_(tmptmpstr).equals("1")) {
						html.append("<span style='color:#003cc8'>"+tmptmpstr.replace("<", "&lt;")+"</span>");
					} else {
						html.append(tmptmpstr.replace("<", "&lt;"));
					};
					
					i__tmpstr = nb__tmpstr;
					
				} else {

					String tmptmpstr = tmpstr.substring(i__tmpstr, endIndex).replace("<", "&lt;").replace(">", "&gt;");
					
					if (tmptmpstr.startsWith("#") || diese) {
						html.append("<span style='color:#bbb'>"+tmptmpstr.replace("<", "&lt;")+"</span>");
						diese = true;
					} else if (tmptmpstr.equals("break") || tmptmpstr.equals("continue") || tmptmpstr.equals("synchronized") || tmptmpstr.equals("while") || tmptmpstr.equals("repeat") || tmptmpstr.equals("for") || tmptmpstr.equals("exception")
							 || tmptmpstr.equals("if") || tmptmpstr.equals("try") || tmptmpstr.equals("switch") || tmptmpstr.equals("mode") || tmptmpstr.equals("case")
							 || tmptmpstr.equals("parallel")) {
						html.append("<span style='color:#9C27B0;font-weight:bold'>"+tmptmpstr.replace("<", "&lt;")+"</span>");
					} else if (tmptmpstr.startsWith("[") && tmptmpstr.endsWith("]")) {
						html.append("<span style='color:#ba1c00'>"+tmptmpstr.replace("<", "&lt;")+"</span>");
					} else if (tmptmpstr.equals("false") || tmptmpstr.equals("false")|| tmptmpstr.equals("null")) {
						html.append("<span style='color:#003cc8'>"+tmptmpstr.replace("<", "&lt;")+"</span>");
					} else if (TypeFx.is_number(tmptmpstr).equals("1")) {
						html.append("<span style='color:#333'>"+tmptmpstr.replace("<", "&lt;")+"</span>");
					} else if (StringFx.is_letter_(tmptmpstr).equals("1")) {
						html.append("<span style='color:#003cc8'>"+tmptmpstr.replace("<", "&lt;")+"</span>");
					} else {
						html.append(tmptmpstr.replace("<", "&lt;"));
					};
					
					i__tmpstr=endIndex;
					
				}
				
			}
			
		}
		
		//Return all command lines
		return html.toString();
		
	}
	
	public static String search_str = " ;\t\n() {}";
	
	//Split words
	public static String[] splitWords(String words, String separator) {
		
		return words.split(Pattern.quote(separator));
		
	}
	
	public static String appendFile(String filePath, String str) throws Exception {

		//Initialization
		FileWriter fw = null;

		//Initialization
		File file = new File(filePath);

		try {

			//Create the new file
			file.createNewFile();
			fw = new FileWriter(file, true);

			//Write str
			fw.write(str);

			//Flush the file
			fw.flush();

			//Return true
			return "1";

		} catch (Exception e) {

			//Generate an error
			throw e;

		} finally {

			//Close the writer
			try {fw.close();} catch (Exception e) {}

		}

	}
	
	//Read an ini file
	public static String conf_value(String path, String section, String field) {

		//Initialization
		String line="", value="";
		int found = 0;
		BufferedReader in =null;

		//Try to get the value
		try {

			//In stream
			in =  new BufferedReader( new FileReader(path));

			//parse to find the section
			while ((line=in.readLine())!=null)
			{
				
				if (!line.equals("") && line.equals("[" + section + "]"))
				{
					//The section found
					break;
				}
				
			}

			//parse to find the field
			while (((line=in.readLine())!=null))
			{
				if (!line.equals("") && !(line.substring(0,1).equals("["))) {
					if (atom(line, 1, "=").equals(field))
					{
						//The field found
						found=1;
						break;
					}
					
				}
			}
			
			//Manage the last line
			if (found==0 && line!=null &&  !line.equals("")) {
				if (atom(line, 1, "=").equals(field))	{
					found=1;
				}
			}
			
			//Get the value of found
			if (found==1) value=line.substring(field.length()+1).replace("\r\n", "").replace("\n", "").replace("\r", "");

			//return the value
			return value;

		} catch (Exception e) {

			return null;

		} finally {

			//Close the stream
			try {in.close();} catch (Exception e) {};

		}

	}
	
	//Basic rpad function
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
	
	static String current_system_out_print = "";
	
	//Add space rpad
	public static void system_out_print(String str, boolean ln, String color, String strAdd) {
		
		str += strAdd;
		
		if (ln) {
			
			System.out.println(color+Misc.rpad(current_system_out_print+str, " ", "65").substring((current_system_out_print+str).length())+str.substring(0, str.length()-strAdd.length()));
			
		} else {
			current_system_out_print = str;
			System.out.print(color+str);
		}

	}
	
	//Get the os
	public static String os() {
		
		String OS = System.getProperty("os.name").toLowerCase();
		
		if (OS.indexOf("win") >= 0) {
			
			return "win";
			
		} else if (OS.indexOf("mac") >= 0) {
			
			return "mac";
			
		} else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 ) {
			
			return "unix";
			
		} else return "unknow";
		
	}
	
	//Get a specific string in a list
	public static String atom(String atomList, int index, String separator) {
		
		//Prepare the list
		String copyAtomList=atomList;
		Pattern motif = Pattern.compile("["+separator+"]");
		
		//Split the list
		String[] ch = motif.split(copyAtomList, -1);
		
		//Return the atom
		return ch[index-1];
		
	}
	
	//Get a specific string in a list
	public static Pattern motif = Pattern.compile("[;]");
	public static String[] atom_tab(String atomList) {
		
		//Prepare the list
		String copyAtomList=atomList;
		
		//Return the list
		return motif.split(copyAtomList, -1);
		
	}
	
	//Basic lrtrim function
	public static String lrtrim(String str) {
		
		return str.replaceAll("\\s+$", "").replaceAll("^\\s+", "");

	}
	
	//Basic trim function
	public static String trim(String str) {
		
		return str.replaceAll("\\s+$", "").replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");

	}
	
	public static String load(String filePath) throws Exception
	{

		//Initialization
		String line="";
		BufferedReader in = null;
		StringBuilder data = new StringBuilder();

		//Try to get data
		try {

			//Load the stream
			in =  new BufferedReader( new InputStreamReader( new FileInputStream(filePath)));

			//Parse the stream
			while ((line=in.readLine())!=null)
			{
				//Get line by line
				data.append(line);
				data.append("\n");
			}

		} catch (Exception e) {

			//Generate an error
			throw e;

		} finally {

			//Close input stream
			try {in.close();} catch (Exception e) {};

		}

		//Return the data
		return data.toString();
	}
	
	public static String create(String filePath, String str) throws Exception {

		//Initialization
		FileWriter fw = null;

		//Initialization
		File file = new File(filePath);

		try {

			//Create the new file
			file.createNewFile();
			fw = new FileWriter(file);

			//Write str
			fw.write(str);

			//Flush the file
			fw.flush();

			//Return true
			return "1";

		} catch (Exception e) {

			//Generate an error
			throw e;

		} finally {

			//Close the writer
			try {fw.close();} catch (Exception e) {}

		}

	}
	
	public static void append2(String filePath, String str, boolean line) {

		//Initialization
		FileWriter fw = null;

		//Initialization
		File file = new File(filePath);

		try {

			//Create the new file
			if (!file.exists()) file.createNewFile();
			
			fw = new FileWriter(file, true);

			//Write str
			fw.write((line?"____________________________________________________________\n":"") + str);

			//Flush the file
			fw.flush();

		} catch (Exception e) {

			System.out.println("Misc.append="+e.getMessage());

		} finally {

			//Close the writer
			try {fw.close();} catch (Exception e) {}

		}

	}

}