package re.jpayet.mentdb.ext.se;
/**
 * Project: MentDB
 * Description: Mentalese Database Engine
 * Website: http://www.innov-ai.com
 * Author: Jimmitry Payet
 * Mail: jim@innov-ai.com
 * Locality: Reunion Island (French)
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Symbol {
	
	public static void stimulateEn(Database db, String word) throws Exception {
		
		for(int i=0;i<word.length();i++) {
			
			String c = ""+word.charAt(i);
			
			LinkedHashMap<String, Integer> words = null;
			if (db.allSymbolsEn.containsKey(c)) {
				words = db.allSymbolsEn.get(c);
			} else {
				words = new LinkedHashMap<String, Integer>();
				db.allSymbolsEn.put(c, words);
			}
			
			if (words.containsKey(word)) {
				words.remove(word);
			}

			words.put(word, 0);
			
			if (words.size()>IndexAI.NB_WORDS_BY_SYMBOL) {
				List<String> orderedKeys = new ArrayList<String>(words.keySet());
				words.remove(orderedKeys.get(0));
			}
			
		}
		
	}
	
	public static void stimulateFr(Database db, String word) throws Exception {
		
		for(int i=0;i<word.length();i++) {
			
			String c = ""+word.charAt(i);
			
			LinkedHashMap<String, Integer> words = null;
			if (db.allSymbolsFr.containsKey(c)) {
				words = db.allSymbolsFr.get(c);
			} else {
				words = new LinkedHashMap<String, Integer>();
				db.allSymbolsFr.put(c, words);
			}
			
			if (words.containsKey(word)) {
				words.remove(word);
			}

			words.put(word, 0);
			
			if (words.size()>IndexAI.NB_WORDS_BY_SYMBOL) {
				List<String> orderedKeys = new ArrayList<String>(words.keySet());
				words.remove(orderedKeys.get(0));
			}
			
		}
		
	}
	
	public static List<Map.Entry<String, Integer>> findWordEn(Database db, String word) throws Exception {
		
		ConcurrentHashMap<String, Integer> search = new ConcurrentHashMap<String, Integer>();
		
		//Initialization
		ExecutorService executor = Executors.newFixedThreadPool(word.length());
		
		//Create workers
		for(int i=0;i<word.length();i++) {
			
			String c = ""+word.charAt(i);
			
            executor.execute((Runnable) new SymbolThreadEn(db, c, word, search));
            
        }
		
		//Wait
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
		
		List<Map.Entry<String, Integer>> list = new LinkedList<>( search.entrySet() );
		Collections.sort( list, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
			{
				return o1.getValue().compareTo(o2.getValue());
			}
		} );
		
		return list;
		
	}
	
	public static List<Map.Entry<String, Integer>> findWordFr(Database db, String word) throws Exception {
		
		ConcurrentHashMap<String, Integer> search = new ConcurrentHashMap<String, Integer>();
		
		//Initialization
		ExecutorService executor = Executors.newFixedThreadPool(word.length());
		
		//Create workers
		for(int i=0;i<word.length();i++) {
			
			String c = ""+word.charAt(i);
			
            executor.execute((Runnable) new SymbolThreadFr(db, c, word, search));
            
        }
		
		//Wait
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
		
		List<Map.Entry<String, Integer>> list = new LinkedList<>( search.entrySet() );
		Collections.sort( list, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
			{
				return o1.getValue().compareTo(o2.getValue());
			}
		} );
		
		return list;
		
	}
	
	public static int get_percent(int size) throws Exception {
		
		if (size<=2) {
			return 50;
		} else if (size<=3) {
			return 34;
		} else if (size<=5) {
			return 30;
		} else if (size<=7) {
			return 25;
		} else if (size<=10) {
			return 20;
		} else {
			return 15;
		}
		
	}

}
