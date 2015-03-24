package dictionary;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExMatcher {

	private String regEx;
	private Pattern pattern;
	private String category;


	public RegExMatcher(String regEx, String category) {
		this.setRegEx(regEx);
		this.setCategory(category);
		pattern = Pattern.compile(regEx);
	}


	public Vector<Chunk> recognize(String text){
		Vector<Chunk> out = new Vector<Chunk>();
		Matcher matcher = pattern.matcher(text);
		while ( matcher.find() ) {
			int start = matcher.start();
			int end = matcher.end();
			Chunk chunk = new Chunk(start, end, category, text);
			out.add(chunk);
		}

		return out;

	}

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

}
