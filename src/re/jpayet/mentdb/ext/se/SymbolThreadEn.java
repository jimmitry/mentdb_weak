package re.jpayet.mentdb.ext.se;
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


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

public class SymbolThreadEn implements Runnable {

	private String c = null;
	private String word = null;
	private ConcurrentHashMap<String, Integer> search = null;
	private Database db;

	public SymbolThreadEn(Database db, String c, String word, ConcurrentHashMap<String, Integer> search) {
		this.c = c;
		this.word = word;
		this.search = search;
		this.db = db;
	}

	@Override
	public void run() {

		try {
			
			if (db.allSymbolsEn.containsKey(c)) {
				
				List<String> words = new ArrayList<String>(db.allSymbolsEn.get(c).keySet());
				for(int j=0;j<words.size();j++) {
					
					String w = words.get(j);
					
					int diff = StringUtils.getLevenshteinDistance(word, w);
					if (!search.containsKey(w)) {
						search.put(w, diff);
					}
					
				}
				
			}
			
		} catch (Exception e) {
			
			System.out.println("#####Symbol Thread Err: "+e.getMessage());
			e.printStackTrace();
			
		}

	}
	
}

