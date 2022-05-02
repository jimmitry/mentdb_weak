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

package re.jpayet.mentdb.ext.mongodb;

import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.common.collect.ImmutableList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;

import re.jpayet.mentdb.ext.env.EnvManager;

public class MongoDBManager {

	public static void main(String[] args) throws Exception {

		String sess = "sessionId1";
		String sessdb = "sessionIdDb1";
		String sesscollection = "sessionIdCol1";
		
		EnvManager env = new EnvManager();
		
		client_connect(env, "mongodb://localhost:27017", sess);
		
		System.out.println("DBS: "+database_show(env, sess));

		database_load(env, sess, sessdb, "jim");
		System.out.println("Stat: "+database_stat(env, sessdb));
		
		System.out.println("Collection: "+collection_show(env, sessdb));
		
		collection_load(env, sessdb, sesscollection, "collection_jim");
		
		//collection_insert(env, sesscollection, "{\"name\":\"jim\", \"age\": 40}");
		
		System.out.println("Select all: "+collection_select(env, sesscollection, null, null, null, null, null, null, null, null, null));
		
		client_disconnect(env, "sessionId1");
		client_disconnectall(env);
		
	}
	
	public static void client_connect(EnvManager env, String url, String clientId) throws Exception {
		
		if (env.mongoClientObj.containsKey(clientId)) {
			
			throw new Exception("Sorry, the session id '"+clientId+"' already exist.");
			
		}
		
		MongoClient mongoClient = MongoClients.create(url);
		
		env.mongoClientObj.put(clientId, mongoClient);
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray client_show(EnvManager env) throws Exception {
		
		JSONArray result = new JSONArray();
		for(String key : env.mongoClientObj.keySet()) {
			
			result.add(key);
			
		}
		
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray database_show(EnvManager env) throws Exception {
		
		JSONArray result = new JSONArray();
		for(String key : env.mongoBddObj.keySet()) {
			
			result.add(key);
			
		}
		
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray collection_show(EnvManager env) throws Exception {
		
		JSONArray result = new JSONArray();
		for(String key : env.mongoCollectionObj.keySet()) {
			
			result.add(key);
			
		}
		
		return result;
		
	}
	
	public static String client_exist(EnvManager env, String clientId) throws Exception {
		
		if (env.mongoClientObj.containsKey(clientId)) {
			
			return "1";
			
		} else return "0";
		
	}
	
	public static void client_disconnect(EnvManager env, String clientId) throws Exception {
		
		if (!env.mongoClientObj.containsKey(clientId)) {
			
			throw new Exception("Sorry, the session id '"+clientId+"' does not exist.");
			
		}
		
		env.mongoClientObj.get(clientId).close();
		env.mongoClientObj.remove(clientId);
		
	}
	
	public static void client_disconnectall(EnvManager env) throws Exception {
		
		for(String sessionId : env.mongoClientObj.keySet()) {
			
			env.mongoClientObj.get(sessionId).close();
			env.mongoClientObj.remove(sessionId);
			
		}
		
	}

	@SuppressWarnings("unchecked")
	public static JSONArray database_show(EnvManager env, String clientId) throws Exception {
		
		if (!env.mongoClientObj.containsKey(clientId)) {
			
			throw new Exception("Sorry, the client id '"+clientId+"' does not exist.");
			
		}
		
		List<String> names = ImmutableList.copyOf(env.mongoClientObj.get(clientId).listDatabaseNames());
		JSONArray list = new JSONArray();
		for(int i=0;i<names.size();i++) {
			
			list.add(names.get(i));
			
		}
		
		return list;
		
	}
	
	public static String database_exist(EnvManager env, String databaseId) throws Exception {
		
		if (env.mongoBddObj.containsKey(databaseId)) {
			
			return "1";
			
		} else return "0";
		
	}

	public static void database_unload(EnvManager env, String databaseId) throws Exception {
		
		if (!env.mongoBddObj.containsKey(databaseId)) {
			
			throw new Exception("Sorry, the database '"+databaseId+"' is not loaded.");
			
		}
		
		env.mongoBddObj.remove(databaseId);
		
	}

	public static void database_unloadall(EnvManager env) throws Exception {
		
		for(String databaseId : env.mongoBddObj.keySet()) {
			
			env.mongoBddObj.remove(databaseId);
			
		}
		
	}

	@SuppressWarnings("unchecked")
	public static JSONArray collection_show(EnvManager env, String databaseId) throws Exception {
		
		if (!env.mongoBddObj.containsKey(databaseId)) {
			
			throw new Exception("Sorry, the database '"+databaseId+"' does not loaded.");
			
		}
		
		JSONArray list = new JSONArray();
		for (String name : env.mongoBddObj.get(databaseId).listCollectionNames()) {

			list.add(name);
		    
		}
		
		return list;
		
	}

	public static void database_load(EnvManager env, String clientId, String databaseId, String databaseName) throws Exception {
		
		if (!env.mongoClientObj.containsKey(clientId)) {
			
			throw new Exception("Sorry, the client id '"+clientId+"' does not exist.");
			
		}
		
		if (env.mongoBddObj.containsKey(databaseId)) {
			
			throw new Exception("Sorry, the database '"+databaseId+"' already loaded.");
			
		}
		
		env.mongoBddObj.put(databaseId, env.mongoClientObj.get(clientId).getDatabase(databaseName));
		
	}

	@SuppressWarnings("unchecked")
	public static JSONObject database_stat(EnvManager env, String databaseId) throws Exception {

		if (!env.mongoBddObj.containsKey(databaseId)) {
			
			throw new Exception("Sorry, the database '"+databaseId+"' does not exist.");
			
		}
		
		Document stats = env.mongoBddObj.get(databaseId).runCommand(new Document("dbstats", 1));
		JSONObject result = new JSONObject();
        for (Map.Entry<String, Object> set : stats.entrySet()) {

        	result.put(set.getKey(), set.getValue());
            
        }
        
        return result;
		
	}

	public static void collection_load(EnvManager env, String databaseId, String collectionId, String collectionName) throws Exception {
		
		if (!env.mongoBddObj.containsKey(databaseId)) {
			
			throw new Exception("Sorry, the database '"+databaseId+"' does not exist.");
			
		}
		
		if (env.mongoCollectionObj.containsKey(collectionId)) {
			
			throw new Exception("Sorry, the collection id '"+collectionId+"' already exist.");
			
		}
		
		env.mongoCollectionObj.put(collectionId, env.mongoBddObj.get(databaseId).getCollection(collectionName));
		
	}
	
	public static String collection_exist(EnvManager env, String collectionId) throws Exception {
		
		if (env.mongoCollectionObj.containsKey(collectionId)) {
			
			return "1";
			
		} else return "0";
		
	}

	public static void collection_unload(EnvManager env, String collectionId) throws Exception {
		
		if (!env.mongoCollectionObj.containsKey(collectionId)) {
			
			throw new Exception("Sorry, the collection '"+collectionId+"' does not loaded.");
			
		}
		
		env.mongoCollectionObj.remove(collectionId);
		
	}

	public static void collection_unloadall(EnvManager env) throws Exception {
		
		for(String collectionId : env.mongoCollectionObj.keySet()) {
			
			env.mongoCollectionObj.remove(collectionId);
			
		}
		
	}

	public static void collection_insert(EnvManager env, String collectionId, String json) throws Exception {
		
		if (!env.mongoCollectionObj.containsKey(collectionId)) {
			
			throw new Exception("Sorry, the collection id '"+collectionId+"' does not exist.");
			
		}
		
		Document doc = Document.parse(json);
		
		env.mongoCollectionObj.get(collectionId).insertOne(doc);
		
	}

	public static long collection_update(EnvManager env, String collectionId, String jsonTarget, String jsonAction) throws Exception {
		
		if (!env.mongoCollectionObj.containsKey(collectionId)) {
			
			throw new Exception("Sorry, the collection id '"+collectionId+"' does not exist.");
			
		}
		
		return env.mongoCollectionObj.get(collectionId).updateMany(Document.parse(jsonTarget), Document.parse(jsonAction)).getModifiedCount();
		
	}

	public static long collection_delete(EnvManager env, String collectionId, String jsonFilter) throws Exception {
		
		if (!env.mongoCollectionObj.containsKey(collectionId)) {
			
			throw new Exception("Sorry, the collection id '"+collectionId+"' does not exist.");
			
		}
		
		return env.mongoCollectionObj.get(collectionId).deleteMany(BasicDBObject.parse(jsonFilter)).getDeletedCount();
		
	}

	@SuppressWarnings("unchecked")
	public static JSONArray collection_select(EnvManager env, String collectionId, String jsonFilter, String jsonSort, String batchSize, String skip, String limit, String jsonProjection, String jsonHint, String jsonMin, String jsonMax) throws Exception {
		
		if (!env.mongoCollectionObj.containsKey(collectionId)) {
			
			throw new Exception("Sorry, the collection id '"+collectionId+"' does not exist.");
			
		}
		
		MongoCursor<Document> cur;
		FindIterable<Document> fi;
		
		if (jsonFilter!=null && !jsonFilter.equals("")) {
			fi = env.mongoCollectionObj.get(collectionId).find(BasicDBObject.parse(jsonFilter));
		} else {
			fi = env.mongoCollectionObj.get(collectionId).find();
		}
		if (jsonSort!=null && !jsonSort.equals("")) {
			fi.sort(BasicDBObject.parse(jsonSort));
		}
		if (batchSize!=null && !batchSize.equals("")) {
			fi.batchSize(Integer.parseInt(batchSize));
		}
		if (jsonHint!=null && !jsonHint.equals("")) {
			fi.hint(Document.parse(jsonHint));
		}
		if (jsonProjection!=null && !jsonProjection.equals("")) {
			fi.projection(BasicDBObject.parse(jsonProjection));
		}
		if (limit!=null && !limit.equals("")) {
			fi.limit(Integer.parseInt(limit));
		}
		if (jsonMax!=null && !jsonMax.equals("")) {
			fi.max(BasicDBObject.parse(jsonMax));
		}
		if (jsonMin!=null && !jsonMin.equals("")) {
			fi.min(BasicDBObject.parse(jsonMin));
		}
		if (skip!=null && !skip.equals("")) {
			fi.skip(Integer.parseInt(skip));
		}
		cur = fi.iterator();
		JSONArray result = new JSONArray();
		JSONParser jp = new JSONParser();
		while (cur.hasNext()) {
			
			result.add((JSONObject) jp.parse(cur.next().toJson()));
	        
		}
		
		return result;
		
	}
	
	
	
}
