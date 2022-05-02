package re.jpayet.mentdb.ext.se;

public class Relation {

	public boolean is_en = true;
	public byte city = 0;
	public byte zone = 0;
	public long company = 0;
	public String sentence = "";
	public Result result = null;
	public Entity entity = null;
	public long id;
	
	public Relation(long id, byte city, byte zone, long company, boolean en, String sentence, Result result) {

		this.id = id;
		this.city = city;
		this.zone = zone;
		this.company = company;
		this.is_en = en;
		this.sentence = sentence;
		this.result = result;
		
	}
	
	public Relation(long id, byte city, byte zone, long company, Entity entity, boolean en, String sentence, Result result) {

		this.id = id;
		this.city = city;
		this.zone = zone;
		this.company = company;
		this.is_en = en;
		this.sentence = sentence;
		this.result = result;
		this.entity = entity;
		
	}

}
