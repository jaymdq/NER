package dictionary.approximatedDictionaries;

import java.util.Vector;

import dictionary.dictionaryEntry.DictionaryEntry;

public class TrieNodeLeaf extends AbsTrieNode {
	
	// Variables
	Vector<DictionaryEntry> entriesList = null;
	
	// Constructors
	public TrieNodeLeaf(Character character) {
		super(character);
		this.entriesList = new Vector<DictionaryEntry>();
	}

	// Methods
	
	@Override	
	public Vector<DictionaryEntry> getListOfDictionaryEntries(String text) {
		if (!text.isEmpty())
			return null;
		return this.entriesList;
	}

	@Override
	public void addToMap(String text, DictionaryEntry dictionaryEntry) {
		if (!text.isEmpty())
			return;
		this.entriesList.addElement(dictionaryEntry);
	}

}
