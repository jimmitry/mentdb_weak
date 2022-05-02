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

package re.jpayet.mentdb.core.db.command;

import java.util.Vector;

import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.mail.ImapDiskManager;
import re.jpayet.mentdb.ext.mail.ImapManager;
import re.jpayet.mentdb.ext.session.SessionThread;

//Command full access
public class CommandFullAccess14 {

	//Execute the command
	public static String execute(SessionThread session, Vector<MQLValue> inputVector, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
		case "mail download imap":

			//Get key, name and value
			String output_dir = inputVector.get(3).value;
			String nbMsgToDownload = inputVector.get(4).value;
			String unreadOrAll = inputVector.get(5).value;
			String copyMessageInAnotherFolder = inputVector.get(6).value;
			String deleteMsgAfterDownload = inputVector.get(7).value;
			String markAsRead = inputVector.get(8).value;
			String startDate = inputVector.get(9).value;
			String endDate = inputVector.get(10).value;
			String fromCondition = inputVector.get(11).value;
			String subjectCondition = inputVector.get(12).value;
			String json = inputVector.get(13).value;

			return JsonManager.format_Gson(
					ImapManager.parse(output_dir, nbMsgToDownload, unreadOrAll, 
							copyMessageInAnotherFolder, deleteMsgAfterDownload, 
							markAsRead, startDate, endDate, fromCondition, 
							subjectCondition, json, env, session, 
							parent_pid, current_pid));

		case "mail download imap_disk":

			//Get key, name and value
			output_dir = inputVector.get(3).value;
			nbMsgToDownload = inputVector.get(4).value;
			unreadOrAll = inputVector.get(5).value;
			copyMessageInAnotherFolder = inputVector.get(6).value;
			deleteMsgAfterDownload = inputVector.get(7).value;
			markAsRead = inputVector.get(8).value;
			startDate = inputVector.get(9).value;
			endDate = inputVector.get(10).value;
			fromCondition = inputVector.get(11).value;
			subjectCondition = inputVector.get(12).value;
			json = inputVector.get(13).value;

			return JsonManager.format_Gson(
					ImapDiskManager.parse(output_dir, nbMsgToDownload, unreadOrAll, 
							copyMessageInAnotherFolder, deleteMsgAfterDownload, 
							markAsRead, startDate, endDate, fromCondition, 
							subjectCondition, json, env, session, 
							parent_pid, current_pid));

		default:

			//Script execution
			inputVector.remove(inputVector.size()-1);

			return CommandFullAccess.concatOrUnknow(inputVector);

		}

	}

}