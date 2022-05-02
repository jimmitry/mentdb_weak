package re.jpayet.mentdb.ext.se;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class SearchThreadEn implements Runnable {

	private byte city = 0;
	private byte zone = 0;
	private String word = null;
	private String text = null;
	private int depth = 0;
	private ConcurrentHashMap<Long, Double> found = null;
	private ConcurrentHashMap<Long, Integer> found_depth = null;
	private Database db;

	public SearchThreadEn(Database db, byte city, byte zone, String word, int depth, ConcurrentHashMap<Long, Double> found, ConcurrentHashMap<Long, Integer> found_depth, String text) {

		this.city = city;
		this.zone = zone;
		this.word = word;
		this.depth = depth;
		this.found = found;
		this.found_depth = found_depth;
		this.text = text;
		this.db = db;
		
	}

	@Override
	public void run() {

		try {
			
			Vector<Long> v_rel = db.pilesEn.get(word);
			
			for(int i=0;i<v_rel.size() && i<depth;i++) {
				
				Long r = v_rel.get(i);
				
				synchronized (r) {
					
					Relation rel = db.relations.get(r);
					
					if ((city==-1 || city==rel.city) && (zone==-1 || zone==rel.zone)) {
						
						if (!found.containsKey(r)) {
							
							found.put(r, similarity(rel.sentence, text));
							
						}
						
						if (found_depth.containsKey(r)) {
							
							if (i<found_depth.get(r)) {
								found_depth.put(r, i);
							}
							
						} else {
							
							found_depth.put(r, i);
							
						}
						
					}
					
				}
				
			}
			
		} catch (Exception e) {
			
			System.out.println("#####Search Thread Err: "+e.getMessage());
			e.printStackTrace();
			
		}

	}
	
	public static double similarity(String s1, String intput) {
		
		String[] sentence = s1.split(" ");
		double nb_found = 0;
		
		HashMap<Integer, Integer> pos = new HashMap<Integer, Integer>();
		
	    String[] cuts = intput.split(" ");
	    
	    for(int i=0;i<cuts.length;i++) {
	    	
	    		String current_input_word = cuts[i];
	    		
	    		if (!current_input_word.equals("")) {
	    			
		    		for(int j=0;j<sentence.length;j++) {
		    			
		    			String current_sentence_word = sentence[j];
		    			
		    			if (similarity2(current_input_word, current_sentence_word)>=95) {
	    					
	    					nb_found++;
	    					
	    					pos.put(j, 1);
	    					
	    				}
			    	
			    }
		    		
	    		}
	    	
	    }
	    
	    List<Map.Entry<Integer, Integer>> list = new LinkedList<>( pos.entrySet() );
		
	    Collections.sort( list, new Comparator<Map.Entry<Integer, Integer>>() {
			@Override
			public int compare( Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2 )
			{
				return o2.getValue().compareTo(o1.getValue());
			}
		} );
	    
	    int last = -1;
	    for(int i=0;i<list.size();i++) {
	    	
	    		if (i>0 && last==list.get(i).getKey()) {
	    			nb_found += 0.5;
	    		}
	    		
	    		last = list.get(i).getKey();
	    	
	    }

	    return nb_found;

	  }
	
	public static double similarity2(String s1, String s2) {
		
	    String longer = s1, shorter = s2;
	    if (s1.length() < s2.length()) { // longer should always have greater length
	      longer = s2; shorter = s1;
	    }
	    int longerLength = longer.length();
	    if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
	    return ((longerLength - editDistance(longer, shorter)) / (double) longerLength)*100.0;

	  }

	  public static int editDistance(String s1, String s2) {
	    s1 = s1.toLowerCase();
	    s2 = s2.toLowerCase();

	    int[] costs = new int[s2.length() + 1];
	    for (int i = 0; i <= s1.length(); i++) {
	      int lastValue = i;
	      for (int j = 0; j <= s2.length(); j++) {
	        if (i == 0)
	          costs[j] = j;
	        else {
	          if (j > 0) {
	            int newValue = costs[j - 1];
	            if (s1.charAt(i - 1) != s2.charAt(j - 1))
	              newValue = Math.min(Math.min(newValue, lastValue),
	                  costs[j]) + 1;
	            costs[j - 1] = lastValue;
	            lastValue = newValue;
	          }
	        }
	      }
	      if (i > 0)
	        costs[s2.length()] = lastValue;
	    }
	    return costs[s2.length()];
	  }
	
}

