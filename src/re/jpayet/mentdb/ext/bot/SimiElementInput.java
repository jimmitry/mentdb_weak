package re.jpayet.mentdb.ext.bot;

public class SimiElementInput
{

	public String word = null;
	public int hash = -1;
	public boolean locked = false;
	public boolean is_equal = false;
	public boolean is_next = false;
	
	public SimiElementInput(String word) {
		this.word = word;
		this.hash = word.toLowerCase().hashCode();
	}
	
	public String toString() {
		return word+" locked:"+locked+" is_equal="+is_equal+" is_next="+is_next;
	}

}