package dictionary.ApproximatedDictionaries;

import java.util.Vector;

import dictionary.DictionaryEntry;

public abstract class AbsTrieNode {
	
	protected Character character;
	
	/**
	 * Contructor de la clase abstracta de los nodos del arbol trie
	 * @param character El parametro character define el caracter directamente relacionado con el nodo.
	 */
	
	public AbsTrieNode(Character character) {
		super();
		this.character = character;
	}
	
	/**
	 * 
	 * @return El caracter directamente relacionado con el nodo.
	 */
	
	public Character getCharacter() {
		return character;
	}
	
	/**
	 * 
	 * @param character El parametro character define el caracter directamente relacionado con el nodo.
	 */
	
	public void setCharacter(Character character) {
		this.character = character;
	}
	
	public abstract Vector<DictionaryEntry> getListOfDictionaryEntries(String text);
	public abstract void addToMap(String text, DictionaryEntry dictionaryEntry);
}
