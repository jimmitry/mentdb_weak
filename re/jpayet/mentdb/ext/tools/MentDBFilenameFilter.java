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

import java.io.File;
import java.io.FilenameFilter;

import re.jpayet.mentdb.ext.fx.StringFx;

public class MentDBFilenameFilter implements FilenameFilter {

	String regexFilter;
	boolean getFile;
	boolean getDirectory;

	// constructor takes string argument
	public MentDBFilenameFilter(String regexFilter, boolean getFile, boolean getDirectory)
	{
		this.regexFilter = regexFilter;
		this.getFile = getFile;
		this.getDirectory = getDirectory;
	}

	@Override
	public boolean accept(File dir, String name) {
		
		File f = new File(dir.getAbsolutePath()+"/"+name);

		//Get directories
		if (f.isDirectory() && getDirectory && StringFx.matches(name, regexFilter).equals("1")) {
			return true;
		}

		//Get files
		if (f.isFile() && getFile && StringFx.matches(name, regexFilter).equals("1")) {
			return true;
		}

		return false;
	}
	
}