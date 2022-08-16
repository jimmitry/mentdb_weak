package re.jpayet.mentdb.ext.bot;

public class SimiElementTrigger
{

	public String word = null;
	public int hash = -1;
	public boolean is_var = false;
	
	public SimiElementTrigger(String word) {
		this.word = word;
		this.hash = word.toLowerCase().hashCode();
		if (word.startsWith("[")) {
			is_var = true;
		}
	}

}