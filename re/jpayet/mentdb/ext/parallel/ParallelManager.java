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

package re.jpayet.mentdb.ext.parallel;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.session.SessionThread;

public class ParallelManager {

	@SuppressWarnings("unchecked")
	public static JSONArray exe(EnvManager env, Vector<MQLValue> parameters, SessionThread session, String parentId, String current_pid) throws Exception {
		
		//Initialization
		Vector<MQLWorkerThread> run = new Vector<MQLWorkerThread>();
		ExecutorService executor = Executors.newFixedThreadPool(parameters.size());
		
		//Create workers
		for(int i=0;i<parameters.size();i++) {

			MQLWorkerThread workerMql = new MQLWorkerThread(env, parameters.get(i).value, session, parentId, current_pid);
            Runnable worker = workerMql;
            run.add(workerMql);
            executor.execute(worker);
            
        }
		
		//Wait
        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        JSONArray result = new JSONArray();
        
        //Get results
        for(int i=0;i<run.size();i++) {
        	
        		JSONObject o = new JSONObject();
        	
	        	//Concatenation
	        	if (run.get(i).isError) {

	        		o.put("status", "KO");
	        		o.put("result", run.get(i).result);
	        		
	        	} else {

	        		o.put("status", "OK");
	        		o.put("result", run.get(i).result);
	        		
	        	}
	        	
	        	result.add(o);
		    
		}
		
		return result;

	}

}
