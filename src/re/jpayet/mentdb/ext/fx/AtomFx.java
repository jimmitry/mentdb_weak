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

package re.jpayet.mentdb.ext.fx;

import java.util.regex.Pattern;

//The type class
public class AtomFx {
	
	public static String before_exclud(String atomList, String index, String separator) {
		
		//Try to get the atom in atom list
		try {
			
			//All parameters can not be null (return null in this case)
			if (atomList==null || index==null || separator==null || separator.length()!=1) {
				
				return null;
				
			} else {
				
				if (Integer.parseInt(index)<1 || Integer.parseInt(index)>Integer.parseInt(size( atomList, separator))) {
					
					return null;
					
				} else {
				
					String beforeExcludedAtomList="";
					
					//parse the list
					for(int i=1;i<Integer.parseInt(index);i++) {
						//Create the list
						beforeExcludedAtomList+=separator+get( atomList, ""+i, separator);
					}
					
					//Delete the first char if not empty
					if (!beforeExcludedAtomList.equals("")) 
						beforeExcludedAtomList=beforeExcludedAtomList.substring(1);
					
					return beforeExcludedAtomList;
					
				}
				
			}
			
		} catch (Exception e) {
			
			//If an error appears then return null
			return null;
			
		}
		
	}
	
	public static String before_includ(String atomList, String index, String separator) {
		
		//Try to get the atom in atom list
		try {
			
			//All parameters can not be null (return null in this case)
			if (atomList==null || index==null || separator==null || separator.length()!=1) {
				
				return null;
				
			} else {
				
				if (Integer.parseInt(index)<1 || Integer.parseInt(index)>Integer.parseInt(size( atomList, separator))) {
					
					return null;
					
				} else {
				
					String beforeIncludedAtomList="";
					
					//parse the list
					for(int i=1;i<=Integer.parseInt(index);i++) {
						
						//Create the list
						beforeIncludedAtomList+=separator+get( atomList, ""+i, separator);
						
					}
					
					//Delete the first char if not empty
					if (!beforeIncludedAtomList.equals("")) 
						beforeIncludedAtomList=beforeIncludedAtomList.substring(1);
					
					return beforeIncludedAtomList;
					
				}
				
			}
			
		} catch (Exception e) {
			
			//If an error appears then return null
			return null;
			
		}
		
	}
	
	public static String count(String atomList, String atomToCount, String separator) {
		
		//Try to count
		try {
			
			//All parameters can not be null (return null in this case)
			if (atomList==null || atomToCount==null || separator==null || separator.length()!=1) {
				
				return null;
				
			} else {
				
				//Initialization
				int counter = 0;
				
				//parse the list
				for(int i=1;i<=Integer.parseInt(size( atomList, separator));i++) {
					
					if (get( atomList, ""+i, separator).equals(atomToCount)) 
						counter++;
					
				}
				
				return ""+counter;
				
			}
			
		} catch (Exception e) {
			
			//If an error appears then return null
			return null;
			
		}
		
	}
	
	public static String count_distinct(String atomList, String atomToCount, String separator) {
		
		//Try to count
		try {
			
			//All parameters can not be null (return null in this case)
			if (atomList==null || atomToCount==null || separator==null || separator.length()!=1) {
				
				return null;
				
			} else {
				
				//Initialization
				int counter = 0;
				String copyAtomList = distinct( atomList, separator);
				
				//parse the list
				for(int i=1;i<=Integer.parseInt(size( copyAtomList, separator));i++) {
					
					if (get( copyAtomList, ""+i, separator).equals(atomToCount)) 
						counter++;
					
				}
				
				return ""+counter;
				
			}
			
		} catch (Exception e) {
			
			//If an error appears then return null
			return null;
			
		}
		
	}
	
	public static String count_lrtrim(String atomList, String atomToCount, String separator) {
		
		//Try to count
		try {
			
			//All parameters can not be null (return null in this case)
			if (atomList==null || atomToCount==null || separator==null || separator.length()!=1) {
				
				return null;
				
			} else {
				
				//Initialization
				int counter = 0;
				
				//parse the list
				for(int i=1;i<=Integer.parseInt(size( atomList, separator));i++) {
					
					if (StringFx.lrtrim(get( atomList, ""+i, separator)).equals(StringFx.lrtrim(atomToCount))) 
						counter++;
					
				}
				
				return ""+counter;
				
			}
			
		} catch (Exception e) {
			
			//If an error appears then return null
			return null;
			
		}
		
	}
	
	public static String count_lrtrim_distinct(String atomList, String atomToCount, String separator) {
		
		//Try to count
		try {
			
			//All parameters can not be null (return null in this case)
			if (atomList==null || atomToCount==null || separator==null || separator.length()!=1) {
				
				return null;
				
			} else {
				
				//Initialization
				int counter = 0;
				String copyAtomList = distinct_lrtrim( atomList, separator);
				
				//parse the list
				for(int i=1;i<=Integer.parseInt(size( copyAtomList, separator));i++) {
					
					if (StringFx.lrtrim(get( copyAtomList, ""+i, separator)).equals(StringFx.lrtrim(atomToCount))) 
						counter++;
					
				}
				
				return ""+counter;
				
			}
			
		} catch (Exception e) {
			
			//If an error appears then return null
			return null;
			
		}
		
	}

	public static String distinct(String atomList, String separator) {
		
		//Try to get the distinct atom list
		try {
			
			//Atom list and separator can not be null
			if (atomList==null || separator==null || separator.length()!=1) {
				
				return null;
				
			} else {

				//Get the distinct atom list
				String distinctAtomList = "";
				
				//parse the list
				for(int i=1;i<=Integer.parseInt(size( atomList, separator));i++) {
					
					if (find( distinctAtomList, get( atomList, ""+i, separator), separator).equals("0")) 
						distinctAtomList += separator+get( atomList, ""+i, separator);
				
				}
				
				//Delete the first char if not empty
				if (!distinctAtomList.equals("")) distinctAtomList=distinctAtomList.substring(1);
				
				return distinctAtomList;
				
			}
			
		} catch (Exception e) {
			
			//If an error was generated than return null
			return null;
			
		}
		
	}

	public static String distinct_lrtrim(String atomList, String separator) {
		
		//Try to get the distinct atom list
		try {
			
			//Atom list and separator can not be null
			if (atomList==null || separator==null || separator.length()!=1) {
				
				return null;
				
			} else {

				String distinctLRTrimAtom="";
				
				//parse the list
				for(int i=1;i<=Integer.parseInt(size( atomList, separator));i++) {
					
					if (find( distinctLRTrimAtom, StringFx.lrtrim(get( atomList, ""+i, separator)), separator).equals("0")) 
						distinctLRTrimAtom+=separator+StringFx.lrtrim(get( atomList, ""+i, separator));
					
				}
				
				//Delete two first chars if not empty
				if (!distinctLRTrimAtom.equals("")) distinctLRTrimAtom=distinctLRTrimAtom.substring(1);
				
				return distinctLRTrimAtom;
				
			}
			
		} catch (Exception e) {
			
			//If an error appears then return null
			return null;
			
		}
		
	}

	public static String distinct_lrtrim_1sbefore(String atomList, String separator) {
		
		//Try to get the distinct atom list
		try {
			
			//Atom list and separator can not be null
			if (atomList==null || separator==null || separator.length()!=1) {
				
				return null;
				
			} else {

				String distinctLRTrimAtom="";
				
				//parse the list
				for(int i=1;i<=Integer.parseInt(size( atomList, separator));i++) {
					
					if (find( distinctLRTrimAtom, StringFx.lrtrim(get( atomList, ""+i, separator)), separator).equals("0")) 
						distinctLRTrimAtom+=separator+StringFx.lrtrim(get( atomList, ""+i, separator));
					
				}
				
				//Delete two first chars if not empty
				if (!distinctLRTrimAtom.equals("")) distinctLRTrimAtom=distinctLRTrimAtom.substring(1);
				
				return distinctLRTrimAtom.replace(",", ", ");
				
			}
			
		} catch (Exception e) {
			
			//If an error appears then return null
			return null;
			
		}
		
	}
	
	public static String find(String atomList, String atomToFind, String separator) {
		
		//try to get the position
		try {
			
			//All parameters can not be null (return null in this case)
			if (atomList==null || atomToFind==null || separator==null || separator.length()!=1) {
				
				return null;
				
			} else {
				
				int position = 0;
				
				//parse the list
				for(int j=1;j<=Integer.parseInt(size( atomList, separator));j++) {
					
					if (get( atomList, ""+j, separator).equals(atomToFind)) {
						position=j; break;
					}
					
				}
				
				return ""+position;
			}
			
		} catch (Exception e) {
			
			//If an error was generated then return null
			return null;
		}
		
	}
	
	public static String find_lrtrim(String atomList, String atomToFind, String separator) {
		
		//try to get the position
		try {
			
			//All parameters can not be null (return null in this case)
			if (atomList==null || atomToFind==null || separator==null || separator.length()!=1) {
				
				return null;
				
			} else {
				
				int position = 0;
				
				//parse the list
				for(int j=1;j<=Integer.parseInt(size( atomList, separator));j++) {
					
					if (StringFx.lrtrim(get( atomList, ""+j, separator)).equals(StringFx.lrtrim(atomToFind))) {
						position=j; break;
					}
					
				}
				
				return ""+position;
			}
			
		} catch (Exception e) {
			
			//If an error was generated then return null
			return null;
		}
		
	}

	public static String get(String atomList, String index, String separator) {
		
		//Try to get the atom in atom list
		try {
			
			//All parameters can not be null (return null in this case)
			if (atomList==null || index==null || separator==null || separator.length()!=1) {
				
				return null;
				
			} else {
				
				if (Integer.parseInt(index)<1 || Integer.parseInt(index)>Integer.parseInt(size( atomList, separator))) {
					
					return null;
					
				} else {
				
					//Prepare the list
					String copyAtomList=atomList;
					Pattern motif = null;
					
					if (separator.equals("[") || separator.equals("]")) motif = Pattern.compile("[\\"+separator+"]");
					else motif = Pattern.compile("["+separator+"]");
					copyAtomList="d"+separator+copyAtomList+separator+"f";
					
					//Split the list
					String[] ch = motif.split(copyAtomList, -1);
					
					//Return the atom
					return ch[Integer.parseInt(index)];
					
				}
				
			}
			
		} catch (Exception e) {
			
			//If an error appears then return null
			return null;
			
		}
		
	}
	
	public static String get_first(String atomList, String separator) {
		
		//Try to get the first atom
		try {
			
			//All parameters can not be null (return null in this case)
			if (atomList==null || separator==null || separator.length()!=1) {
				
				return null;
				
			} else {

				//Get the first atom
				return get( atomList, "1", separator);
				
			}
			
		} catch (Exception e) {
			
			//If an error appears then return null
			return null;
			
		}
		
	}

	public static String get_first_lrtrim(String atomList, String separator) {
		
		//Try to get the first atom
		try {
			
			//All parameters can not be null (return null in this case)
			if (atomList==null || separator==null || separator.length()!=1) {
				
				return null;
				
			} else {

				//Get the first atom
				return StringFx.lrtrim(get( atomList, "1", separator));
				
			}
			
		} catch (Exception e) {
			
			//If an error appears then return null
			return null;
			
		}
		
	}
	
	public static String get_last(String atomList, String separator) {
		
		//Try to get the last atom
		try {
			
			//All parameters can not be null (return null in this case)
			if (atomList==null || separator==null || separator.length()!=1) {
				
				return null;
				
			} else {

				//Get the last atom
				return get( atomList, size( atomList, separator), separator);
				
			}
		} catch (Exception e) {
			
			//If an error appears then return null
			return null;
			
		}
		
	}
	
	public static String get_last_lrtrim(String atomList, String separator) {
		
		//Try to get the last atom
		try {
			
			//All parameters can not be null (return null in this case)
			if (atomList==null || separator==null || separator.length()!=1) {
				
				return null;
				
			} else {

				//Get the last atom
				return StringFx.lrtrim(get( atomList, size( atomList, separator), separator));
				
			}
		} catch (Exception e) {
			
			//If an error appears then return null
			return null;
			
		}
		
	}

	public static String get_lrtrim(String atomList, String index, String separator) {
		
		//Try to get the atom in atom list
		try {
			
			//All parameters can not be null (return null in this case)
			if (atomList==null || index==null || separator==null || separator.length()!=1) {
				
				return null;
				
			} else {
				
				if (Integer.parseInt(index)<1 || Integer.parseInt(index)>Integer.parseInt(size( atomList, separator))) {
					
					return null;
					
				} else {
				
					//Prepare the list
					String copyAtomList=atomList;
					Pattern motif = null;
					
					if (separator.equals("[") || separator.equals("]")) motif = Pattern.compile("[\\"+separator+"]");
					else motif = Pattern.compile("["+separator+"]");
					copyAtomList="d"+separator+copyAtomList+separator+"f";
					
					//Split the list
					String[] ch = motif.split(copyAtomList, -1);
					
					return StringFx.lrtrim(ch[Integer.parseInt(index)]);
					
				}
				
			}
			
		} catch (Exception e) {
			
			//If an error appears then return null
			return null;
			
		}
		
	}
	
	public static String lrtrim(String atomList, String separator) {
		
		//Try to get the atom list
		try {
			
			//Atom list and separator can not be null
			if (atomList==null || separator==null || separator.length()!=1) {
				
				return null;
				
			} else {

				String LRTrimAtomList="";
				
				//parse the list
				for(int i=1;i<=Integer.parseInt(size( atomList, separator));i++) {
					
					LRTrimAtomList+=separator+StringFx.lrtrim(get( atomList, ""+i, separator));
					
				}
				
				//Delete the first char if not empty
				if (!LRTrimAtomList.equals("")) LRTrimAtomList=LRTrimAtomList.substring(1);
				
				return LRTrimAtomList;
				
			}
			
		} catch (Exception e) {
			
			//If an error was generated than return null
			return null;
			
		}
		
	}
	
	public static String position(String atomList, String atomToFind, String separator) {
		
		//FIND_ATOM
		return find( atomList, atomToFind, separator);
		
	}
	
	public static String position_lrtrim(String atomList, String atomToFind, String separator) {
		
		//FIND_LRTRIM_ATOM
		return find_lrtrim( atomList, atomToFind, separator);
		
	}
	
	public static String size(String atomList, String separator) {
		
		//Try to get the size
		try {
			
			//All parameters can not be null (return null in this case)
			if (atomList==null || separator==null || separator.length()!=1) {
				
				return null;
				
			} else {
				
				//Prepare list
				String copyAtomList=atomList;
				Pattern motif = null;
				
				if (separator.equals("[") || separator.equals("]")) motif = Pattern.compile("[\\"+separator+"]");
				else motif = Pattern.compile("["+separator+"]");
				copyAtomList="d"+separator+copyAtomList+separator+"f";
				
				//Split the list
				String[] ch = motif.split(copyAtomList, -1);
				
				//Return the size
				return ""+(ch.length-2);
				
			}
			
		} catch (Exception e) {
			
			//If an error appears then return null
			return null;
			
		}
		
	}

}
