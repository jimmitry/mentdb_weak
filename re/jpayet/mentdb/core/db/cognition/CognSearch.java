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

package re.jpayet.mentdb.core.db.cognition;

import java.util.HashMap;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CognSearch {
	
	public static String[] inWords = null;
	public static int[] groupIndex = null;

	//Constructor
	public static void reset(String[] inWords) {

		CognSearch.inWords = inWords;
		groupIndex = new int[inWords.length];
		
		for(int i=0;i<inWords.length;i++) {
			groupIndex[i] = -1;
		}

	}

	//Count empty
	public static int countEmpty() {
		
		int result = 0;
		
		for(int i=0;i<groupIndex.length;i++) {
			
			if (groupIndex[i]==-1) {
				
				result ++;
				
			}
			
		}
		
		return result;
		
	}

	//Contains
	public static boolean contains(int group) {
		
		boolean result = false;
		
		for(int i=0;i<inWords.length;i++) {
			
			if (groupIndex[i]==group) {
				
				result = true;
				break;
				
			}
			
		}
		
		return result;
		
	}

	//Correction
	public static String get(int group) {
		
		String result = "";
		
		for(int i=0;i<inWords.length;i++) {
			
			if (groupIndex[i]==group) {
				
				result += " "+inWords[i];
				
			}
			
		}
		
		if (result.length()>0) result = result.substring(1);
		
		return result;
		
	}

	//Correction
	public static void correction(int groupSize) {
		
		Vector<Integer> otherGroups = new Vector<Integer>();
		for(int i=0;i<groupSize;i++) {
			if (!contains(i) && !otherGroups.contains(i)) {
				otherGroups.add(i);
			}
		}
		
		
		if (otherGroups.size()==1) {
			
			//Last group to select
			int lastGroup = otherGroups.get(0);
			
			for(int i=0;i<groupIndex.length;i++) {
				
				if (groupIndex[i]==-1) {
					
					groupIndex[i] = lastGroup;
					
				}
				
			}
			
		} else if (otherGroups.size()==countEmpty()) {
			
			int iOther = 0;
			
			//Same number
			for(int i=0;i<groupIndex.length;i++) {
				
				if (groupIndex[i]==-1) {
					
					groupIndex[i] = otherGroups.get(iOther);
					
					iOther++;
					
				}
				
			}
			
		} else {
			
			//Others

			int lastGroup = -1;
			for(int i=0;i<groupIndex.length;i++) {
				
				int currentGroup = groupIndex[i];
				
				if (currentGroup!=-1) {
					
					lastGroup = currentGroup;
					
				} else if (lastGroup!=-1) {
					
					groupIndex[i] = lastGroup+1;
					
				}
				
			}
	
			lastGroup = -1;
			for(int i=groupIndex.length-1;i>=0;i--) {
				
				int currentGroup = groupIndex[i];
				
				if (currentGroup!=-1) {
					
					lastGroup = currentGroup;
					
				} else if (lastGroup!=-1) {
					
					groupIndex[i] = lastGroup-1;
					
				}
				
			}
			
		}

	}
	
	//Show 
	public static String show() {

		String index = "";
		String word = "";
		String ii = "";
		for(int i=0;i<inWords.length;i++) {
			index += groupIndex[i]+" ";
			word += inWords[i]+" ";
			ii += i+" ";
		}
		
		return index+"\n"+word+"\n"+ii;
		
	}
	
	//Show 
	public static void set(int pos, int groupIndex) {

		CognSearch.groupIndex[pos] = groupIndex;
		
	}
	
	//Show 
	public static String lastThought(int iThought, JSONArray stimulation) {
		
		String result = "";
		boolean breakFor = false;

		int j = 1;
		for(int i=0;i<stimulation.size();i++) {
			
			JSONArray currentStim = (JSONArray) ((JSONObject) ((JSONArray) stimulation.get(i)).get(0)).get("words");

			for(int z=0;z<currentStim.size();z++) {
				
				if (j==iThought) {
					
					result = currentStim.get(z)+"";
					
					breakFor = true;
					
				}
				
				if (breakFor) break;
				
				j++;
				
			}
			
			if (breakFor) break;
		}
		
		return result;
		
	}
	
	//Search by probability
	@SuppressWarnings("unchecked")
	public static void byProbability(JSONArray groups, String[] inWords) {
		
		reset(inWords);
		
		//Result
		JSONObject probabilities = new JSONObject();
		
		for(int iGroup=0;iGroup<groups.size();iGroup++) {

			JSONObject currentGroup = ((JSONObject) groups.get(iGroup));
			int values = 0;
			
			//Not thoughts
			if ((currentGroup.get("type")+"").equals("W")) {

				//Get all thoughts from the strategy
				JSONArray words = ((JSONArray) ((JSONObject) groups.get(iGroup)).get("th"));
				
				JSONObject objProb = new JSONObject();
				
				//Parse all words
				int iPosInGroup = 0;
				int first_iWord=-1;
				HashMap<Integer, Integer> alreadySearch = new HashMap<Integer, Integer>();
				for(int iWord=0;iWord<inWords.length;iWord++) {
					
					if (alreadySearch.get(iWord)==null || alreadySearch.get(iWord)<=2) {
					
						String in = inWords[iWord];
						
						boolean foundWord = false;
	
						//Parse words
						for(;iPosInGroup<words.size();iPosInGroup++) {
	
							JSONObject obj = ((JSONObject) ((JSONObject) words.get(iPosInGroup)).get("words"));
	
							if (obj.containsKey(in)) {
								
								if (first_iWord==-1) first_iWord=iWord;
								values++;
								iPosInGroup++;
								foundWord = true;
								break;
							
							}
						
						}
						
						if (!foundWord) {
							
							if (alreadySearch.containsKey(iWord)) {
								alreadySearch.put(iWord, ((Integer) alreadySearch.get(iWord))+1);
							} else {
								alreadySearch.put(iWord, 1);
							}
							
							objProb.put(first_iWord, values);
							values = 0;
							iPosInGroup = 0;
							iWord--;
							first_iWord=-1;
							
						}	
						
					}
				
				}
				
				if (values>0) {
					
					objProb.put(first_iWord, values);
					
				}
				
				probabilities.put(iGroup, objProb);
				
			}
			
		}

		//Search the max for each group
		int valid_max_key_group = -1;
		int valid_max_key_indexWord = -1;
		int valid_max_key_val_indexWord = -1;
		while (probabilities.size()>0) {
			
			//Parse the probability object
			for(Object g : probabilities.keySet()) {
				
				Integer key_group = (Integer) g;
				JSONObject indexWords = (JSONObject) probabilities.get(key_group);
				
				//Parse the index words object
				for(Object iw : indexWords.keySet()) {
					
					Integer key_indexWord = (Integer) iw;
					int val = (Integer) indexWords.get(key_indexWord);
					
					if (val>valid_max_key_val_indexWord) {
						valid_max_key_group = key_group;
						valid_max_key_indexWord = key_indexWord;
						valid_max_key_val_indexWord = val;
					}
					
				}
				
			}

			//Mark groups
			for(int i=valid_max_key_indexWord;i<valid_max_key_indexWord+valid_max_key_val_indexWord;i++) {
				
				set(i, valid_max_key_group);
				
			}
			
			probabilities.remove(valid_max_key_group);
			
		}
		
		//System.out.println(show());
		
		//End group correction
		correction(groups.size());
		
		//System.out.println(show());
		
	}

}
