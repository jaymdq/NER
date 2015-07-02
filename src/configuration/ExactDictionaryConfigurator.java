package configuration;

import java.util.Vector;

import dictionary.dictionaryentry.DictionaryEntry;
import dictionary.exactDictionaries.ExactDictionary;

public class ExactDictionaryConfigurator extends AbsDictionaryConfigurator {

	// Variables

	// Constructors

	// Getters and Setters

	public ExactDictionaryConfigurator(String name, Vector<DictionaryEntry> entries) {
		super(name,entries);
	}

	// Methods
	@Override
	protected int checkParameters(Vector<String> params) {
		int out = 0;

		setParameter("-c",false);
		setParameter("-a",false);
		
		for (String param : params){
			switch(param){
			case "-c" :
			case "-C" :
					setParameter("-c",true);
					break;
			case "-a":
			case "-A":
					setParameter("-a",true);
					break;
			default:
				out = 1;
				break;
			}
		}

		return out;
	}

	@Override
	protected Object configureObject() {
		ExactDictionary out = null;

		//Parameters
		boolean caseSensitive = (boolean) getParameter("-c");
		boolean allMatches = (boolean) getParameter("-a");
		
		out = new ExactDictionary(getEntries(),caseSensitive,allMatches);
				
		return out;
	}

}
