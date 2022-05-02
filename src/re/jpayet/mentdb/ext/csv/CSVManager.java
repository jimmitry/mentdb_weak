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

package re.jpayet.mentdb.ext.csv;

import java.io.FileReader;
import java.util.Vector;

import com.opencsv.CSVReader;

import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.AtomFx;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.tools.Misc;

//CSV parser
public class CSVManager {

	public static void parse(EnvManager env, SessionThread session, String filePath, String columnSeparator, String quoteChar, String namespace, String forceColumnNames, String mqlAction, String parent_pid, String current_pid) throws Exception {

		//Initialization
		namespace = re.jpayet.mentdb.ext.statement.Statement.eval(session, namespace, env, parent_pid, current_pid);
		filePath = re.jpayet.mentdb.ext.statement.Statement.eval(session, filePath, env, parent_pid, current_pid);
		columnSeparator = re.jpayet.mentdb.ext.statement.Statement.eval(session, columnSeparator, env, parent_pid, current_pid);
		quoteChar = re.jpayet.mentdb.ext.statement.Statement.eval(session, quoteChar, env, parent_pid, current_pid);
		forceColumnNames = re.jpayet.mentdb.ext.statement.Statement.eval(session, forceColumnNames, env, parent_pid, current_pid);

		CSVReader reader = null;
		
		try {
		
			reader = new CSVReader(new FileReader(filePath), columnSeparator.charAt(0), quoteChar.charAt(0));
			
			Vector<String> colnames = new Vector<String>();
	
			String[] values;
			
			if (forceColumnNames!=null && !forceColumnNames.equals("")) {
				
				//Get the first column
				values = reader.readNext();
				
				int nb = Integer.parseInt(AtomFx.size(forceColumnNames, columnSeparator));
				for(int i=1;i<=nb;i++) {
					colnames.add(AtomFx.get(forceColumnNames, ""+i, columnSeparator));
				}
				
			} else {
				
				//Get the first column
				values = reader.readNext();
				
				for(int i=0;i<values.length;i++) {
					
					colnames.add(values[i]);
							
				}
				
			}
			
			Vector<Vector<MQLValue>> repeat_action = Misc.splitCommand(mqlAction);
			
			try {
	
				while ((values = reader.readNext()) != null) {
		
					for(int i=0;i<values.length;i++) {
						
						env.set("["+namespace+"_"+colnames.get(i)+"]", values[i]);
						
					}
					
					try {
					
						//Execute action
						re.jpayet.mentdb.ext.statement.Statement.eval(session, MQLValue.deepCopyValue(repeat_action), env, parent_pid, current_pid);
					
					} catch (Exception e) {
						if (e.getMessage()==null || !e.getMessage().endsWith("j28hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at")) {
							throw e;
						}
					};

					session.sessionThreadAgent.current_function = "csv parse > next";
		
				}
				
			}  catch (Exception m) {
				if (m.getMessage()==null || !m.getMessage().endsWith("j18hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at")) {
					throw m;
				}
			}
			
		} catch (Exception e) {

			throw new Exception("Sorry, there is an error ("+e.getMessage()+").");

		} finally {

			try {

				//Close the CSV file
				reader.close();

			} catch (Exception f) {}

		}

	}

}