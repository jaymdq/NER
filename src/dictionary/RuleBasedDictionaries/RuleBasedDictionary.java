package dictionary.RuleBasedDictionaries;

import java.util.Vector;

import dictionary.Chunk;
import dictionary.Dictionary;

public class RuleBasedDictionary implements Dictionary {

	private Vector<RegExMatcher> matchers;
	
	public RuleBasedDictionary(Vector<RegExMatcher> matchers){
		this.matchers = matchers;
	}
	
	public RuleBasedDictionary(){
		this.matchers = new Vector<RegExMatcher>();
	}
	
	public void addMatcher(RegExMatcher matcher){
		matchers.add(matcher);
	}

	public Vector<RegExMatcher> getMatchers() {
		return matchers;
	}

	public void setMatchers(Vector<RegExMatcher> matchers) {
		this.matchers = matchers;
	}
	
	public Vector<Chunk> recognize(String text, boolean debugMode){
		Vector<Chunk> out = new Vector<Chunk>();
		for (RegExMatcher matcher : matchers){
			out.addAll(matcher.recognize(text));
		}
		
		if (debugMode)
			System.out.println(out);
		
		return out;
	}
	
}
