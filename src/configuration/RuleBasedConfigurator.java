package configuration;

import java.util.Vector;

import dictionary.dictionaryEntry.DictionaryEntry;
import dictionary.ruleBasedDictionaries.RegExMatcher;
import dictionary.ruleBasedDictionaries.RuleBasedDictionary;

public class RuleBasedConfigurator extends AbsDictionaryConfigurator {

	// Variables

	private Vector<RegExMatcher> matchers;
	
	// Constructors

	public RuleBasedConfigurator(String name, Vector<DictionaryEntry> entries, Vector<RegExMatcher> matchers) {
		super(name, entries);
		this.setMatchers(matchers);
	}
	
	// Getters and Setters

	public Vector<RegExMatcher> getMatchers() {
		return matchers;
	}

	public void setMatchers(Vector<RegExMatcher> matchers) {
		this.matchers = matchers;
	}
	
	// Methods
	
	@Override
	protected int checkParameters(Vector<String> params) {
		return 0;
	}

	@Override
	protected Object configureObject() {
		if (this.matchers == null)
			this.matchers = new Vector<RegExMatcher>();
		return new RuleBasedDictionary(this.matchers);
	}

	@Override
	public String getErrorReason() {
		return "No errors";
	}
}
