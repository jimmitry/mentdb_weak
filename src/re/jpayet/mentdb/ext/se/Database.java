package re.jpayet.mentdb.ext.se;

import java.util.LinkedHashMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
	
	public String id;
	public long id_rel = 0L;
	public long id_ent = 0L;
	public ConcurrentHashMap<Byte, String> cityNames = new ConcurrentHashMap<Byte, String>();
	public ConcurrentHashMap<Byte, String> zoneNamesFr = new ConcurrentHashMap<Byte, String>();
	public ConcurrentHashMap<Byte, String> zoneNamesEn = new ConcurrentHashMap<Byte, String>();
	public ConcurrentHashMap<Long, String> companyNames = new ConcurrentHashMap<Long, String>();
	public ConcurrentHashMap<Long, String> companyDescriptionsFr = new ConcurrentHashMap<Long, String>();
	public ConcurrentHashMap<Long, String> companyDescriptionsEn = new ConcurrentHashMap<Long, String>();
	public ConcurrentHashMap<Long, Vector<Long>> companyEntities = new ConcurrentHashMap<Long, Vector<Long>>();
	public ConcurrentHashMap<Long, Entity> entities = new ConcurrentHashMap<Long, Entity>();
	public ConcurrentHashMap<Long, Vector<Long>> companies = new ConcurrentHashMap<Long, Vector<Long>>();
	public ConcurrentHashMap<Long, Relation> relations = new ConcurrentHashMap<Long, Relation>();
	
	public ConcurrentHashMap<Byte, Vector<Relation>> zones_relations = new ConcurrentHashMap<Byte, Vector<Relation>>();
	public ConcurrentHashMap<Byte, Vector<Long>> companies_by_city = new ConcurrentHashMap<Byte, Vector<Long>>();
	public ConcurrentHashMap<String, Vector<Long>> pilesEn = new ConcurrentHashMap<String, Vector<Long>>();
	public ConcurrentHashMap<String, Vector<Long>> pilesFr = new ConcurrentHashMap<String, Vector<Long>>();

	public ConcurrentHashMap<String, LinkedHashMap<String, Integer>> allSymbolsEn = new ConcurrentHashMap<String, LinkedHashMap<String, Integer>>();
	public ConcurrentHashMap<String, LinkedHashMap<String, Integer>> allSymbolsFr = new ConcurrentHashMap<String, LinkedHashMap<String, Integer>>();

}
