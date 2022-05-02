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

package re.jpayet.mentdb.ext.compress;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import re.jpayet.mentdb.ext.fx.DateFx;

public class CompressManager {
	
	public static long incr = 0;

	public static void zip(String in, String out) throws Exception {

		if (!new File(in).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the source '"+in+"' does not exist.");

		}

		if (new File(out).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the destination '"+out+"' already exist.");

		}

		//Initialization
		ZipArchiveOutputStream arc = null;
		FileOutputStream fileWriter = null;

		try {

			fileWriter = new FileOutputStream(out);
			arc = new ZipArchiveOutputStream(fileWriter);

			zip_recursive("", in, arc);

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				arc.flush();
			} catch (Exception e) {}

			try {
				arc.finish();
			} catch (Exception e) {}

			try {
				arc.close();
			} catch (Exception e) {}

			try {
				fileWriter.close();
			} catch (Exception e) {}

		}

	}

	public static void zip_recursive(String originPath, String in, ZipArchiveOutputStream arc) throws Exception {

		File f = new File(in);
		String dir = "";

		if (!originPath.equals("")) {

			dir = originPath + "/";

		}

		if (f.isDirectory()) {

			//Add the directory
			ZipArchiveEntry entry = new ZipArchiveEntry(dir+f.getName() + "/");
			entry.setSize(0);
			arc.putArchiveEntry(entry);
			arc.closeArchiveEntry();

			//Add sub files or directories
			String[] sub = f.list();
			for(int i=0;i<sub.length;i++) {

				zip_recursive(dir+f.getName(), in+"/"+sub[i], arc);

			}

		} else {

			ZipArchiveEntry entry = new ZipArchiveEntry(dir+f.getName());
			Path path = Paths.get(in);
			byte[] bytes = Files.readAllBytes(path);
			entry.setSize(bytes.length);
			arc.putArchiveEntry(entry);
			arc.write(bytes);
			arc.closeArchiveEntry();

		}

	}

	public static String unzip(String in, String dir) throws Exception {

		if (!new File(in).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the source '"+in+"' does not exist.");

		}

		if (new File(dir).isFile()) {

			//Generate an exception
			throw new Exception("Sorry, the destination must be a directory.");

		}
		
		//Initialization
		ZipArchiveInputStream arc = null;
		FileInputStream fileReader = null;

		try {

			fileReader = new FileInputStream(in);
			arc = new ZipArchiveInputStream(fileReader);

			//Create the destination directory
			if (!new File(dir).exists()) {

				(new File(dir)).mkdir();

			}

			ZipArchiveEntry entry = arc.getNextZipEntry();
			while (entry != null) {

				File destPath = new File((new File(dir)), entry.getName());

				if (entry.getName().endsWith("/")) {

					//Create the directory
					destPath.mkdirs();

				} else {

					//Create the file
					destPath.createNewFile();

					byte [] btoRead = new byte[1024];

					//FileInputStream fin 
					BufferedOutputStream bout = 
							new BufferedOutputStream(new FileOutputStream(destPath));
					int len = 0;
					while((len = arc.read(btoRead)) != -1)
					{
						bout.write(btoRead,0,len);
					}

					//Close
					bout.close();
					btoRead = null;

				}

				//Get the next entry
				entry = arc.getNextZipEntry();

			}

			return "1";

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				arc.close();
			} catch (Exception e) {}

			try {
				fileReader.close();
			} catch (Exception e) {}

		}

	}

	public static String untar(String in, String dir) throws Exception {

		if (!new File(in).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the source '"+in+"' does not exist.");

		}

		if (new File(dir).isFile()) {

			//Generate an exception
			throw new Exception("Sorry, the destination must be a directory.");

		}

		//Initialization
		TarArchiveInputStream arc = null;
		FileInputStream fileReader = null;

		try {

			fileReader = new FileInputStream(in);
			arc = new TarArchiveInputStream(fileReader);

			//Create the destination directory
			if (!new File(dir).exists()) {

				(new File(dir)).mkdir();

			}

			TarArchiveEntry entry = arc.getNextTarEntry();
			while (entry != null) {

				File destPath = new File((new File(dir)), entry.getName());

				if (entry.getName().endsWith("/")) {

					//Create the directory
					destPath.mkdirs();

				} else {

					//Create the file
					destPath.createNewFile();

					byte [] btoRead = new byte[1024];

					//FileInputStream fin 
					BufferedOutputStream bout = 
							new BufferedOutputStream(new FileOutputStream(destPath));
					int len = 0;
					while((len = arc.read(btoRead)) != -1)
					{
						bout.write(btoRead,0,len);
					}

					//Close
					bout.close();
					btoRead = null;

				}

				//Get the next entry
				entry = arc.getNextTarEntry();

			}

			return "1";

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				arc.close();
			} catch (Exception e) {}

			try {
				fileReader.close();
			} catch (Exception e) {}

		}

	}

	public static void tar(String in, String out) throws Exception {

		if (!new File(in).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the source '"+in+"' does not exist.");

		}

		if (new File(out).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the destination '"+out+"' already exist.");

		}

		//Initialization
		TarArchiveOutputStream arc = null;
		FileOutputStream fileWriter = null;

		try {

			fileWriter = new FileOutputStream(out);
			arc = new TarArchiveOutputStream(fileWriter);

			tar_recursive("", in, arc);

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				arc.flush();
			} catch (Exception e) {}

			try {
				arc.finish();
			} catch (Exception e) {}

			try {
				arc.close();
			} catch (Exception e) {}

			try {
				fileWriter.close();
			} catch (Exception e) {}

		}

	}

	public static void gz(String fileIn, String fileOut) throws Exception {

		if (!new File(fileIn).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the source '"+fileIn+"' does not exist.");

		}

		if (new File(fileOut).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the destination '"+fileOut+"' already exist.");

		}

		//Initialization
		FileOutputStream fileWriter = null;
		GzipCompressorOutputStream gzOut = null;

		try {

			fileWriter = new FileOutputStream(fileOut);
			gzOut = new GzipCompressorOutputStream(fileWriter);
			Path path = Paths.get(fileIn);
			byte[] bytes = Files.readAllBytes(path);
			gzOut.write(bytes);
			gzOut.close();

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				gzOut.close();
			} catch (Exception e) {}

			try {
				fileWriter.close();
			} catch (Exception e) {}

		}

	}

	public static void bz2(String fileIn, String fileOut) throws Exception {

		if (!new File(fileIn).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the source '"+fileIn+"' does not exist.");

		}

		if (new File(fileOut).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the destination '"+fileOut+"' already exist.");

		}

		//Initialization
		FileOutputStream fileWriter = null;
		BZip2CompressorOutputStream gzOut = null;

		try {

			fileWriter = new FileOutputStream(fileOut);
			gzOut = new BZip2CompressorOutputStream(fileWriter);
			Path path = Paths.get(fileIn);
			byte[] bytes = Files.readAllBytes(path);
			gzOut.write(bytes);
			gzOut.close();

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				gzOut.close();
			} catch (Exception e) {}

			try {
				fileWriter.close();
			} catch (Exception e) {}

		}

	}

	public static void ungz(String fileIn, String fileOut) throws Exception {

		if (!new File(fileIn).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the source '"+fileIn+"' does not exist.");

		}

		if (new File(fileOut).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the destination '"+fileOut+"' already exist.");

		}

		//Initialization
		FileInputStream fileReader = null;
		FileOutputStream fileWriter = null;
		GzipCompressorInputStream gzIn = null;

		try {

			fileReader = new FileInputStream(fileIn);
			fileWriter = new FileOutputStream(fileOut);
			gzIn = new GzipCompressorInputStream(fileReader);
			
			final byte[] buffer = new byte[1024];
			int n = 0;
			while (-1 != (n = gzIn.read(buffer))) {
				fileWriter.write(buffer, 0, n);
			}
			

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				gzIn.close();
			} catch (Exception e) {}

			try {
				fileReader.close();
			} catch (Exception e) {}

			try {
				fileWriter.close();
			} catch (Exception e) {}

		}

	}

	public static void unbz2(String fileIn, String fileOut) throws Exception {

		if (!new File(fileIn).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the source '"+fileIn+"' does not exist.");

		}

		if (new File(fileOut).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the destination '"+fileOut+"' already exist.");

		}

		//Initialization
		FileInputStream fileReader = null;
		FileOutputStream fileWriter = null;
		BZip2CompressorInputStream gzIn = null;

		try {

			fileReader = new FileInputStream(fileIn);
			fileWriter = new FileOutputStream(fileOut);
			gzIn = new BZip2CompressorInputStream(fileReader);
			
			final byte[] buffer = new byte[1024];
			int n = 0;
			while (-1 != (n = gzIn.read(buffer))) {
				fileWriter.write(buffer, 0, n);
			}
			

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				gzIn.close();
			} catch (Exception e) {}

			try {
				fileReader.close();
			} catch (Exception e) {}

			try {
				fileWriter.close();
			} catch (Exception e) {}

		}

	}

	public static void tarGz(String in, String out) throws Exception {

		if (!new File(in).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the source '"+in+"' does not exist.");

		}

		if (new File(out).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the destination '"+out+"' already exist.");

		}

		//Initialization
		TarArchiveOutputStream arc = null;
		FileOutputStream fileWriter = null;
		GzipCompressorOutputStream gzOut = null;

		try {

			fileWriter = new FileOutputStream(out);
			gzOut = new GzipCompressorOutputStream(fileWriter);
			arc = new TarArchiveOutputStream(gzOut);

			tar_recursive("", in, arc);

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				arc.flush();
			} catch (Exception e) {}

			try {
				arc.finish();
			} catch (Exception e) {}

			try {
				arc.close();
			} catch (Exception e) {}

			try {
				gzOut.close();
			} catch (Exception e) {}

			try {
				fileWriter.close();
			} catch (Exception e) {}

		}

	}

	public static void untarGz(String in, String dir) throws Exception {

		if (!new File(in).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the source '"+in+"' does not exist.");

		}

		if (new File(dir).isFile()) {

			//Generate an exception
			throw new Exception("Sorry, the destination must be a directory.");

		}
		
		//Create the destination directory
		if (!new File(dir).exists()) {

			(new File(dir)).mkdir();

		}
		
		long i = incr++;
		
		String tmpFileName = dir+"/untarGz_"+DateFx.systimestamp_min()+"_"+i+".tar";
		
		CompressManager.ungz(in, tmpFileName);
		
		CompressManager.untar(tmpFileName, dir);
		
		(new File(tmpFileName)).delete();
		
	}

	public static void untarBz2(String in, String dir) throws Exception {

		if (!new File(in).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the source '"+in+"' does not exist.");

		}

		if (new File(dir).isFile()) {

			//Generate an exception
			throw new Exception("Sorry, the destination must be a directory.");

		}
		
		//Create the destination directory
		if (!new File(dir).exists()) {

			(new File(dir)).mkdir();

		}

		long i = incr++;
		
		String tmpFileName = dir+"/untarBz2_"+DateFx.systimestamp_min()+"_"+i+".tar";
		
		CompressManager.unbz2(in, tmpFileName);
		
		CompressManager.untar(tmpFileName, dir);
		
		(new File(tmpFileName)).delete();

	}

	public static void tarBz2(String in, String out) throws Exception {

		if (!new File(in).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the source '"+in+"' does not exist.");

		}

		if (new File(out).exists()) {

			//Generate an exception
			throw new Exception("Sorry, the destination '"+out+"' already exist.");

		}

		//Initialization
		TarArchiveOutputStream arc = null;
		BZip2CompressorOutputStream arcOut = null;
		FileOutputStream fileWriter = null;

		try {

			fileWriter = new FileOutputStream(out);
			arcOut = new BZip2CompressorOutputStream(fileWriter);
			arc = new TarArchiveOutputStream(arcOut);

			tar_recursive("", in, arc);

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				arc.flush();
			} catch (Exception e) {}

			try {
				arc.finish();
			} catch (Exception e) {}

			try {
				arc.close();
			} catch (Exception e) {}

			try {
				arcOut.close();
			} catch (Exception e) {}

			try {
				fileWriter.close();
			} catch (Exception e) {}

		}

	}

	public static void tar_recursive(String originPath, String in, TarArchiveOutputStream arc) throws Exception {

		File f = new File(in);
		String dir = "";

		if (!originPath.equals("")) {

			dir = originPath + "/";

		}

		if (f.isDirectory()) {

			//Add the directory
			TarArchiveEntry entry = new TarArchiveEntry(dir+f.getName() + "/");
			entry.setSize(0);
			arc.putArchiveEntry(entry);
			arc.closeArchiveEntry();

			//Add sub files or directories
			String[] sub = f.list();
			for(int i=0;i<sub.length;i++) {

				tar_recursive(dir+f.getName(), in+"/"+sub[i], arc);

			}

		} else {

			TarArchiveEntry entry = new TarArchiveEntry(dir+f.getName());
			Path path = Paths.get(in);
			byte[] bytes = Files.readAllBytes(path);
			entry.setSize(bytes.length);
			arc.putArchiveEntry(entry);
			arc.write(bytes);
			arc.closeArchiveEntry();

		}

	}

}
