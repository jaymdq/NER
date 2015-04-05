package dictionary;

import java.util.Vector;

public abstract class AbsTrieNode {
	
	protected Character character;
	
	public AbsTrieNode(Character character) {
		super();
		this.character = character;
	}
	
	public Character getCharacter() {
		return character;
	}
	
	public void setCharacter(Character character) {
		this.character = character;
	}
	
	public abstract Vector<DictionaryEntry> getListOfDictionaryEntries(String text);
	public abstract void addToMap(String text, DictionaryEntry dictionaryEntry);
}
