package re.jpayet.mentdb.ext.bot;

import java.util.ArrayList;

public class SimiElementProposition implements Comparable<SimiElementProposition> {
	
	public ArrayList<SimiElementInput> proposition = new ArrayList<SimiElementInput>();

	public SimiElementProposition(SimiElementInput sei) {
		proposition.add(sei);
	}

	public final void add(SimiElementInput sei) {
		proposition.add(sei);
	}
	
	@Override
    public int compareTo(SimiElementProposition comparepropo) {
		if (comparepropo.proposition.size()>proposition.size()) {
			return 1;
		} else if (comparepropo.proposition.size()<proposition.size()) {
			return -1;
		} else {
			return 0;
		}
    }

	public final byte lock() {
		byte nb = 0;
		for(SimiElementInput sei: proposition) {
			
			if (!sei.locked) {
				sei.locked = true;
				if (nb>0) {
					sei.is_next = true;
				}
				nb++;
			}
			
		}
		return nb;
	}

}