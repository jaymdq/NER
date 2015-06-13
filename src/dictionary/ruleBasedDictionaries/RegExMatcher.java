package dictionary.ruleBasedDictionaries;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dictionary.chunk.Chunk;
import dictionary.chunk.score.Score;

public class RegExMatcher {

	// Variables
	
	private String regEx;
	private Pattern pattern;
	private String category;

	// Constructors
	
	public RegExMatcher(String regEx, String category) {
		this.setRegEx(regEx);
		this.setCategory(category);
		pattern = Pattern.compile(regEx);
	}

	// Getters And Setters
	
	public String getRegEx() {
		return regEx;
	}

	public void setRegEx(String regEx) {
		this.regEx = regEx;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	// Methods
	
	public Vector<Chunk> recognize(String text){
		Vector<Chunk> out = new Vector<Chunk>();
		Matcher matcher = pattern.matcher(text);
		while ( matcher.find() ) {
			int start = matcher.start();
			int end = matcher.end();
			Chunk chunk = new Chunk(start, end, category, text,Score.getInstance().getExactScore());
			out.add(chunk);
		}

		return out;

	}

}
