package dictionary.ruleBasedDictionaries;

import java.util.Vector;

import twitter4j.Logger;
import dictionary.Dictionary;
import dictionary.chunk.Chunk;
import dictionary.exactDictionaries.ExactDictionary;

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
		if (debugMode){
			Logger.getLogger(ExactDictionary.class).info("Recognition Started");
		}
		
		Vector<Chunk> out = new Vector<Chunk>();
		for (RegExMatcher matcher : matchers){
			out.addAll(matcher.recognize(text));
		}
		
		if (debugMode){
			Logger.getLogger(ExactDictionary.class).info("Chunks Found:");
			for (Chunk chunk : out)
				Logger.getLogger(ExactDictionary.class).info(chunk.toString());
			Logger.getLogger(ExactDictionary.class).info("Rule Based Dictionary Finished\n");
		}
			
		return out;
	}
	
}
