package re.jpayet.mentdb.ext.fx;

import java.math.*;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

import org.json.simple.JSONObject;

import java.security.interfaces.*;


/** 
 * Cette classe propose des méthodes permettant de crypter et décrypter des 
 * messages avec l'algorithme RSA. Le message doit cependant être plus petit
 * que KEY_SIZE.
 */
public class MyRSA {
	
	public int KEY_SIZE = 1024;// [512..2048]
	
	private RSAPublicKey publicKey;
	private RSAPrivateKey privateKey;
	
	
	public MyRSA(int KEY_SIZE) {
		
		this.KEY_SIZE = KEY_SIZE;
		
	}
	
	 
	public RSAPublicKey getPublicKey() {
		return publicKey;
	}
	
	
	public byte[] getPublicKeyInBytes() {
		return publicKey.getEncoded();
	}
	
	
	public RSAPrivateKey getPrivateKey() {
		return privateKey;
	}
	
	
	public byte[] getPrivateKeyInBytes() {
		return privateKey.getEncoded();
	}
	
	
	public void setPublicKey(RSAPublicKey publicKey) {
		this.publicKey = publicKey;
	}
	
	
	public void setPublicKey(byte[] publicKeyData) {
		try {
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyData);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			publicKey = (RSAPublicKey)keyFactory.generatePublic(publicKeySpec);
		}
		catch (Exception e) {System.out.println(e);} 
	}
	
	
	public void setPrivateKey(RSAPrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	
	
	public void setPrivateKey(byte[] privateKeyData) {
		try {
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyData);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			privateKey = (RSAPrivateKey)keyFactory.generatePrivate(privateKeySpec);
		}
		catch (Exception e) {System.out.println(e);} 
	}
	
	
	public void generateKeyPair() {
		try {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(KEY_SIZE, new SecureRandom());
		KeyPair kp = keyPairGen.generateKeyPair();
		publicKey = (RSAPublicKey)kp.getPublic();
		privateKey = (RSAPrivateKey)kp.getPrivate();
		}
		catch (Exception e) {System.out.println(e);} 
	}
	
	
	public byte[] crypt(byte[] plaintext) {
		return crypt(new BigInteger(addOneByte(plaintext))).toByteArray();
	}
	
	
	public byte[] crypt(String plaintext) {
		return crypt(plaintext.getBytes());
	}
	 
	 
	public byte[] decryptInBytes(byte[] ciphertext) {
		return removeOneByte(decrypt(new BigInteger(ciphertext)).toByteArray());
	}
	
	
	public String decryptInString(byte[] ciphertext) {
	return new String(decryptInBytes(ciphertext));
	}
	
	public static String encode(String data, String publicKey, String privateKey) {
		
		MyRSA rsa = new MyRSA(2048);
		rsa.setPublicKey(Base64.getDecoder().decode(publicKey));
		rsa.setPrivateKey(Base64.getDecoder().decode(privateKey));
		byte[] ciphertext = rsa.crypt(data);
		return Base64.getEncoder().encodeToString(ciphertext);
		
	}
	
	public static String decode(String data, String privateKey) {
		
		MyRSA rsa = new MyRSA(1024);
		rsa.setPrivateKey(Base64.getDecoder().decode(privateKey));
		
		return rsa.decryptInString(Base64.getDecoder().decode(data));
		
	}
		
	@SuppressWarnings("unchecked")
	public static JSONObject generate_key(String keysize) throws Exception {
		
		MyRSA rsa = new MyRSA(Integer.parseInt(keysize));
		rsa.generateKeyPair();
		byte[] publicKey = rsa.getPublicKeyInBytes();
		byte[] privateKey = rsa.getPrivateKeyInBytes();
		
		JSONObject result = new JSONObject();
		result.put("publicKey", Base64.getEncoder().encodeToString(publicKey));
		result.put("privateKey", Base64.getEncoder().encodeToString(privateKey));
		
		return result;
	
	}
	
	
	/**
	 * Cette méthode permet de tester le bon fonctionnement des autres.
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String plaintext = "teste de jim";
		System.out.println("plaintext = " + plaintext);
		MyRSA rsa = new MyRSA(1024);
		rsa.generateKeyPair();
		byte[] publicKey = rsa.getPublicKeyInBytes();
		byte[] privateKey = rsa.getPrivateKeyInBytes();
		byte[] ciphertext = rsa.crypt(plaintext); 
		System.out.println("ciphertext = " + new BigInteger(ciphertext));
		
		rsa.setPublicKey(publicKey);
		rsa.setPrivateKey(privateKey);
		String plaintext2 = rsa.decryptInString(ciphertext);
		System.out.println("plaintext2 = " + plaintext2);
		if (!plaintext2.equals(plaintext)) System.out.println("Error: plaintext2 != plaintext");
		
		JSONObject k = MyRSA.generate_key("2048");
		System.out.println("ZZZZZZZ = " + MyRSA.encode(plaintext, (String) k.get("publicKey"), (String) k.get("privateKey")));
		
		
	}
	
	
	private BigInteger crypt(BigInteger plaintext) {
		return plaintext.modPow(publicKey.getPublicExponent(), publicKey.getModulus());
	}
	
	
	private BigInteger decrypt(BigInteger ciphertext) {
		return ciphertext.modPow(privateKey.getPrivateExponent(), privateKey.getModulus());
	}
	
	
	/**
	 * Ajoute un byte de valeur 1 au début du message afin d'éviter que ce dernier
	 * ne corresponde pas à un nombre négatif lorsqu'il sera transformé en
	 * BigInteger.
	 */
	private static byte[] addOneByte(byte[] input) {
		byte[] result = new byte[input.length+1];
		result[0] = 1;
		for (int i = 0; i < input.length; i++) {
			result[i+1] = input[i];
		}
		return result;
	}
	
	
	/**
	 * Retire le byte ajouté par la méthode addOneByte.
	 */
	private static byte[] removeOneByte(byte[] input) {
		byte[] result = new byte[input.length-1];
		for (int i = 0; i < result.length; i++) {
			result[i] = input[i+1];
		}
		return result;
	}
}