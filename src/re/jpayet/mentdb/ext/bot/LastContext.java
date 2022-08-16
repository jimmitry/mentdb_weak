package re.jpayet.mentdb.ext.bot;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONObject;

public class LastContext {
	
	public ConcurrentHashMap<String, Integer> tab = new ConcurrentHashMap<String, Integer>();
	
	public void add(String cur_context, int depth) throws Exception {
		
		ArrayList<String> to_delete = new ArrayList<String>();
		
		for(String key : tab.keySet()) {
			
			int cur_val = tab.get(key);
			
			cur_val--;
			
			if (cur_val==0) {
				
				to_delete.add(key);
				
			} else {
				
				tab.put(key, cur_val);
				
			}
			
		}
		
		for(String key : to_delete) tab.remove(key);
		
		if (cur_context!=null) {
			tab.put(cur_context, depth);
		}
		
	}
	
	public int get(String context) {
		
		if (tab.containsKey(context)) {
			return tab.get(context);
		} return 0;
		
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject to_str() {
		
		JSONObject result = new JSONObject();
		
		for(String key : tab.keySet()) {
			result.put(key, tab.get(key));
		}
		
		return result;
		
	}
	
}
