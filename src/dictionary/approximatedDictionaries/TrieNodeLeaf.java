package dictionary.approximatedDictionaries;

import java.util.Vector;

import dictionary.DictionaryEntry;

public class TrieNodeLeaf extends AbsTrieNode {
	Vector<DictionaryEntry> entriesList = null;
	
	public TrieNodeLeaf(Character character) {
		super(character);
		this.entriesList = new Vector<DictionaryEntry>();
	}

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
