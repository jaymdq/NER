package configuration;

import java.util.Vector;

import dictionary.dictionaryentry.DictionaryEntry;

public abstract class AbsDictionaryConfigurator extends AbsConfigurator {

	// Variables
	protected Vector<DictionaryEntry> entries;
	
	// Constructors
	
	public AbsDictionaryConfigurator(String name,Vector<DictionaryEntry> entries) {
		super(name);
		this.setEntries(entries);
	}

	// Getters and Setters
	
	protected Vector<DictionaryEntry> getEntries() {
		return entries;
	}

	protected void setEntries(Vector<DictionaryEntry> entries) {
		this.entries = entries;
	}
	
	// Methods

}
