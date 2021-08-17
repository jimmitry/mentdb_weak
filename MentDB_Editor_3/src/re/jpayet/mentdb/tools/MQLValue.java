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

package re.jpayet.mentdb.tools;

import java.util.Vector;

//MQL value
public class MQLValue {

	public String value = "";
	public int cmdBlockType = 0;
	public int line = 0;
	
	//Constructor
	public MQLValue(String value, int isVariable, int line) {
		this.value = value;
		this.cmdBlockType = isVariable;
		this.line = line;
	}
	
	public static MQLValue deepCopyValue(MQLValue m) {
		
		return new MQLValue(m.value, m.cmdBlockType, m.line);
		
	}
	
	public static Vector<Vector<MQLValue>> deepCopyValue(Vector<Vector<MQLValue>> vvm) {
		
		Vector<Vector<MQLValue>> vvm2 = new Vector<Vector<MQLValue>>();
		
		for(int i=0;i<vvm.size();i++) {
			
			Vector<MQLValue> vm1 = vvm.get(i);
			Vector<MQLValue> vm = new Vector<MQLValue>();
			
			for(int j=0;j<vm1.size();j++) {
				
				vm.add(deepCopyValue(vm1.get(j)));
				
			}
			
			vvm2.add(vm);
			
		}
		
		return vvm2;
		
	}

}
