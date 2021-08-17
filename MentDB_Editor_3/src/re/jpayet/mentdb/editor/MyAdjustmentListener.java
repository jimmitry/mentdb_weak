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

package re.jpayet.mentdb.editor;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import org.fife.ui.rtextarea.RTextScrollPane;

class MyAdjustmentListener implements AdjustmentListener {
	
	private RTextScrollPane sp = null;
	
	public MyAdjustmentListener(RTextScrollPane sp) {
		super();
		this.sp = sp;
	}
	
	public void adjustmentValueChanged(AdjustmentEvent evt) {
		if (evt.getValueIsAdjusting()) {
			return;
		}
		int type = evt.getAdjustmentType();
		switch (type) {
		case AdjustmentEvent.UNIT_INCREMENT:
			break;
		case AdjustmentEvent.UNIT_DECREMENT:
			break;
		case AdjustmentEvent.BLOCK_INCREMENT:
			break;
		case AdjustmentEvent.BLOCK_DECREMENT:
			break;
		case AdjustmentEvent.TRACK:
			
			sp.getVerticalScrollBar().setValue(evt.getValue());
			break;
		}
	}
}