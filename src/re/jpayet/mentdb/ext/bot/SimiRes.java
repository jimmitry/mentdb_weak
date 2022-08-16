package re.jpayet.mentdb.ext.bot;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.encog.util.text.LevenshteinDistance;

import re.jpayet.mentdb.ext.fx.StringFx;

public class SimiRes
{
	
	public int nb_locked = 0, nb_var = 0;;
	String s1 = null;
	String s2 = null;
	public ArrayList<SimiElementInput> t1 = new ArrayList<SimiElementInput>();
	public ArrayList<SimiElementTrigger> t2 = new ArrayList<SimiElementTrigger>();
	
	ArrayList<SimiElementProposition> propositions = new ArrayList<SimiElementProposition>();

	public int result = 0;
	public ArrayList<String> vars = new ArrayList<String>();
	
	public SimiRes(String s1, String s2) {

		
		s1 = StringFx.lrtrim(s1.replace("'", " ").replace("-", " "));
		s2 = StringFx.lrtrim(s2.replace("'", " ").replace("-", " "));

		nb_var = StringUtils.countMatches(s2, "[]");

		this.s1 = s1;
		this.s2 = s2;
		
		String[] t1 = s1.split(" ");
		String[] t2 = s2.split(" ");

		for(String w : t1) {
			this.t1.add(new SimiElementInput(w));
		}
		for(String w : t2) {
			this.t2.add(new SimiElementTrigger(w));
		}
		nb_locked = this.t1.size();
		
	}
	
	public static int diff_word(String w1, String w2) {
		
		//System.out.println("diff: "+w1+" : "+w2);
		
		if (w2.length()>w1.length()) {
			return diff_word_sub(w1, w2);
		} else {
			return diff_word_sub(w2, w1);
		}
		
	}
	
	public static int diff_word_sub(String w1, String w2) {

		return LevenshteinDistance.computeDistance(w1, w2);

	}
	
	public final void search() {
		
		SimiElementProposition sp = null;
		for(int i=0;i<t1.size();i++) {
			SimiElementInput sei = t1.get(i);
			//Find next post
			int next_pos = -1;
			for(int j=0;j<t2.size();j++) {
				SimiElementTrigger set = t2.get(j);
				if (sei.hash==set.hash) {
					next_pos = j;
					sp = new SimiElementProposition(sei);
					sei.is_equal = true;
					break;
				} else {
					if (sei.word.length()<=3) {
						if (diff_word(sei.word, set.word)<=1) {
							next_pos = j;
							sp = new SimiElementProposition(sei);
							sei.is_equal = false;
							break;
						}
					} else if (sei.word.length()<=6) {
						if (diff_word(sei.word, set.word)<=2) {
							next_pos = j;
							sp = new SimiElementProposition(sei);
							sei.is_equal = false;
							break;
						}
					} else {
						if (diff_word(sei.word, set.word)<=3) {
							next_pos = j;
							sp = new SimiElementProposition(sei);
							sei.is_equal = false;
							break;
						}
					}
				}
			}
			if (next_pos!=-1) {
				next_pos++;
				int z=i+1;
				for(;z<t1.size() && next_pos<t2.size();z++) {
					sei = t1.get(z);
					SimiElementTrigger set = t2.get(next_pos);
					if (set.is_var) {
						next_pos++;
					} else {
						if (sei.hash==set.hash) {
							next_pos++;
							sp.add(sei);
							sei.is_equal = true;
						} else {
							if (sei.word.length()<=3) {
								if (diff_word(sei.word, set.word)<=1) {
									next_pos++;
									sp.add(sei);
									sei.is_equal = false;
								} else {
									break;
								}
							} else if (sei.word.length()<=6) {
								if (diff_word(sei.word, set.word)<=2) {
									next_pos++;
									sp.add(sei);
									sei.is_equal = false;
								} else {
									break;
								}
							} else {
								if (diff_word(sei.word, set.word)<=3) {
									next_pos++;
									sp.add(sei);
									sei.is_equal = false;
								} else {
									break;
								}
							}
						}
					}
				}
				i=z-1;
				propositions.add(sp);
			}
		}
		
		Collections.sort(propositions);
		
		/*for(SimiElementProposition sep : propositions) {
			for(int i=0;i<sep.proposition.size();i++) {
				System.out.println(sep.proposition.get(i).toString());
			}
		}*/
		
		for(SimiElementProposition prop : propositions) {
			nb_locked -= prop.lock();
			if (nb_locked==0) {
				break;
			}
		}
		
		vars.add("");
		for(SimiElementInput sei : t1) {
			//System.out.println("###"+sei.word+" : "+sei.locked+" : "+sei.is_equal+" : "+sei.is_next);
			if (sei.locked) {
				if (sei.is_equal) {
					if (sei.is_next) {
						result+= 110;
						//System.out.println("+++110");
						if (!vars.get(vars.size()-1).equals("")) {
							vars.add("");
						}
					} else {
						result+= 100;
						//System.out.println("+++100");
						if (!vars.get(vars.size()-1).equals("")) {
							vars.add("");
						}
					}
				} else {
					if (sei.is_next) {
						result+= 90;
						//System.out.println("+++90");
						if (!vars.get(vars.size()-1).equals("")) {
							vars.add("");
						}
					} else {
						result+= 80;
						//System.out.println("+++80");
						if (!vars.get(vars.size()-1).equals("")) {
							vars.add("");
						}
					}
				}
			} else {
				//System.out.println("#sei.word="+sei.word);
				vars.set(vars.size()-1, vars.get(vars.size()-1)+" "+sei.word);
			}
		}
		
		if (vars.size()==1 && vars.get(0).equals("")) {
			vars = new ArrayList<String>();
		} else {
			
			for(int i=0;i<vars.size();i++) {
				if (vars.get(i).length()>0) {
					vars.set(i, vars.get(i).substring(1));
				}
			}
			
		}
		
		//System.out.println("#vars="+vars.toString());
		//int[] res = {result, t1.size(), nb_var};
		
	}

}